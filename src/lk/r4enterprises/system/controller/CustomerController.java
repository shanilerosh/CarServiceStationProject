/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.Customer;
import lk.r4enterprises.system.model.ReportTableModel;
import lk.r4enterprises.system.model.User;
import lk.r4enterprises.system.view.AlertBox;
import lk.r4enterprises.system.view.AnimateComponent;


/**
 *
 * @author shanil
 */
public class CustomerController implements Initializable    {

   
    @FXML
    private Text txtCid;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtMobileNumber;

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private TableView<Customer> tblCustomerData;

    @FXML
    private TableColumn<Customer,String> colCid;
    
    @FXML
    private TableColumn<Customer,String> colCnumber;

    @FXML
    private TableColumn<Customer,String> colAdress;

    @FXML
    private TableColumn<Customer, String> colName;
    
    @FXML
    private AnchorPane bkground;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtName;
    
    private User userInformation;

    public void setUserInformation(User userInformation) {
        this.userInformation = userInformation;
        
        if(!userInformation.getRole().equals("Admin")){
            btnDelete.setVisible(false);
            btnUpdate.setVisible(false);
        }
    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCid.setCellValueFactory(new PropertyValueFactory<>("cid"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name")); 
        colAdress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCnumber.setCellValueFactory
        (new PropertyValueFactory<>("mobileNumber"));
        try {
            loadCustomers();
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    
    public ObservableList<Customer> getAllCustomers() throws
            ClassNotFoundException, SQLException{
        ObservableList<Customer> customerList=FXCollections.
                observableArrayList();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Customer";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            customerList.add(new Customer(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
            ));
        }
    return customerList;
    
    }
    
    public List<String> getAllCustomerNames() throws
            ClassNotFoundException, SQLException{
        List<String> customerIdList=new ArrayList<>();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT name FROM Customer";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            customerIdList.add(rs.getString(1));
        }
    return customerIdList;
    }
    
    
    public String getCustomerNameFromId(String id) throws SQLException, ClassNotFoundException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT name FROM Customer where cid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            return rs.getString(1);
        }
        return "";
    }
    
    
    public Customer getCustomerFromName(String name) throws SQLException, ClassNotFoundException{
        Customer customer=null;
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Customer where name=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            customer=new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
        }
        return customer;
    }
    
    
    
    public Customer getCustomerFromId(String id) throws SQLException, ClassNotFoundException{
        Customer customer=null;
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Customer where cid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            customer=new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
        }
        return customer;
    }
    
    
    public void loadCustomers() throws ClassNotFoundException, SQLException{
        try {
            tblCustomerData.setItems(getAllCustomers());
            tblCustomerData.getColumns().clear();
            tblCustomerData.getColumns().addAll(colCid,colName,
                    colAdress,colCnumber);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(
                    Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
       setCustomerId();
    }
    
    
    public boolean addCustomer(Customer customer) throws ClassNotFoundException,
         SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="INSERT INTO Customer values(?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, customer.getCid());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getName());
        ps.setString(4, customer.getMobileNumber());
        
        return ps.executeUpdate()>0;
    }

    @FXML
    private void btnCreate_OnAction(ActionEvent event){
        if(txtName.getText().isEmpty()){
            AnimateComponent.animateEmptyField(txtName);
        }else if(txtMobileNumber.getText().isEmpty()){
            AnimateComponent.animateEmptyField(txtMobileNumber);
        }
        else{
            String name=txtName.getText();
            String address=txtAddress.getText();
            String mobile=txtMobileNumber.getText();
            String cid=txtCid.getText();
            
            Customer newCustomer=new Customer(cid, address, name, mobile);
        try {
            if(addCustomer(newCustomer)){
                tblCustomerData.getItems().add(newCustomer);
                AlertBox.showDisplayMessage("Successful", txtName.getText()
                        +"added Succesfully");
                loadCustomers();
            }else{
                AlertBox.showErrorMessage("Error", "Try again");
                loadCustomers();
            }
            
        } catch (Exception ex) {
                AlertBox.showErrorMessage("Error", ex.toString());
        }
        }
    }
    
    public boolean updateCustomer() throws SQLException, ClassNotFoundException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="UPDATE Customer SET adress=?,name=?,mobileNumber=? WHERE cid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, txtAddress.getText());
        ps.setString(2, txtName.getText());
        ps.setString(3, txtMobileNumber.getText());
        ps.setString(4, txtCid.getText());
        
        return ps.executeUpdate()>0;
    }
    

    
    public void setCustomerId() throws ClassNotFoundException, SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT cid FROM Customer ORDER BY cid DESC LIMIT 1";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String custId=null;
        if(rs.next()){
            custId=rs.getString(1);
        }
       
        if(custId!=null){
            String[] temp=custId.split("C");
            int tempNumber=Integer.parseInt(temp[1]);
            tempNumber++;
            
            if(tempNumber<10){
                txtCid.setText("C00"+tempNumber);
            }else if(tempNumber<100){
                txtCid.setText("C0"+tempNumber);
            }else{
                txtCid.setText("C"+tempNumber);
            }
        }else{
            txtCid.setText("C001");
        }
        
        //Clearing the fields
        txtName.clear();
        txtAddress.clear();
        txtMobileNumber.clear();
    }
    
    /**
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public String getCustId() throws ClassNotFoundException, SQLException{
        return txtCid.getText();
    }
    

    @FXML
    private void tblCustomerData_onMouseClicked(MouseEvent event) {
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        if(tblCustomerData.getSelectionModel().getSelectedIndex()>=0){
        Customer customer=tblCustomerData.getSelectionModel().getSelectedItem();
                txtCid.setText(customer.getCid());
                txtAddress.setText(customer.getAddress());
                txtName.setText(customer.getName());
                txtMobileNumber.setText(customer.getMobileNumber());
        }else{
            AlertBox.showErrorMessage("Error", "Try a correct Selection");
        }
        
    }

    @FXML
    private void btnUpdate_onAction(ActionEvent event) throws SQLException, 
            ClassNotFoundException {
            if(txtName.getText().isEmpty()){ 
                AnimateComponent.animateEmptyField(txtName);
            }
            else if(txtMobileNumber.getText().isEmpty()){
                AnimateComponent.animateEmptyField(txtMobileNumber);
            }
            else{
                updateCustomer();
                AlertBox.showDisplayMessage("Sucessful",
                        " succesfully updated.");
                loadCustomers();
                clearFielsAndLoadAgain();
            }
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) throws
            ClassNotFoundException, SQLException {
            if(deleteCustomer()){
                AlertBox.showDisplayMessage("Sucessful", txtName.getText()+
                        " succesfully Deleted.");
                loadCustomers();
                clearFielsAndLoadAgain();
            }else{
                AlertBox.showErrorMessage("Error", "Try again with correct"
                        + " Selection");
            }
            clearFielsAndLoadAgain();
            
    }

    private boolean deleteCustomer() throws SQLException, 
            ClassNotFoundException {
        Connection con=DBConnection.getInstance().getConnection();
        String sql="DELETE FROM Customer WHERE cid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, txtCid.getText());
        return ps.executeUpdate()>0;
    }

    private ObservableList<Customer> loadCustomersByFields(String value) throws
            SQLException, ClassNotFoundException {
        ObservableList<Customer> customerList=FXCollections.
                observableArrayList();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Customer WHERE name LIKE ? || adress"
                + " LIKE ? || mobileNumber LIKE ?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, "%"+value+"%");
        ps.setString(2, "%"+value+"%");
        ps.setString(3, "%"+value+"%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            customerList.add(new Customer(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
            ));
        }
    return customerList;
    }

    @FXML
    private void bkground_OnMouseClicked(MouseEvent event) throws 
            ClassNotFoundException, SQLException {
        tblCustomerData.getSelectionModel().clearSelection();
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }

    private void clearFielsAndLoadAgain() throws ClassNotFoundException,
            SQLException {
        setCustomerId();
        txtName.clear();
        txtAddress.clear();
        txtMobileNumber.clear();
        loadCustomers();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }
    
    public boolean updateCustomerFromName(Customer customer) throws SQLException, ClassNotFoundException{
        PreparedStatement ps = DBConnection.getInstance().getConnection().
                prepareStatement("UPDATE Customer set adress=?,mobileNumber=? WHERE name=?");
                ps.setString(1, customer.getAddress());
                ps.setString(2, customer.getMobileNumber());
                ps.setString(3, customer.getName());
                return ps.executeUpdate()>0;
    }
    
    

    @FXML
    private void txtSearch_OnKeyReleased(KeyEvent event) throws SQLException,
            ClassNotFoundException {
        String value=txtSearch.getText();
        tblCustomerData.setItems(loadCustomersByFields(value));
    }
    
   public String getLatestCid() throws SQLException, ClassNotFoundException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT cid FROM Customer ORDER BY cid DESC LIMIT 1";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String custId=null;
        if(rs.next()){
            custId=rs.getString(1);
        }
       
        if(custId!=null){
            String[] temp=custId.split("C");
            int tempNumber=Integer.parseInt(temp[1]);
            tempNumber++;
            
            if(tempNumber<10){
                return ("C00"+tempNumber);
            }else if(tempNumber<100){
                return ("C0"+tempNumber);
            }else{
                return ("C"+tempNumber);
            }
        }else{
            return ("C001");
        }
   }

//    @FXML
//    private void btnPrint_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException, JRException {
//        Connection connection = DBConnection.getInstance().getConnection();
//        JasperDesign jdesig=JRXmlLoader.load("/home/shanil/NetBeansProjects/CarServiceStationProject/src/reports/report1.jrxml");
//        String query="select * from Customer";
//        
//        JRDesignQuery updateQuery=new JRDesignQuery();
//        updateQuery.setText(query);
//        
//        jdesig.setQuery(updateQuery);
//        
//        JasperReport jReport=JasperCompileManager.compileReport(jdesig);
//        JasperPrint jPrint=JasperFillManager.fillReport(jReport, null,connection);
//        
//        JasperViewer.viewReport(jPrint);
//    }

    @FXML
    private void txtName_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtMobileNumber.requestFocus();
        }
        
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtName);
    }

    @FXML
    private void txtName_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyLettersAndCharacters(event);
    }

    @FXML
    private void txtAddress_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnCreate.requestFocus();
        }
        
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtAddress);
        
    }


    @FXML
    private void txtMobileNumber_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtAddress.requestFocus();
        }
        
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtMobileNumber);
        
    }

    @FXML
    private void txtMobileNumber_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
    }

    @FXML
    private void btnCreate_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnCreate.fire();
        }
    }



    ObservableList<ReportTableModel> getReceivableCustomer() throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list=FXCollections.observableArrayList();
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps=connection.prepareStatement("SELECT IFNULL(SUM(amount),0) FROM Order_ WHERE cid=?");
        PreparedStatement ps1=connection.prepareStatement("SELECT IFNULL(SUM(amount),0) FROM Receipt WHERE cid=?");
        PreparedStatement ps2=connection.prepareStatement("SELECT IFNULL(SUM(totalReturnAmount),0) FROM CustomerReturn WHERE cid=?");
        PreparedStatement ps3=connection.prepareStatement("SELECT IFNULL(SUM(Amount),0) FROM CreditNote WHERE cid=?");
        for (int i = 0; i <getAllCustomers().size(); i++) {
            Customer customer=getAllCustomers().get(i);
            String custId=customer.getCid();
            ps.setString(1, custId);
            ps1.setString(1,custId);
            ps2.setString(1,custId);
            ps3.setString(1,custId);
            ResultSet rs= ps.executeQuery();
            ResultSet rs1= ps1.executeQuery();
            ResultSet rs2= ps2.executeQuery();
            ResultSet rs3= ps3.executeQuery();
            double totalOrderAmount=0;
            double totalReceipt=0;
            double totalReturn=0;
            double totalCreditNote=0;
            if(rs.next()){
                 totalOrderAmount=rs.getDouble(1);
            }
            if(rs1.next()){
                 totalReceipt=rs1.getDouble(1);
            }
            if(rs2.next()){
                 totalReturn=rs2.getDouble(1);
            }
            if(rs3.next()){
                 totalCreditNote=rs3.getDouble(1);
            }
            double total=totalOrderAmount-totalReceipt-totalReturn+totalCreditNote;
            System.out.println(custId+" "+totalOrderAmount+" "+totalReceipt+" "+totalCreditNote+" "+totalReturn+" total"+total);
            
            if(total!=0){
                list.add(new ReportTableModel(customer.getName(),
                        custId,
                        String.format("%,.2f", total),
                        String.format("%,.2f", totalOrderAmount),
                        String.format("%,.2f", totalReceipt),
                        String.format("%,.2f", totalReturn),
                        String.format("%,.2f", totalCreditNote)
                ));
            }
        }
        
        return list;
    }

    

    

    


    
    
}