package com.pb.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pb.serializer.JsonValueMappingSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author PengBin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@JacksonAnnotationsInside
@JsonSerialize(using = JsonValueMappingSerializer.class)
public @interface JsonValueMapping {

    String value() default "";

    /**
     * javascript Expression
     * <pre>
     *     com.pb.constants.GenderDictionary.of(arg0)
     * </pre>
     * @return
     */
    String script() default "";
}
