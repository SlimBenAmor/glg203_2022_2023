package com.yaps.petstore.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.yaps.petstore.exceptions.ValidationException;
import com.yaps.petstore.validation.annotation.IsNotEmpty;

public class ValidatorTest {
    static final class Person {
        private String nom, prenom;

        public Person(String nom, String prenom) {
            this.nom = nom;
            this.prenom = prenom;
        }

        @IsNotEmpty
        public String getNom() {
            return nom;
        }

        @IsNotEmpty
        public String getPrenom() {
            return prenom;
        }

        public String getString() {
            return nom + " " + prenom;
        }

        public void affiche() {
            System.out.println(getString());
        }

        public char getLettreNom(int pos) {
            return nom.charAt(pos);
        }
    }

    @Test
    void testIsGetterOk() throws NoSuchMethodException, SecurityException {
        boolean computed = new Validator().isGetter(Person.class.getMethod("getNom"));
        assertTrue(computed, "getNom is a getter");
    }

    @Test
    void testIsGetterNotOk() throws NoSuchMethodException, SecurityException {
        boolean computed = new Validator().isGetter(Person.class.getMethod("affiche"));
        assertFalse(computed, "affiche is not a getter");
    }

    @Test
    void testIsGetterNotOk1() throws NoSuchMethodException, SecurityException {
        boolean computed = new Validator().isGetter(Person.class.getMethod("getLettreNom", Integer.TYPE));
        assertFalse(computed, "getLettreNom is not a getter");
    }

    @Test
    public void testObjectOk() throws ValidationException {
        assertDoesNotThrow(() -> {
            Person p = new Person("aa", "bb");
            new Validator().validateObject(p);
        });
    }

    @Test
    public void testNomNull() {
        assertThrows(ValidationException.class,
                () -> {
                    Person p = new Person(null, "bb");
                    new Validator().validateObject(p);
                });
    }

    @Test
    public void testNomVide() {
        assertThrows(ValidationException.class,
                () -> {
                    Person p = new Person("", "bb");
                    new Validator().validateObject(p);
                });
    }


    @Test
    public void testPrenomNull() {
        assertThrows(ValidationException.class,
                () -> {
                    Person p = new Person("aa", null);
                    new Validator().validateObject(p);
                });
    }

}
