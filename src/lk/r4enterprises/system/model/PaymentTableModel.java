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
public class PaymentTableModel {
        private String pid; 
	private String SupplierName;
        private String paymentType;
        private String dateOfPayment;
	private double amount;
	private String supplierInvoiceNumber;

    public PaymentTableModel(String pid, String SupplierName, String paymentType, String dateOfPayment, double amount, String supplierInvoiceNumber) {
        this.pid = pid;
        this.SupplierName = SupplierName;
        this.paymentType = paymentType;
        this.dateOfPayment = dateOfPayment;
        this.amount = amount;
        this.supplierInvoiceNumber = supplierInvoiceNumber;
    }

    public PaymentTableModel() {
    }

    @Override
    public String toString() {
        return "PaymentTableModel{" + "pid=" + pid + ", SupplierName=" + SupplierName + ", paymentType=" + paymentType + ", dateOfPayment=" + dateOfPayment + ", amount=" + amount + ", supplierInvoiceNumber=" + supplierInvoiceNumber + '}';
    }


    /**
     * @return the pid
     */
    public String getSid() {
        return getPid();
    }

    /**
     * @param pid the pid to set
     */
    public void setSid(String pid) {
        this.setPid(pid);
    }

    /**
     * @return the SupplierName
     */
    public String getSupplierName() {
        return SupplierName;
    }

    /**
     * @param SupplierName the SupplierName to set
     */
    public void setSupplierName(String SupplierName) {
        this.SupplierName = SupplierName;
    }

    /**
     * @return the paymentType
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
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
     * @return the supplierInvoiceNumber
     */
    
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