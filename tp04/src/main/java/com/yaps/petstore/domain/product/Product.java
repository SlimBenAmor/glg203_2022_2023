package com.yaps.petstore.domain.product;

import com.yaps.petstore.domain.category.Category;
import com.yaps.utils.model.CheckException;
import com.yaps.utils.model.DomainObject;


/**
 * This class represents a Product in the catalog of the YAPS company.
 * The catalog is divided into categories. Each one divided into products
 * and each product in items.
 */
public final class Product extends DomainObject {

    private String name;
    private String description;
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

    public void checkData() throws CheckException {
        if (id == null || "".equals(id))
            throw new CheckException("Invalid id");
        if (name == null || "".equals(name))
            throw new CheckException("Invalid name");
        if (description == null || "".equals(description))
            throw new CheckException("Invalid description");
        if (category == null || "".equals(category.getId()))
            throw new CheckException("Invalid category");
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
