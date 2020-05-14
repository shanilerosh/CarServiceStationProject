/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.model;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;



/**
 *
 * @author shanil
 */
public class ReportTableModel {
    private String oid;
    private String customerName;
    private String cid;
    private String dateOfTransaction;
    private String amount;
    private String totalOrder;
    private String totalReceipts;
    private String totalReturns;
    private String totalCreditNotes;
    private String discount;
    private int quantity;
    private String sid;
    private String supplierName;
    private String creditNoteNumber;
    private String DebitNoteNumber;
    private String profitPerItem;
    private String customerReturnId;
    private String SupplierReturnId;
    private String GRNnumber;
    private String itemModel;
    private String itemName;
    private String itemPrice;
    private String iid;
    private String itemCategory;
    private String customerMobile;
    private String cost;

    public ReportTableModel(String dateOfTransaction, int quantity) {
        this.dateOfTransaction = dateOfTransaction;
        this.quantity = quantity;
    }

    public ReportTableModel(String dateOfTransaction, String amount) {
        this.dateOfTransaction = dateOfTransaction;
        this.amount = amount;
    }
    
    public ReportTableModel(String customerName, String cid, String amount, String totalOrder, String totalReceipts, String totalReturns, String totalCreditNotes) {
        this.customerName = customerName;
        this.cid = cid;
        this.amount = amount;
        this.totalOrder = totalOrder;
        this.totalReceipts = totalReceipts;
        this.totalReturns = totalReturns;
        this.totalCreditNotes = totalCreditNotes;
    }
    
    
        
    
    public ReportTableModel(String oid, String customerName, String dateOfTransaction, String amount, String discount) {
        this.oid = oid;
        this.customerName = customerName;
        this.dateOfTransaction = dateOfTransaction;
        this.amount = amount;
        this.discount = discount;
    }

    public ReportTableModel(int quantity, String itemModel, String itemName, String itemPrice, String iid, String amount) {
        this.quantity = quantity;
        this.itemModel = itemModel;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.iid = iid;
        this.amount = amount;
    }
    
    public ReportTableModel(int quantity, String itemModel, String itemName,String iid, String amount) {
        this.quantity = quantity;
        this.itemModel = itemModel;
        this.itemName = itemName;
        this.iid = iid;
        this.amount = amount;
    }

    public ReportTableModel(String customerName, String customerMobile, String cid, String amount) {
        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.cid = cid;
        this.amount = amount;
    }

    public ReportTableModel(String amount, int quantity, String sid, String supplierName) {
        this.amount = amount;
        this.quantity = quantity;
        this.sid = sid;
        this.supplierName = supplierName;
    }

        public ReportTableModel(String oid, String customerName, String dateOfTransaction, String amount, String totalOrder, String cost) {
        this.oid = oid;
        this.customerName = customerName;
        this.dateOfTransaction = dateOfTransaction;
        this.amount = amount;
        this.totalOrder = totalOrder;
        this.cost = cost;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
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
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
     * @return the dateOfTransaction
     */
    public String getDateOfTransaction() {
        return dateOfTransaction;
    }

    /**
     * @param dateOfTransaction the dateOfTransaction to set
     */
    public void setDateOfTransaction(String dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return the discount
     */
    public String getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(String discount) {
        this.discount = discount;
    }

    /**
     * @return the quantity
     */
    

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
     * @return the supplierName
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * @return the creditNoteNumber
     */
    public String getCreditNoteNumber() {
        return creditNoteNumber;
    }

    /**
     * @param creditNoteNumber the creditNoteNumber to set
     */
    public void setCreditNoteNumber(String creditNoteNumber) {
        this.creditNoteNumber = creditNoteNumber;
    }

    /**
     * @return the DebitNoteNumber
     */
    public String getDebitNoteNumber() {
        return DebitNoteNumber;
    }

    /**
     * @param DebitNoteNumber the DebitNoteNumber to set
     */
    public void setDebitNoteNumber(String DebitNoteNumber) {
        this.DebitNoteNumber = DebitNoteNumber;
    }

    /**
     * @return the profitPerItem
     */
    public String getProfitPerItem() {
        return profitPerItem;
    }

    /**
     * @param profitPerItem the profitPerItem to set
     */
    public void setProfitPerItem(String profitPerItem) {
        this.profitPerItem = profitPerItem;
    }

    /**
     * @return the customerReturnId
     */
    public String getCustomerReturnId() {
        return customerReturnId;
    }

    /**
     * @param customerReturnId the customerReturnId to set
     */
    public void setCustomerReturnId(String customerReturnId) {
        this.customerReturnId = customerReturnId;
    }

    /**
     * @return the SupplierReturnId
     */
    public String getSupplierReturnId() {
        return SupplierReturnId;
    }

    /**
     * @param SupplierReturnId the SupplierReturnId to set
     */
    public void setSupplierReturnId(String SupplierReturnId) {
        this.SupplierReturnId = SupplierReturnId;
    }

    /**
     * @return the GRNnumber
     */
    public String getGRNnumber() {
        return GRNnumber;
    }

    /**
     * @param GRNnumber the GRNnumber to set
     */
    public void setGRNnumber(String GRNnumber) {
        this.GRNnumber = GRNnumber;
    }

    /**
     * @return the itemModel
     */
    public String getItemModel() {
        return itemModel;
    }

    /**
     * @param itemModel the itemModel to set
     */
    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the itemPrice
     */
    public String getItemPrice() {
        return itemPrice;
    }

    /**
     * @param itemPrice the itemPrice to set
     */
    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
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
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the itemCategory
     */
    public String getItemCategory() {
        return itemCategory;
    }

    /**
     * @param itemCategory the itemCategory to set
     */
    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    /**
     * @return the customerMobile
     */
    public String getCustomerMobile() {
        return customerMobile;
    }

    /**
     * @param customerMobile the customerMobile to set
     */
    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    /**
     * @return the totalOrder
     */
    public String getTotalOrder() {
        return totalOrder;
    }

    /**
     * @param totalOrder the totalOrder to set
     */
    public void setTotalOrder(String totalOrder) {
        this.totalOrder = totalOrder;
    }

    /**
     * @return the totalReceipts
     */
    public String getTotalReceipts() {
        return totalReceipts;
    }

    /**
     * @param totalReceipts the totalReceipts to set
     */
    public void setTotalReceipts(String totalReceipts) {
        this.totalReceipts = totalReceipts;
    }

    /**
     * @return the totalReturns
     */
    public String getTotalReturns() {
        return totalReturns;
    }

    /**
     * @param totalReturns the totalReturns to set
     */
    public void setTotalReturns(String totalReturns) {
        this.totalReturns = totalReturns;
    }

    /**
     * @return the totalCreditNotes
     */
    public String getTotalCreditNotes() {
        return totalCreditNotes;
    }

    /**
     * @param totalCreditNotes the totalCreditNotes to set
     */
    public void setTotalCreditNotes(String totalCreditNotes) {
        this.totalCreditNotes = totalCreditNotes;
    }
    
    
    
    
    
}
