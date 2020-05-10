/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.SupplierDetail;

/**
 *
 * @author shanil
 */
public class SupplierDetailController {
   private boolean addItem(SupplierDetail supplierDetail) throws SQLException, ClassNotFoundException{
       PreparedStatement ps = DBConnection.getInstance().getConnection().
               prepareStatement("INSERT INTO SupplierDetail(GRN,iid,quantity,amount) values(?,?,?,?)");
       ps.setString(1, supplierDetail.getGRN());
       ps.setString(2, supplierDetail.getIid());
       ps.setDouble(3, supplierDetail.getQuantity());
       ps.setDouble(4, supplierDetail.getAmount());
       return ps.executeUpdate()>0;
   
   }
   
   
   public boolean addItemDetail(ArrayList<SupplierDetail> supplierDetails) throws ClassNotFoundException, SQLException{
        for(SupplierDetail sd:supplierDetails){
            boolean isAdded=addItem(sd);
            if(!isAdded){return false;}
        }
        return true;
    }
    
}
