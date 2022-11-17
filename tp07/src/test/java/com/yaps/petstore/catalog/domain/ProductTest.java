package com.yaps.petstore.catalog.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.yaps.common.model.CheckException;

public class ProductTest {

    Category category = new Category("c", "c", "c");
    
    @Test
    public void testSimpleCreation() {        
        Product p = new Product("p", "p", "p", category);
        assertEquals("c", p.getCategory().getId());
    }

    @Test
    public void testIdNotEmpty() {
        assertThrows(CheckException.class, () -> {
            Product p = new Product("", "p", "p", category);
            p.checkData();
        });
    }

    @Test
    public void testNameNotEmpty() {
        assertThrows(CheckException.class, () -> {
            Product p = new Product("p", "", "p", category);
            p.checkData();
        });
    }

   

    @Test
    public void testDescriptionNotEmpty() {
        assertThrows(CheckException.class, () -> {
            Product p = new Product("p", "p", "", category);
            p.checkData();
        });
    }

   

    @Test
    public void checkToString() {
        Category cat = new Category("cat1", "name1", "descr1");
        Product p = new Product("p0", "n0", "d0", cat);
        // the category is displayed in its short form.
        String expected = "product id=p0, name=n0\ncategory=cat1\tname1\ndescription=d0";
        String computed = p.toString();
        assertEquals(expected, computed, "toString should be defined");        
    }

    @Test
    public void checkShort() {
        Category cat = new Category("cat1", "name1", "descr1");
        Product p = new Product("p0", "n0", "d0", cat);
        // the category is displayed in its short form.
        String expected = "p0\tn0";
        String computed = p.shortDisplay();
        assertEquals(expected, computed, "toString should be defined");        

    }


    @Test
    public void testNullId() {
        assertThrows(NullPointerException.class,
                () -> new Product(null, "a", "a", category));
    }

    @Test
    public void testNullName() {
        assertThrows(NullPointerException.class,
                () -> new Product("a", null, "a", category));
    }

    @Test
    public void testNullDescription() {
        assertThrows(NullPointerException.class,
             () -> new Product("a", "a", null, category));
    }

    @Test
    public void testNullCategory() {
        assertThrows(NullPointerException.class,
             () -> new Product("a", "a", "a", null));
    }

    

}
