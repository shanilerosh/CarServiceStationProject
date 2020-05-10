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
public class DebitNote {
    private String supplierName;
    private double amount;
    private String narration;
    private String dnId;
    private String sid;
    private String dateOfTransaction;
    
    
    
    public DebitNote() {
    }

    public DebitNote(String supplierName, double amount, String narration, String dnId, String sid,String dateOfTransaction) {
        this.supplierName = supplierName;
        this.amount = amount;
        this.narration = narration;
        this.dnId = dnId;
        this.sid = sid;
        this.dateOfTransaction=dateOfTransaction;
    }

    @Override
    public String toString() {
        return "DebitNote{" + "supplierName=" + supplierName + ", amount=" + amount + ", narration=" + narration + ", dnId=" + dnId + ", sid=" + sid + '}';
    }

    public String getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(String dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    
    

    

    /**
     * @return the supplierName
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
     * @return the dnId
     */
    public String getDnId() {
        return dnId;
    }

    /**
     * @param dnId the dnId to set
     */
    public void setDnId(String dnId) {
        this.dnId = dnId;
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
    
    
}
