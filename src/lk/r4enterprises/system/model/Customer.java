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
public class Customer {
    private String cid;
    private String name;
    private String address;
    private String mobileNumber;    
    private String activeInactive;    

    
    public Customer() {
    }

    public Customer(String cid, String name, String address, String mobileNumber, String activeInactive) {
        this.cid = cid;
        this.name = name;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.activeInactive = activeInactive;
    }

    public String getActiveInactive() {
        return activeInactive;
    }

    public void setActiveInactive(String activeInactive) {
        this.activeInactive = activeInactive;
    }
    
    

    @Override
    public String toString() {
        return "Customer{" + "cid=" + cid + ", name=" + name + ", address=" + address + ", mobileNumber=" + mobileNumber + '}';
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

    /**
     * @return the firstName
     */
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * @param mobileNumber the mobileNumber to set
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
    
    
    
}
