/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.Car;

/**
 *
 * @author shanil
 */
public class CarController {
    public Car getCarFromRegistration(String name) throws SQLException, ClassNotFoundException{
        Connection con=DBConnection.getInstance().getConnection();
        Car foundCar=null;
        String sql="SELECT * FROM Car where registrationNumber=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            foundCar=new Car(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4));
        }
        return foundCar;
    }
    
    
    public Car getCarFromId(String id) throws SQLException, ClassNotFoundException{
        Connection con=DBConnection.getInstance().getConnection();
        Car foundCar=null;
        String sql="SELECT * FROM Car where crid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            foundCar=new Car(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4));
        }
        return foundCar;
    }
    
    
    public List<String> getAllCarRegNumbers() throws
            ClassNotFoundException, SQLException{
        List<String> carRegList=new ArrayList<>();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT registrationNumber FROM Car";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            carRegList.add(rs.getString(1));
        }
    return carRegList;
    }
    
    public boolean addCar(Car car) throws ClassNotFoundException,
         SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="INSERT INTO Car values(?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, car.getCrid());
        ps.setString(2, car.getRegistrationNumber());
        ps.setString(3, car.getModelNumber());
        ps.setString(4, car.getCid());
        return ps.executeUpdate()>0;
    }
    
    public boolean updateCar(Car car) throws ClassNotFoundException,
         SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="UPDATE Car set model=?,cid=? WHERE registrationNumber=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, car.getModelNumber());
        ps.setString(2, car.getCid());
        ps.setString(3, car.getRegistrationNumber());
        return ps.executeUpdate()>0;
    }
    
    
       public String getLatestCrid() throws SQLException, ClassNotFoundException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT crid FROM Car ORDER BY crid DESC LIMIT 1";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String crID=null;
        if(rs.next()){
            crID=rs.getString(1);
        }
       
        if(crID!=null){
            String[] temp=crID.split("CR");
            int tempNumber=Integer.parseInt(temp[1]);
            tempNumber++;
            
            if(tempNumber<10){
                return ("CR00"+tempNumber);
            }else if(tempNumber<100){
                return ("CR0"+tempNumber);
            }else{
                return ("CR"+tempNumber);
            }
        }else{
            return ("CR001");
        }
   }
}
