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
public class Car {
    private String crid;
    private String registrationNumber;
    private String modelNumber;
    private String cid;

    /**
     * @return the crid
     */
    public String getCrid() {
        return crid;
    }

    /**
     * @param crid the crid to set
     */
    public void setCrid(String crid) {
        this.crid = crid;
    }

    /**
     * @return the registrationNumber
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * @param registrationNumber the registrationNumber to set
     */
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    /**
     * @return the modelNumber
     */
    public String getModelNumber() {
        return modelNumber;
    }

    /**
     * @param modelNumber the modelNumber to set
     */
    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    
    public Car() {
    }

    public Car(String crid, String registrationNumber, String modelNumber, String cid) {
        this.crid = crid;
        this.registrationNumber = registrationNumber;
        this.modelNumber = modelNumber;
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "Car{" + "crid=" + crid + ", registrationNumber=" + registrationNumber + ", modelNumber=" + modelNumber + ", cid=" + cid + '}';
    }

    /**
     * @return the cid
     */
    public String getCid() {
        return cid;
    }

    /**
     * @param cid the cid to set
     */
    public void setCid(String cid) {
        this.cid = cid;
    }
    
    
    
}
