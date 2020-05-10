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
public class SupplierReturn {
    private String sReturnId;
    private String sid;
    private String returnDate;
    private String docId;
    private double totalAmount;
    private ArrayList<ReturnOutward> list;

    /**
     * @return the sReturnId
     */
    public String getsReturnId() {
        return sReturnId;
    }

    @Override
    public String toString() {
        return "SupplierReturn{" + "sReturnId=" + sReturnId + ", sid=" + sid + ", returnDate=" + returnDate + ", docId=" + docId + ", totalAmount=" + totalAmount + ", list=" + list + '}';
    }

    public SupplierReturn() {
    }

    public SupplierReturn(String sReturnId, String sid, String returnDate, String docId, double totalAmount, ArrayList<ReturnOutward> list) {
        this.sReturnId = sReturnId;
        this.sid = sid;
        this.returnDate = returnDate;
        this.docId = docId;
        this.totalAmount = totalAmount;
        this.list = list;
    }

    /**
     * @param sReturnId the sReturnId to set
     */
    public void setsReturnId(String sReturnId) {
        this.sReturnId = sReturnId;
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
     * @return the returnDate
     */
    public String getReturnDate() {
        return returnDate;
    }

    /**
     * @param returnDate the returnDate to set
     */
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * @return the docId
     */
    public String getDocId() {
        return docId;
    }

    /**
     * @param docId the docId to set
     */
    public void setDocId(String docId) {
        this.docId = docId;
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

    /**
     * @return the list
     */
    public ArrayList<ReturnOutward> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(ArrayList<ReturnOutward> list) {
        this.list = list;
    }

    /**
     * @return the cReturnId
     */
    
}
