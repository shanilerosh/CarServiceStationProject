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
public class Order {
    private String oid;
    private String date;
    private double discount;
    private double Amount;
    private String customerId;
    private String carId;
    private ArrayList<OrderDetail> list;

    
    
    

    public Order() {
    }

    public Order(String oid, String date, double discount, double Amount, String customerId, String carId, ArrayList<OrderDetail> list) {
        this.oid = oid;
        this.date = date;
        this.discount = discount;
        this.Amount = Amount;
        this.customerId = customerId;
        this.carId = carId;
        this.list = list;
    }

    @Override
    public String toString() {
        return "Order{" + "oid=" + oid + ", date=" + date + ", discount=" + discount + ", Amount=" + Amount + ", customerId=" + customerId + ", carId=" + carId + ", list=" + list + '}';
    }

    
    /**
     * @return the oid
     */
    public String getOid() {
        return oid;
    }

    /**
     * @param oid the oid to set
     */
    public void setOid(String oid) {
        this.oid = oid;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the list
     */
    public ArrayList<OrderDetail> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(ArrayList<OrderDetail> list) {
        this.list = list;
    }   

    /**
     * @return the discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * @return the Amount
     */
    public double getAmount() {
        return Amount;
    }

    /**
     * @param Amount the Amount to set
     */
    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    /**
     * @return the carId
     */
    public String getCarId() {
        return carId;
    }

    /**
     * @param carId the carId to set
     */
    public void setCarId(String carId) {
        this.carId = carId;
    }
    
    
}
