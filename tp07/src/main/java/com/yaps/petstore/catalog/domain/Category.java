package com.yaps.petstore.catalog.domain;

import com.yaps.common.model.CheckException;
import com.yaps.common.model.DomainObject;

/**
 * This class represents a Category in the catalog of the YAPS company.
 * The catalog is divided into categories. Each one divided into products
 * and each product in items.
 */
public final class Category extends DomainObject {

    private String name = "";
    private String description = "";

    public Category() {
    }

    public Category(final String id) {
        super(id);
    }

    public Category(final String id, final String name, final String description) {
        super(id);
        if (name == null || description == null)
            throw new NullPointerException();
        this.name = name;
        this.description = description;
    }

    public void checkData() throws CheckException {
        if (id == null || "".equals(id))
            throw new CheckException("Invalid name");
        if (name == null || "".equals(name))
            throw new CheckException("Invalid name");
        if (description == null || "".equals(description))
            throw new CheckException("Invalid description");
    }

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
        if (description == null) {
            throw new NullPointerException("description should not be null");
        }
        this.description = description;
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("category id=").append(id);
        buf.append(", name=").append(name);
        buf.append("\ndescription=").append(description);
        return buf.toString();
    }

    @Override
    public String shortDisplay() {
        return id + "\t" + name;
    }
}
