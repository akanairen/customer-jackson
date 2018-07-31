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

/**
 * @author PengBin
 */
public class JsonValueMappingSerializer extends JsonSerializer<Object> implements ContextualSerializer {

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
        }

        String script = jsonValueMapping.script();
        if (script.trim().length() == 0) {
            gen.writeObject(value);
        }

        // TODO 此处可以做优化，可以尝试根据Script和Value做缓存处理
        ScriptContext scriptContext = new ScriptContext(script);
        Map<String, Object> bindings = new HashMap<>(1);
        bindings.put("arg0", value);

        Object result = scriptContext.evaluateScriptExpression(bindings);
        String text = result == null ? null : result.toString();
        String name = jsonValueMapping.value();
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
        System.out.println("---------------------");
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
}
