package com.yaps.petstore.catalog.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yaps.common.model.CheckException;

public class ItemTest {

    Category category;
    Product product;

    @BeforeEach
    public void beforeEach() {
        category = new Category("c0", "nc0", "dc0");
        product = new Product("p0", "np0", "dp0", category);
    }

    @Test
    public void testCreateItem() {
        Item item = new Item("i0", "ni0", 100, "image1", product);
        assertEquals("i0", item.getId());
        assertEquals("ni0", item.getName());
        assertEquals("image1", item.getImagePath());
        assertEquals(100.0, item.getUnitCost(), 1e-8);
        assertEquals("np0", item.getProduct().getName());
    }

    @Test
    public void testCheck1() {

        assertThrows(CheckException.class, () -> {
            Item it = new Item("", "a", 1000, "image", product);
            it.checkData();
        },
                "empty id illegal");

        assertThrows(CheckException.class, () -> {
            Item it = new Item("i0", "", 1000, "image", product);
            it.checkData();
        },
                "empty name illegal");
    }

    @Test
    public void testShortDisplay() {
        Item it = new Item("i0", "ni0", 100, "image", product);
        String expected = "i0\tni0";
        String computed = it.shortDisplay();
        assertEquals(expected, computed);
    }

    @Test
    public void testToString() {
        Item it = new Item("i0", "ni0", 100, "pict", product);
        String expected = "item id=i0, name=ni0, image path=pict, product=p0\tnp0, unit cost=100.0";
        String computed = it.toString();
        assertEquals(expected, computed);
    }

    @Test
    public void testNullId() throws Exception {
        assertThrows(NullPointerException.class, () -> new Item(null, "a", 0, "a", product));
    }

    @Test
    public void testNullName() throws Exception {
        assertThrows(NullPointerException.class, () -> new Item("a", null, 0, "a", product));
    }

    @Test
    public void testNullImagePath() throws Exception {
        assertThrows(NullPointerException.class, () -> new Item("a", "a", 0, null, product));
    }

    @Test
    public void testNullSetId() throws Exception {
        assertThrows(NullPointerException.class,
                () -> {
                    Item item = new Item("a", "a", 0, "a", product);
                    item.setId(null);
                });
    }


    @Test
    public void testNullSetName() throws Exception {
        assertThrows(NullPointerException.class,
                () -> {
                    Item item = new Item("a", "a", 0, "a", product);
                    item.setName(null);
                });
    }



    @Test
    public void testNullSetImagePath() throws Exception {
        assertThrows(NullPointerException.class,
                () -> {
                    Item item = new Item("a", "a", 0, "a", product);
                    item.setImagePath(null);
                });
    }

    @Test
    public void testNullSetProduct() throws Exception {
        assertThrows(NullPointerException.class,
                () -> {
                    Item item = new Item("a", "a", 0, "a", product);
                    item.setProduct(null);
                });
    }


}
