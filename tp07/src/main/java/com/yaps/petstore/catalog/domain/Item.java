package com.yaps.petstore.catalog.domain;


import com.yaps.common.model.CheckException;
import com.yaps.common.model.DomainObject;

/**
 * This class represents an Item in the catalog of the YAPS company.
 * The catalog is divided into categories. Each one divided into products
 * and each product in items.
 */
public final class Item extends DomainObject {

    // ======================================
    // =             Attributes             =
    // ======================================
    private String name = "";
    private double unitCost = 0.0;
    private Product product;
    private String imagePath = "";

    // ======================================
    // =            Constructors            =
    // ======================================

    public Item(final String id, final String name, final double unitCost, String imagePath, final Product product) {
        super(id);
        if (name == null || imagePath == null || product == null)
            throw new NullPointerException();
        this.name = name;
        this.unitCost = unitCost;
        this.imagePath = imagePath;
        this.product = product;
    }

    // ======================================
    // =           Business methods         =
    // ======================================
    public void checkData() throws CheckException {
        if (id.equals(""))
            throw new CheckException("Invalid id");        
        if (name.equals(""))
            throw new CheckException("Invalid name");
        if (unitCost <= 0)
            throw new CheckException("Invalid unit cost");
        product.checkData();
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

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(final double unitCost) {
        this.unitCost = unitCost;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        if (product ==  null)
            throw new NullPointerException();
        this.product = product;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        if (imagePath ==  null)
            throw new NullPointerException();
        this.imagePath = imagePath;
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("item id=").append(id)
            .append(", name=").append(name)
            .append(", image path=").append(imagePath)
            .append(", product=").append(product.shortDisplay())
            .append(", unit cost=").append(unitCost);
        return buf.toString();
    }

    @Override
    public String shortDisplay() {
        return id + "\t" + name;
    }

}
