package com.yaps.petstore.catalog.dao;

import com.yaps.petstore.catalog.domain.Category;
import com.yaps.petstore.catalog.domain.Item;
import com.yaps.petstore.catalog.domain.Product;

/**
 * Utility class for holding the content of the default test DB for DBUnit.
 */
public class DefaultContent {

    private DefaultContent() {
    }

    /**
     * The categories found in the default test setting.
     */
    public static final Category[] DEFAULT_CATEGORIES = {
            new Category("a", "name_a", "descr_a"),
            new Category("b", "name_b", "descr_b")
    };

    /**
     * XML representation of the categories for DBUnit.
     */
    public static final String CATEGORY_XML[] = {
            "<category id='a' name='name_a' description='descr_a'/>",
            "<category id='b' name='name_b' description='descr_b'/>"
    };

    /**
     * The products found in the default test setting.
     * 
     * @throws Exception
     */

    public static final Product[] DEFAULT_PRODUCTS = {
            new Product("pa", "name_pa", "descr_pa", DEFAULT_CATEGORIES[0]),
            new Product("pb", "name_pb", "descr_pb", DEFAULT_CATEGORIES[1]),
            new Product("pc", "name_pc", "descr_pc", DEFAULT_CATEGORIES[1])
    };

    /**
     * XML representation of the products for DBUnit.
     */

    public static final String PRODUCT_XML[] = {
        "<product id='pa' name='name_pa' description='descr_pa' category_fk='a'/>",
        "<product id='pb' name='name_pb' description='descr_pb' category_fk='b'/>",
        "<product id='pc' name='name_pc' description='descr_pc' category_fk='b'/>"
    };

    public static final Item[] DEFAULT_ITEMS = {
        new Item("ia", "name_ia", 10, "imagea", DEFAULT_PRODUCTS[1]),
        new Item("ib", "name_ib", 30, "imageb", DEFAULT_PRODUCTS[1]),
        new Item("ic", "name_ic", 20, "imagec", DEFAULT_PRODUCTS[0])
    };

    public static final String ITEM_XML[] = {
        "<item id='ia' name ='name_ia' unit_cost='10' product_fk='pb' image_path='imagea'/>",
        "<item id='ib' name ='name_ib' unit_cost='30' product_fk='pb' image_path='imageb'/>",
        "<item id='ic' name ='name_ic' unit_cost='20' product_fk='pa' image_path='imagec'/>",        
    };

}
