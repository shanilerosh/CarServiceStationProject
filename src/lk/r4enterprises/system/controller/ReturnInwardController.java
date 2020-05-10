/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.ReportTableModel;
import lk.r4enterprises.system.model.ReturnInwards;


/**
 *
 * @author shanil
 */
public class ReturnInwardController {
    public boolean addItemDetail(ArrayList<ReturnInwards> itemdetails) throws ClassNotFoundException, SQLException{
        for(ReturnInwards temp:itemdetails){
            boolean isAdded=AddItem(temp);
            if(!isAdded){return false;}
        }
        return true;
    }
    

    public boolean AddItem(ReturnInwards detail) throws ClassNotFoundException, SQLException{
        PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO ReturnInward (creturnid,iid,pricePerReturn,returnQty) VALUES(?,?,?,?)");
        stm.setString(1,detail.getcReturnId());
        stm.setString(2,detail.getIid());
        stm.setDouble(3,detail.getPricePerReturn());
        stm.setDouble(4,detail.getReturnQty());
        return stm.executeUpdate()>0;
        
    }

    ObservableList<ReportTableModel> getReturnCountByDates(String datePickerFrom, String datePickerTo) throws ClassNotFoundException, SQLException {
            ObservableList<ReportTableModel> list = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT CustomerReturn.returnDate,COUNT(ReturnInward.returnQty) FROM CustomerReturn INNER JOIN ReturnInward ON ReturnInward.creturnid=CustomerReturn.creturnid WHERE CustomerReturn.returnDate BETWEEN ? AND ? GROUP BY CustomerReturn.returnDate");
        ps.setString(1, datePickerFrom);
        ps.setString(2, datePickerTo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new ReportTableModel(
                    rs.getString(1),
                    rs.getInt(2)
            ));
        }
        return list;
    }
}
