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
public class ReceiptTableModel {
        private String rid; 
	private String custName;
        private String receiptType;
        private String dateOfReceipt;
	private double amount;
	private String narration;
	

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
     * @return the custName
     */
    public String getCustName() {
        return custName;
    }

    /**
     * @param custName the custName to set
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * @return the receiptType
     */
    public String getReceiptType() {
        return receiptType;
    }

    /**
     * @param receiptType the receiptType to set
     */
    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
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
  

    
    public ReceiptTableModel() {
    }

    public ReceiptTableModel(String rid, String custName, String receiptType, String dateOfReceipt, double amount, String narration) {
        this.rid = rid;
        this.custName = custName;
        this.receiptType = receiptType;
        this.dateOfReceipt = dateOfReceipt;
        this.amount = amount;
        this.narration = narration;
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

    @Override
    public String toString() {
        return "ReceiptTableModel{" + "rid=" + rid + ", custName=" + custName + ", receiptType=" + receiptType + ", dateOfReceipt=" + dateOfReceipt + ", amount=" + amount + ", narration=" + narration + '}';
    }

    
    
        
    
}
