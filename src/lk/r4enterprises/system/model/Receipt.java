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
public class Receipt {
    	private String rid; 
	private String custID;	
	private double amount;
	private String rType; 
	private String dateOfReceipt;
	private String narration;

    
    public Receipt() {
    }

    public Receipt(String rid, String custID, double amount, String rType, String dateOfReceipt, String narration) {
        this.rid = rid;
        this.custID = custID;
        this.amount = amount;
        this.rType = rType;
        this.dateOfReceipt = dateOfReceipt;
        this.narration = narration;
    }

    @Override
    public String toString() {
        return "Receipt{" + "rid=" + rid + ", custID=" + custID + ", amount=" + amount + ", rType=" + rType + ", dateOfReceipt=" + dateOfReceipt + ", narration=" + narration + '}';
    }

    

    /**
     * @return the rid
     */
    public String getRid() {
        return rid;
    }

    /**
     * @param rid the rid to set
     */
    public void setRid(String rid) {
        this.rid = rid;
    }

    /**
     * @return the custID
     */
    public String getCid() {
        return getCustID();
    }

    /**
     * @param custID the custID to set
     */
    public void setCid(String custID) {
        this.setCustID(custID);
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
     * @return the rType
     */
    public String getrType() {
        return rType;
    }

    /**
     * @param rType the rType to set
     */
    public void setrType(String rType) {
        this.rType = rType;
    }

    /**
     * @return the dateOfReceipt
     */
    public String getDateOfReceipt() {
        return dateOfReceipt;
    }

    /**
     * @param dateOfReceipt the dateOfReceipt to set
     */
    public void setDateOfReceipt(String dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
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
     * @return the custID
     */
    public String getCustID() {
        return custID;
    }

    /**
     * @param custID the custID to set
     */
    public void setCustID(String custID) {
        this.custID = custID;
    }

    
        
    
}
