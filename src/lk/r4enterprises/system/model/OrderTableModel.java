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
public class OrderTableModel {
    private int counter;
    private String itemModel;
    private String itemName;
    private double unitPrice;
    private int quantity;
    private double itemTotal;

    /**
     * @return the counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * @param counter the counter to set
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * @return the itemModel
     */
    public String getItemModel() {
        return itemModel;
    }

    /**
     * @param itemModel the itemModel to set
     */
    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
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
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the itemTotal
     */
    public double getItemTotal() {
        return itemTotal;
    }

    /**
     * @param itemTotal the itemTotal to set
     */
    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }

    public OrderTableModel(int counter, String itemModel, String itemName, double unitPrice, int quantity, double itemTotal) {
        this.counter = counter;
        this.itemModel = itemModel;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.itemTotal = itemTotal;
    }

    public OrderTableModel() {
    }

    @Override
    public String toString() {
        return "OrderTableModel{" + "counter=" + counter + ", itemModel=" + itemModel + ", itemName=" + itemName + ", unitPrice=" + unitPrice + ", quantity=" + quantity + ", itemTotal=" + itemTotal + '}';
    }
    
    
    
}
