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
public class Payment {
    	private String pid; 
	private String SupID;	
	private double amount;
	private String pType; 
	private String dateOfPayment;
	private String supplierInvoiceNumber;

    
    public Payment() {
    }

    public Payment(String pid, String SupID, double amount, String pType, String dateOfPayment, String supplierInvoiceNumber) {
        this.pid = pid;
        this.SupID = SupID;
        this.amount = amount;
        this.pType = pType;
        this.dateOfPayment = dateOfPayment;
        this.supplierInvoiceNumber = supplierInvoiceNumber;
    }

    @Override
    public String toString() {
        return "Payment{" + "pid=" + getPid() + ", SupID=" + getSupID() + ", amount=" + getAmount() + ", pType=" + getpType() + ", dateOfPayment=" + getDateOfPayment() + ", supplierInvoiceNumber=" + getSupplierInvoiceNumber() + '}';
    }

    /**
     * @return the pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * @return the SupID
     */
    public String getSupID() {
        return SupID;
    }

    /**
     * @param SupID the SupID to set
     */
    public void setSupID(String SupID) {
        this.SupID = SupID;
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
     * @return the pType
     */
    public String getpType() {
        return pType;
    }

    /**
     * @param pType the pType to set
     */
    public void setpType(String pType) {
        this.pType = pType;
    }

    /**
     * @return the dateOfPayment
     */
    public String getDateOfPayment() {
        return dateOfPayment;
    }

    /**
     * @param dateOfPayment the dateOfPayment to set
     */
    public void setDateOfPayment(String dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    /**
     * @return the supplierInvoiceNumber
     */
    
    /**
     * @return the supplierInvoiceNumber
     */
    public String getSupplierInvoiceNumber() {
        return supplierInvoiceNumber;
    }

    /**
     * @param supplierInvoiceNumber the supplierInvoiceNumber to set
     */
    public void setSupplierInvoiceNumber(String supplierInvoiceNumber) {
        this.supplierInvoiceNumber = supplierInvoiceNumber;
    }

    
}