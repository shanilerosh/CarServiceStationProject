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
public class CreditNote {
    private String customerName;
    private double amount;
    private String narration;
    private String cnID;
    private String cid;
    private String dateOfTransaction;

    public CreditNote(String customerName, double amount, String narration, String cnID, String cid,String dateOfTransaction) {
        this.customerName = customerName;
        this.amount = amount;
        this.narration = narration;
        this.cnID = cnID;
        this.cid = cid;
        this.dateOfTransaction = dateOfTransaction;
    }

    

    public CreditNote() {
    }

    @Override
    public String toString() {
        return "CreditNote{" + "customerName=" + customerName + ", amount=" + amount + ", narration=" + narration + ", cnID=" + cnID + ", cid=" + cid + '}';
    }

    
    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(String dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
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

    /**
     * @return the narration
     */
    public String getNarration() {
        return narration;
    }

    /**
     * @param narration the narration to set
     */
    public void setNarration(String narration) {
        this.narration = narration;
    }

    /**
     * @return the cnID
     */
    public String getCnID() {
        return cnID;
    }

    /**
     * @param cnID the cnID to set
     */
    public void setCnID(String cnID) {
        this.cnID = cnID;
    }

    /**
     * @return the cid
     */
    public String getCid() {
        return cid;
    }

    /**
     * @param cid the cid to set
     */
    public void setCid(String cid) {
        this.cid = cid;
    }
    
    
}
