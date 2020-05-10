/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.SupplierReturn;

/**
 *
 * @author shanil
 */
public class SupplierReturnsController {
    
    public boolean addSupplierReturns(SupplierReturn supplierReturn) throws ClassNotFoundException, SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="INSERT INTO SupplierReturn values(?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, supplierReturn.getsReturnId());
        ps.setString(2, supplierReturn.getSid());
        ps.setString(3, supplierReturn.getReturnDate());
        ps.setString(4, supplierReturn.getDocId());
        ps.setDouble(5, supplierReturn.getTotalAmount());
        
        boolean isSave=ps.executeUpdate()>0;
        
        if(isSave){
            return new ReturnOutwardController().addItemDetail(supplierReturn.getList());
        }
        return false;

    }
    
}
