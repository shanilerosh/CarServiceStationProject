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
public class SupplierDetail {
    private String GRN;
    private String sid;
    private String iid;
    private double quantity;
    private double amount;

    public SupplierDetail(String GRN, String sid, String iid, double quantity, double amount) {
        this.GRN = GRN;
        this.sid = sid;
        this.iid = iid;
        this.quantity = quantity;
        this.amount = amount;
    }

    public SupplierDetail() {
    }

    @Override
    public String toString() {
        return "SupplierDetail{" + "GRN=" + GRN + ", sid=" + sid + ", iid=" + iid + ", quantity=" + quantity + ", amount=" + amount + '}';
    }


    public String getGRN() {
        return GRN;
    }

    /**
     * @param GRN the GRN to set
     */
    public void setGRN(String GRN) {
        this.GRN = GRN;
    }

    /**
     * @return the sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid(String sid) {
        this.sid = sid;
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
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    
}
