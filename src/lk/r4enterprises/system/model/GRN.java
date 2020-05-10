/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.model;

import java.util.ArrayList;

/**
 *
 * @author shanil
 */
public class GRN {
    private String grn;
    private String grnDate;
    private String supplierInvoiceNumber;
    private String supplierInvoiceDate;
    private String sid;
    private double totalGrnAmount;
    private ArrayList<SupplierDetail> list;
    
    /**
     * @return the grn
     */
    public String getGrn() {
        return grn;
    }

    public GRN(String grn, String grnDate, String supplierInvoiceNumber, String supplierInvoiceDate, String sid, double totalGrnAmount, ArrayList<SupplierDetail> list) {
        this.grn = grn;
        this.grnDate = grnDate;
        this.supplierInvoiceNumber = supplierInvoiceNumber;
        this.supplierInvoiceDate = supplierInvoiceDate;
        this.sid = sid;
        this.totalGrnAmount = totalGrnAmount;
        this.list = list;
    }

    /**
     * @param grn the grn to set
     */
    public void setGrn(String grn) {
        this.grn = grn;
    }

    /**
     * @return the grnDate
     */
    public String getGrnDate() {
        return grnDate;
    }

    /**
     * @param grnDate the grnDate to set
     */
    public void setGrnDate(String grnDate) {
        this.grnDate = grnDate;
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

    /**
     * @return the supplierInvoiceDate
     */
    public String getSupplierInvoiceDate() {
        return supplierInvoiceDate;
    }

    /**
     * @param supplierInvoiceDate the supplierInvoiceDate to set
     */
    public void setSupplierInvoiceDate(String supplierInvoiceDate) {
        this.supplierInvoiceDate = supplierInvoiceDate;
    }

    /**
     * @return the supplierName
     */
    
    /**
     * @return the totalGrnAmount
     */
    public double getTotalGrnAmount() {
        return totalGrnAmount;
    }

    /**
     * @param totalGrnAmount the totalGrnAmount to set
     */
    public void setTotalGrnAmount(double totalGrnAmount) {
        this.totalGrnAmount = totalGrnAmount;
    }

    /**
     * @return the list
     */
    public ArrayList<SupplierDetail> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(ArrayList<SupplierDetail> list) {
        this.list = list;
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



