/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.model;

/**
 *
 * @author shanil
 */
public class Item {
    private String iid;
    private String name;
    private String category;
    private double unitPrice;
    private String model;
    private int quantityOnHand;

    public Item(String iid, String name, String category, double unitPrice, String model, int quantityOnHand) {
        this.iid = iid;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.model = model;
        this.quantityOnHand = quantityOnHand;
    }

    public Item() {
    }

    @Override
    public String toString() {
        return "Item{" + "iid=" + iid + ", name=" + name + ", category=" + category + ", unitPrice=" + unitPrice + ", model=" + model + ", quantityOnHand=" + quantityOnHand + '}';
    }

    /**
     * @return the iid
     */
    public String getIid() {
        return iid;
    }

    /**
     * @param iid the iid to set
     */
    public void setIid(String iid) {
        this.iid = iid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the unitPrice
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the quantityOnHand
     */
    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    /**
     * @param quantityOnHand the quantityOnHand to set
     */
    public void setQuantityOnHand(int quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }
    
    
    
}
