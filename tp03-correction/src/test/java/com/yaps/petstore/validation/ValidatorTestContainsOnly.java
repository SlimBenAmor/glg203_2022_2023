package com.yaps.petstore.validation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.yaps.petstore.exceptions.ValidationException;
import com.yaps.petstore.validation.ValidatorTest.Person;
import com.yaps.petstore.validation.annotation.ContainsOnly;
import com.yaps.petstore.validation.annotation.IsNotEmpty;

public class ValidatorTestContainsOnly {

    static final class Person {
        private String nom, prenom;

        public Person(String nom, String prenom) {
            this.nom = nom;
            this.prenom = prenom;
        }

        @IsNotEmpty
        @ContainsOnly("abcdefghijklmnopqrstuvwxyz")
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
    public void testNomMalForme() {
        assertThrows(ValidationException.class,
                () -> {
                    Person p = new Person("a7", "bb");
                    new Validator().validateObject(p);
                });
    }
}
