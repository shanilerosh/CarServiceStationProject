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
public class CustomerReturn {
    private String cReturnId;
    private String cid;
    private String returnDate;
    private String docId;
    private double totalAmount;
    private ArrayList<ReturnInwards> list;

    /**
     * @return the cReturnId
     */
    public String getcReturnId() {
        return cReturnId;
    }

    /**
     * @param cReturnId the cReturnId to set
     */
    public void setcReturnId(String cReturnId) {
        this.cReturnId = cReturnId;
    }

    /**
     * @return the cid
     */
    public String getCid() {
        return cid;
    }

    public CustomerReturn(String cReturnId, String cid, String returnDate, String docId, double totalAmount, ArrayList<ReturnInwards> list) {
        this.cReturnId = cReturnId;
        this.cid = cid;
        this.returnDate = returnDate;
        this.docId = docId;
        this.totalAmount = totalAmount;
        this.list = list;
    }

    @Override
    public String toString() {
        return "CustomerReturn{" + "cReturnId=" + cReturnId + ", cid=" + cid + ", returnDate=" + returnDate + ", docId=" + docId + ", totalAmount=" + totalAmount + ", list=" + list + '}';
    }

    

    public CustomerReturn() {
    }

    /**
     * @param cid the cid to set
     */
    public void setCid(String cid) {
        this.cid = cid;
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
    public ArrayList<ReturnInwards> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(ArrayList<ReturnInwards> list) {
        this.list = list;
    }
    
    
    
    
}
