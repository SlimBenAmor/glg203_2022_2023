package com.yaps.petstore.domain.category;
import java.util.List;
import com.yaps.utils.model.DomainObject;
import com.yaps.petstore.domain.annotation.propertyMetaData;


/**
 * This class represents a Category in the catalog of the YAPS company.
 * The catalog is divided into categories. Each one divided into products
 * and each product in items.
 */
public final class Category extends DomainObject {

    @propertyMetaData(order = 2, columnName = "name")
    private String name;
    @propertyMetaData(order = 3, columnName = "description")
    private String description;

    public Category() {
    }

    public Category(final String id) {
        super(id);
    }

    public Category(final String id, final String name, final String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public Category(final List<Object> argList) {
        super((String) argList.get(0));
        this.name = (String) argList.get(1);
        this.description = (String) argList.get(2);
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
