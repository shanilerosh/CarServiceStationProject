/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author shanil
 */
public class DBConnection {
    private static DBConnection dbConnection;
    private Connection con;
    
    
    private DBConnection() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"
                + "CarServiceStation","shanil","Outsider_2019");
    
    }
    
    public static DBConnection getInstance() throws ClassNotFoundException,
            SQLException{
        return dbConnection==null ? dbConnection=new DBConnection() : 
                dbConnection;
    }
    
   public Connection getConnection(){
       return con;
   }
}
