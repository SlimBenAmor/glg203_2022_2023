package com.yaps.petstore.domain.item;

import com.yaps.petstore.domain.product.Product;
import com.yaps.utils.model.CheckException;
import com.yaps.utils.model.DomainObject;


/**
 * This class represents a Item in the catalog of the YAPS company.
 * The catalog is divided into categories. Each one divided into products
 * and each product in items.
 */
public final class Item extends DomainObject {

    private String name;
    private double unitCost;
    private Product product;
    

    public Item() {
    }

    public Item(final String id) {
        super(id);
    }

    public Item(final String id, final String name, final double unitCost, Product product) {
        super(id);
        this.name = name;
        this.unitCost = unitCost;
        this.product = product;
    }

    public void checkData() throws CheckException {
        if (id == null || "".equals(id))
            throw new CheckException("Invalid id");
        if (name == null || "".equals(name))
            throw new CheckException("Invalid name");
        if (unitCost<0)
            throw new CheckException("Invalid unit cost");
        if (product == null || "".equals(product.getId()))
            throw new CheckException("Invalid product");
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(final double unitCost) {
        this.unitCost = unitCost;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("item id=").append(id);
        buf.append(", name=").append(name);
        buf.append(", product=").append(product.shortDisplay());
        buf.append(", unit cost=").append(unitCost);
        return buf.toString();
    }

    @Override
    public String shortDisplay() {
        return id + "\t" + name;
    }
}
