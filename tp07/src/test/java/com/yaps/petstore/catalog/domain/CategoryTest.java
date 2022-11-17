package com.yaps.petstore.catalog.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.yaps.common.model.CheckException;

public class CategoryTest {

    /**
     * This test tries to create an object with a invalid values.
     */
    @Test
    public void testCreateCategoryWithInvalidValues() throws Exception {

        // Creates an object with empty values
        try {
            final Category category = new Category(new String(), new String(), new String());
            category.checkData();
            fail("Object with empty values should not be created");
        } catch (CheckException e) {
        }
    }

    @Test
    public void checkToString() {
        Category cat = new Category("cat1", "name1", "descr1");
        String expected = "category id=cat1, name=name1\ndescription=descr1";
        String computed = cat.toString();
        assertEquals(expected, computed, "toString should be defined");        
    }

    @Test
    public void checkShort() {
        Category cat = new Category("cat1", "name1", "descr1");
        String expected = "cat1\tname1";
        String computed = cat.shortDisplay();
        assertEquals(expected, computed, "shortDisplay should be defined");        
    }

    @Test
    public void testNullId() {
        assertThrows(NullPointerException.class, 
            () -> new Category(null)
        );
    }

    @Test
    public void testNullId2() {
        assertThrows(NullPointerException.class, 
            () -> new Category(null, "a", "a")
        );
    }

    @Test
    public void testNullName() {
        assertThrows(NullPointerException.class, 
            () -> new Category("id1", null, "a")
        );
    }

    @Test
    public void testNullDescr() {
        assertThrows(NullPointerException.class, 
            () -> new Category("id1", "dsfdf", null)
        );
    }

    @Test
    public void testNullSetId() {
        assertThrows(NullPointerException.class, 
            () -> {
                Category cat = new Category("a", "a", "a");
                cat.setId(null);
            } 
        );
    }

    @Test
    public void testNullSetName() {
        assertThrows(NullPointerException.class, 
            () -> {
                Category cat = new Category("a", "a", "a");
                cat.setName(null);
            } 
        );
    }

    @Test
    public void testNullSetDescription() {
        assertThrows(NullPointerException.class, 
            () -> {
                Category cat = new Category("a", "a", "a");
                cat.setDescription(null);
            } 
        );
    }


}
