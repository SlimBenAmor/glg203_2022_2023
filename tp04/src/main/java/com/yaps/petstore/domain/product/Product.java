package com.yaps.petstore.domain.product;

import java.util.List;
import com.yaps.petstore.domain.category.Category;
import com.yaps.utils.model.DomainObject;
import com.yaps.petstore.domain.annotation.propertyMetaData;


/**
 * This class represents a Product in the catalog of the YAPS company.
 * The catalog is divided into categories. Each one divided into products
 * and each product in items.
 */
public final class Product extends DomainObject {
    @propertyMetaData(order = 2, columnName = "name")
    private String name;
    @propertyMetaData(order = 3, columnName = "description")
    private String description;
    @propertyMetaData(order = 4, columnName = "category_fk")
    private Category category;

    public Product() {
    }

    public Product(final String id) {
        super(id);
    }

    public Product(final String id, final String name, final String description, Category category) {
        super(id);
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public Product(final List<Object> argList) {
        super((String) argList.get(0));
        this.name = (String) argList.get(1);
        this.description = (String) argList.get(2);
        this.category = (Category) argList.get(3);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("product id=").append(id);
        buf.append(", name=").append(name);
        buf.append("\ncategory=").append(category.shortDisplay());
        buf.append("\ndescription=").append(description);
        return buf.toString();
    }

    @Override
    public String shortDisplay() {
        return id + "\t" + name;
    }
}
