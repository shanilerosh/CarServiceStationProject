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
import lk.r4enterprises.system.model.ReturnOutward;


/**
 *
 * @author shanil
 */
public class ReturnOutwardController {
    public boolean addItemDetail(ArrayList<ReturnOutward> itemdetails) throws ClassNotFoundException, SQLException{
        for(ReturnOutward temp:itemdetails){
            boolean isAdded=AddItem(temp);
            if(!isAdded){return false;}
        }
        return true;
    }
    

    public boolean AddItem(ReturnOutward detail) throws ClassNotFoundException, SQLException{
        PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO ReturnOutward (sreturnid,iid,pricePerReturn,returnQty) VALUES(?,?,?,?)");
        stm.setString(1,detail.getsReturnId());
        stm.setString(2,detail.getIid());
        stm.setDouble(3,detail.getPricePerReturn());
        stm.setDouble(4,detail.getReturnQty());
        return stm.executeUpdate()>0;
        
    }
}
