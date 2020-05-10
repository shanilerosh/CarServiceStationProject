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
public class ReturnsTableModel {
    private int rowCount;
    private String itemModel;
    private double returningPrice;
    private double returnQuantity;
    private double totalAmount;

    public ReturnsTableModel(int rowCount, String itemModel, double returningPrice, double returnQuantity, double totalAmount) {
        this.rowCount = rowCount;
        this.itemModel = itemModel;
        this.returningPrice = returningPrice;
        this.returnQuantity = returnQuantity;
        this.totalAmount = totalAmount;
    }

    public ReturnsTableModel() {
    }

    @Override
    public String toString() {
        return "ReturnsTableModel{" + "rowCount=" + getRowCount() + ", itemModel=" + getItemModel() + ", returningPrice=" + getReturningPrice() + ", returnQuantity=" + getReturnQuantity() + ", totalAmount=" + getTotalAmount() + '}';
    }

    /**
     * @return the rowCount
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * @param rowCount the rowCount to set
     */
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
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
     * @return the returningPrice
     */
    public double getReturningPrice() {
        return returningPrice;
    }

    /**
     * @param returningPrice the returningPrice to set
     */
    public void setReturningPrice(double returningPrice) {
        this.returningPrice = returningPrice;
    }

    /**
     * @return the returnQuantity
     */
    public double getReturnQuantity() {
        return returnQuantity;
    }

    /**
     * @param returnQuantity the returnQuantity to set
     */
    public void setReturnQuantity(double returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    /**
     * @return the totalAmount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    
}
