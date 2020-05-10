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
import lk.r4enterprises.system.model.CustomerReturn;

/**
 *
 * @author shanil
 */
public class CustomerReturnsConroller {
    
    public boolean addCustomerReturns(CustomerReturn customerReturn ) throws ClassNotFoundException, SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="INSERT INTO CustomerReturn values(?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, customerReturn.getcReturnId());
        ps.setString(2, customerReturn.getCid());
        ps.setString(3, customerReturn.getReturnDate());
        ps.setString(4, customerReturn.getDocId());
        ps.setDouble(5, customerReturn.getTotalAmount());
        
        boolean isSave=ps.executeUpdate()>0;
        
        if(isSave){
            return new ReturnInwardController().addItemDetail(customerReturn.getList());
        }
        return false;

    }
    
}
