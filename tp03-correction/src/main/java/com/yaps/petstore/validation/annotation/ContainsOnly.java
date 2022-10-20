package com.yaps.petstore.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation checks the content of a String.
 * Its value is the set of authorized characters.
 * The checked property is ok if it's not null and contains only
 * characters in the set. It can be empty, though.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ContainsOnly {
    String message() default "badly formed";
    String value();
}
