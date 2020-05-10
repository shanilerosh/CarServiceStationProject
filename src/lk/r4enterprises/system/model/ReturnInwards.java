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
public class ReturnInwards {
    private String cReturnId;
    private String iid;
    private double pricePerReturn;
    private double returnQty;

    public ReturnInwards(String cReturnId, String iid, double pricePerReturn, double returnQty) {
        this.cReturnId = cReturnId;
        this.iid = iid;
        this.pricePerReturn = pricePerReturn;
        this.returnQty = returnQty;
    }

    public ReturnInwards() {
    }

    @Override
    public String toString() {
        return "ReturnInwards{" + "cReturnId=" + getcReturnId() + ", iid=" + getIid() + ", pricePerReturn=" + getPricePerReturn() + ", returnQty=" + getReturnQty() + '}';
    }

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
