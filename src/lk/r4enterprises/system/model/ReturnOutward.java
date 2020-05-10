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
public class ReturnOutward {
    private String sReturnId;
    private String iid;
    private double pricePerReturn;
    private double returnQty;

    public ReturnOutward(String sReturnId, String iid, double pricePerReturn, double returnQty) {
        this.sReturnId = sReturnId;
        this.iid = iid;
        this.pricePerReturn = pricePerReturn;
        this.returnQty = returnQty;
    }

    public ReturnOutward() {
    }

    @Override
    public String toString() {
        return "ReturnOutward{" + "sReturnId=" + getsReturnId() + ", iid=" + getIid() + ", pricePerReturn=" + getPricePerReturn() + ", returnQty=" + getReturnQty() + '}';
    }

    /**
     * @return the sReturnId
     */
    public String getsReturnId() {
        return sReturnId;
    }

    /**
     * @param sReturnId the sReturnId to set
     */
    public void setsReturnId(String sReturnId) {
        this.sReturnId = sReturnId;
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
     * @return the pricePerReturn
     */
    public double getPricePerReturn() {
        return pricePerReturn;
    }

    /**
     * @param pricePerReturn the pricePerReturn to set
     */
    public void setPricePerReturn(double pricePerReturn) {
        this.pricePerReturn = pricePerReturn;
    }

    /**
     * @return the returnQty
     */
    public double getReturnQty() {
        return returnQty;
    }

    /**
     * @param returnQty the returnQty to set
     */
    public void setReturnQty(double returnQty) {
        this.returnQty = returnQty;
    }

    
}
