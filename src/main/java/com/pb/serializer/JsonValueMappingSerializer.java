package com.pb.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.pb.annotation.JsonValueMapping;
import com.pb.util.ScriptContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 关于只创建一次，参考了如下
 * https://blog.csdn.net/iter_zc/article/details/40543365/
 * <p>
 * @author PengBin
 */
public class JsonValueMappingSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>(128);

    private static ConcurrentHashMap<String, SpinStatus> raceUtil = new ConcurrentHashMap<>();


    private JsonValueMapping jsonValueMapping;

    public JsonValueMappingSerializer() {
        super();
    }

    public JsonValueMappingSerializer(JsonValueMapping jsonValueMapping) {
        this.jsonValueMapping = jsonValueMapping;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        String script = jsonValueMapping.script();
        if (script.trim().length() == 0) {
            gen.writeObject(value);
            return;
        }

        String name = jsonValueMapping.value();
        Object result;
        boolean cacheable = jsonValueMapping.cacheable();
        if (cacheable) {
            String key = script + value;
            result = cache.get(key);
            if (result == null) {
                SpinStatus spinStatus = new SpinStatus();
                SpinStatus oldSpinStatus = raceUtil.putIfAbsent(key, spinStatus);

                // 只有第一个线程访问到的时候为 null
                if (oldSpinStatus == null) {

                    try {
                        result = evaluateScriptExpression(script, value);
                        if (result != null) {
                            cache.putIfAbsent(key, result);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("evaluateScriptExpression: " + script);
                    } finally {
                        spinStatus.released = true;
                    }
                } else {
                    // 其他情况下，等待
                    while (!oldSpinStatus.released) {
                    }
                }

                result = cache.get(key);
            }
        } else {
            result = evaluateScriptExpression(script, value);
        }

        String text = result == null ? null : result.toString();
        if (name.trim().length() == 0) {
            gen.writeObject(text);
        } else {
            gen.writeObject(value);
            gen.writeStringField(name, text);
        }

        //        System.out.println(gen.getOutputContext().getCurrentName());
        //        System.out.println(gen.getOutputContext().typeDesc());
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return prov.findNullValueSerializer(null);
        }

        JsonValueMapping annotation = property.getAnnotation(JsonValueMapping.class);
        if (annotation == null) {
            annotation = property.getContextAnnotation(JsonValueMapping.class);
        }

        if (annotation != null) {
            return new JsonValueMappingSerializer(annotation);
        }
        return prov.findValueSerializer(property.getType(), property);
    }


    /**
     * 执行脚本
     * @param script
     * @param value
     * @return
     */
    private Object evaluateScriptExpression(String script, Object value) {
        ScriptContext scriptContext = new ScriptContext(script);
        Map<String, Object> bindings = new HashMap<>(1);
        bindings.put("arg0", value);

        return scriptContext.evaluateScriptExpression(bindings);
    }

    /**
     * 自旋类
     */
    private static class SpinStatus {
        volatile boolean released;
    }
}
