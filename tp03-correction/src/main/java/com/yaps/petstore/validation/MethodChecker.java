package com.yaps.petstore.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

public interface MethodChecker {
    
    /**
     * Checks if the result of calling m on o gives a reasonable value, according to annotation a.
     * Normally, a method checker is specific to a certain kind of annotations ; the annotation 
     * is passed as a parameter to access possible annotation arguments.
     * @param a the annotation being processed
     * @param m the getter method for the property to check
     * @param o the object to check
     * @return an error message if an error has been detected.
     */
    Optional<ValidationErrorMessage> checkMethod(Annotation a, Method m, Object o);
}
