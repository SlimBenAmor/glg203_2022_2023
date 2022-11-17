package com.yaps.petstore.catalog.domain;

import com.yaps.common.model.CheckException;
import com.yaps.common.model.DomainObject;

/**
 * This class represents a Product in the catalog of the YAPS company.
 * The catalog is divided into categories. Each one divided into products
 * and each product in items.
 */
public final class Product extends DomainObject {

    // ======================================
    // =             Attributes             =
    // ======================================
    private String name = "";
    private String description = "";
    private Category category;

    // ======================================
    // =            Constructors            =
    // ======================================


    public Product(final String id, final String name, final String description, final Category category) {
        super(id);
        if (name == null || description == null || category == null)
            throw new NullPointerException();
        this.name = name;
        this.description = description;
        this.category = category;
    }

    // ======================================
    // =           Business methods         =
    // ======================================

    public void checkData() throws CheckException {
        if (id.isEmpty())
            throw new CheckException("Invalid id");
        if (name.isEmpty())
            throw new CheckException("Invalid name");
        if (description.isEmpty())
            throw new CheckException("Invalid description");
        category.checkData();
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (name == null)
            throw new NullPointerException();
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        if (description == null)
            throw new NullPointerException();
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        if (category == null)
            throw new NullPointerException();
        this.category = category;
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("product id=").append(id)
            .append(", name=").append(name)
            .append("\n")
            .append("category=")
            .append(category.shortDisplay())
            .append("\n")
            .append("description=")
            .append(description);
        return buf.toString();
    }

    @Override
    public String shortDisplay() {
        return id + "\t" + name;
    }

}
