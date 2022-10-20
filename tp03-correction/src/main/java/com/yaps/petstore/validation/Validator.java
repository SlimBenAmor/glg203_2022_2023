package com.yaps.petstore.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.yaps.petstore.exceptions.ValidationException;
import com.yaps.petstore.validation.annotation.ContainsOnly;
import com.yaps.petstore.validation.annotation.IsNotEmpty;

public class Validator {

    /**
     * Cette map relie les annotations au code qui effectue réellement la
     * validation.
     */
    private Map<Class<? extends Annotation>, MethodChecker> annotationProcessorMap = new HashMap<>();

    public Validator() {
        // On configure le validateur
        annotationProcessorMap.put(IsNotEmpty.class, this::checkIsNotEmpty);
        // ici, votre code pour d'autres annotations...
        annotationProcessorMap.put(ContainsOnly.class, this::checkOnlyContains);
    }

    /**
     * Cette méthode valide un objet à partir de ses annotations.
     * 
     * NE PAS LA MODIFIER !
     * @param o
     * @throws ValidationException
     */
    public void validateObject(Object o) throws ValidationException {
        // On récupère la classe de l'objet pour accéder aux annotations :
        Class<? extends Object> clazz = o.getClass();
        // on parcourt les méthodes de la classe pour chercher les getters annotés
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            // on regarde si c'est un getter (voir la méthode dans la classe Validator)
            if (isGetter(m)) {
                // On cherche les annotations de validation...
                for (Annotation a : m.getAnnotations()) {
                    // Ce sont celles qui sont enregistrées dans la map...
                    MethodChecker processor = annotationProcessorMap.get(a.annotationType());
                    if (processor != null) {
                        // on récupère le processeur et on l'utilise pour valider la propriété.
                        Optional<ValidationErrorMessage> error = processor.checkMethod(a, m, o);
                        if (error.isPresent()) {
                            throw new ValidationException(error.get().getMessage());
                        }
                        // si ValidationException n'était pas une exception déclarée, on pourrait utiliser :
                        // processor.checkMethod(a, m, o).ifPresent(e -> {throw new
                        // ValidationException(e.getMessage())});
                    }
                }
            }
        }
    }

    private Optional<ValidationErrorMessage> checkOnlyContains(Annotation a, Method m, Object o) {
        Object res;
        try {
            res = m.invoke(o);
            ContainsOnly actualAnnotation = (ContainsOnly) a;
            String message = actualAnnotation.message();
            if (res != null) {
                Set<Integer> possibleCharacters = actualAnnotation.value().chars().boxed().collect(Collectors.toSet());

                String s = (String) res;

                for (int i = 0; i < s.length(); i++) {
                    if (!possibleCharacters.contains(Integer.valueOf(s.charAt(i)))) {
                        return Optional.of(new ValidationErrorMessage(message));
                    }
                }
            }
            return Optional.empty();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            throw new BadValidationConfiguration(
                    "Method " + m.getName() + " should return a String or not use ContainsOnly annotation");
        }
    }

    private Optional<ValidationErrorMessage> checkIsNotEmpty(Annotation a, Method m, Object o) {
        try {
            IsNotEmpty actualAnnotation = (IsNotEmpty) a;
            Object res = m.invoke(o);
            boolean ok = true;
            if (res == null) {
                ok = false;
            } else if (res instanceof String) {
                String s = (String) res;
                ok = !s.isEmpty();
            } else if (res instanceof Collection) {
                Collection<?> col = (Collection<?>) res;
                ok = !col.isEmpty();
            }
            if (ok) {
                return Optional.empty();
            } else {
                return Optional.of(
                        new ValidationErrorMessage(actualAnnotation.message()));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Is this method a getter.
     * Not private to ease testing.
     * 
     * @param m
     * @return
     */
    boolean isGetter(Method m) {
        String methodName = m.getName();
        return methodName.length() >= 4
                && methodName.startsWith("get")
                && Character.isUpperCase(methodName.charAt(3))
                && m.getParameterCount() == 0;
    }
}
