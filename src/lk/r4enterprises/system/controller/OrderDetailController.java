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
import lk.r4enterprises.system.model.Item;
import lk.r4enterprises.system.model.OrderDetail;
import lk.r4enterprises.system.model.ReportTableModel;

/**
 *
 * @author shanil
 */
public class OrderDetailController {
    public boolean addItemDetail(ArrayList<OrderDetail> itemdetails) throws ClassNotFoundException, SQLException{
        for(OrderDetail temp:itemdetails){
            boolean isAdded=AddItem(temp);
            if(!isAdded){return false;}
        }
        return true;
    }
    

    public boolean AddItem(OrderDetail detail) throws ClassNotFoundException, SQLException{
        PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO OrderDetail VALUES(?,?,?,?)");
        stm.setObject(1,detail.getOid());
        stm.setObject(2,detail.getIid());
        stm.setObject(3,detail.getQty());
        stm.setObject(4,detail.getUnitPrice());
        
        return stm.executeUpdate()>0;
        
    }
    

    public ObservableList<ReportTableModel> getItemSaleCountByDates(String datePickerFrom, String datePickerTo) throws SQLException, ClassNotFoundException {
        ObservableList<ReportTableModel> list=FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT OrderDetail.iid,SUM(OrderDetail.quantity),OrderDetail.itemPrice FROM OrderDetail INNER JOIN Order_ ON OrderDetail.oid=Order_.oid WHERE Order_.dateOfOrder BETWEEN ? AND ? GROUP BY OrderDetail.iid;");
        ps.setString(1, datePickerFrom);
        ps.setString(2, datePickerTo);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            Item item=new ItemController().getItemFromIid(rs.getString(1));
            if(item==null){
                return null;
            }
            list.add(
                    new ReportTableModel(
                    (int)rs.getDouble(2), 
                    item.getModel(),
                    item.getName(),
                    String.format("%,.2f", item.getUnitPrice()),
                    item.getIid(),
                    String.format("%,.2f", rs.getDouble(2)*item.getUnitPrice())
                    ));
        }
        return list;
    }
    
}
