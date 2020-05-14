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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import lk.r4enterprises.system.model.ReportTableModel;
import lk.r4enterprises.system.model.Supplier;
import lk.r4enterprises.system.model.User;
import lk.r4enterprises.system.view.AlertBox;
import lk.r4enterprises.system.view.AnimateComponent;

/**
 *
 * @author shanil
 */
public class SupplierController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    
    
    @FXML
    private TableView<Supplier> tblSupplierData;
    @FXML
    private TableColumn<Supplier, String> colSid;
    @FXML
    private TableColumn<Supplier, String> colName;
    @FXML
    private TableColumn<Supplier, String> colEmail;
    @FXML
    private TableColumn<Supplier, String> colAddress;
    @FXML
    private TableColumn<Supplier, String> colMobileNumber;
    
    @FXML
    private Text txtSid;
    @FXML
    private TextField txtEmail;
    
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtMobileNumber;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtsearchSupplier;
    
    private User user;
    @FXML
    private Button btnActiveInactive;
    @FXML
    private TableColumn<Supplier, String> colStatus;

    @FXML
    private void btnCreate_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        if(txtName.getText().isEmpty()){
            AnimateComponent.animateEmptyField(txtName);
        }else if(txtMobileNumber.getText().isEmpty()){
            AnimateComponent.animateEmptyField(txtMobileNumber);
        }
        else{
            if(addSupplier()){
                tblSupplierData.getItems().add(
                new Supplier(
                    txtSid.getText(),
                    txtName.getText(),
                    txtEmail.getText(),
                    txtAddress.getText(),
                    txtMobileNumber.getText(),
                    "Active"
               ));
                AlertBox.showDisplayMessage("Successful", txtName.getText()
                        +"added Succesfully");
                loadSuppliers();

            }else{
                AlertBox.showErrorMessage("Error", "Try again");
                loadSuppliers();
            }
        }
       clearFielsAndLoadAgain();
    }

    public void setUser(User user) {
        this.user = user;
        if(!user.getRole().equals("Admin")){
            btnActiveInactive.setVisible(false);
            btnUpdate.setVisible(false);
        }
        
        
    }

    @FXML
    private void btnUpdate_OnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        boolean confirmation = AlertBox.showConfMessage("Are you sure to update "+txtSid.getText(), "Confirmation");
            if(confirmation){
                if(txtName.getText().isEmpty()){
                AnimateComponent.animateEmptyField(txtName);
            }else if(txtMobileNumber.getText().isEmpty()){
                AnimateComponent.animateEmptyField(txtMobileNumber);
            }
            else{
                updateSupplier();
                AlertBox.showDisplayMessage("Sucessful", txtSid.getText()+
                        " succesfully updated.");
                loadSuppliers();
            }
            
            }
            
            
    }
    
    public boolean updateSupplier() throws SQLException, ClassNotFoundException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="UPDATE Supplier SET name=?,email=?,adress=?,mobile=? WHERE sid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, txtName.getText());
        ps.setString(2, txtEmail.getText());
        ps.setString(3, txtAddress.getText());
        ps.setString(4, txtMobileNumber.getText());
        ps.setString(5, txtSid.getText());
        return ps.executeUpdate()>0;
    }

    
    private boolean makeActiveInactive(String status) throws SQLException, 
            ClassNotFoundException {
        Connection con=DBConnection.getInstance().getConnection();
        String sql="UPDATE Supplier set status=? WHERE sid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setInt(1, status.equals("Active") ? 0 : 1);
        ps.setString(2, txtSid.getText());
        return ps.executeUpdate()>0;
    }

    @FXML
    private void searchSupplier(KeyEvent event) throws SQLException, ClassNotFoundException {
        String value=txtsearchSupplier.getText();
        tblSupplierData.setItems(loadSupplierByFields(value));
    }
    
    private ObservableList<Supplier> loadSupplierByFields(String value) throws
            SQLException, ClassNotFoundException {
        ObservableList<Supplier> suplierList=FXCollections.
                observableArrayList();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Supplier WHERE name LIKE ? || email"
                + " LIKE ? || adress LIKE ? || mobile LIKE ? || status LIKE ?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, "%"+value+"%");
        ps.setString(2, "%"+value+"%");
        ps.setString(3, "%"+value+"%");
        ps.setString(4, "%"+value+"%");
        ps.setString(5, "%"+value+"%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            suplierList.add(new Supplier(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getInt(6)==1?"Active" : "Inactive"
            ));
        }
    return suplierList;
    }

    @FXML
    private void mainPane_OnMouseClicked(MouseEvent event) throws ClassNotFoundException, SQLException {
        tblSupplierData.getSelectionModel().clearSelection();
        loadSuppliers();
        btnActiveInactive.setDisable(true);
        btnUpdate.setDisable(true);
        
    }
    
    
    private void clearFielsAndLoadAgain() throws ClassNotFoundException,
            SQLException {
        setSupplierId();
        txtAddress.clear();
        txtEmail.clear();
        txtMobileNumber.clear();
        txtMobileNumber.clear();
        loadSuppliers();
        btnActiveInactive.setDisable(true);
        btnUpdate.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colSid.setCellValueFactory(new PropertyValueFactory<>("sid"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colMobileNumber.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        try {
            loadSuppliers();
            clearFielsAndLoadAgain();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SupplierController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadSuppliers() throws ClassNotFoundException, SQLException {
        tblSupplierData.setItems(getAllSuppliers());
        tblSupplierData.getColumns().clear();
        tblSupplierData.getColumns().addAll(colSid,colName,colEmail,colAddress,
                colMobileNumber,colStatus);
        setSupplierId();
    }
    
    public ObservableList<Supplier> getAllSuppliers() throws ClassNotFoundException, SQLException{
        ObservableList<Supplier> supplierList=FXCollections.
                observableArrayList();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Supplier";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            supplierList.add(new Supplier(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                   rs.getInt(6)==1?"Active" : "Inactive" 
            ));
            System.out.println(rs.getString(5));
        }
    return supplierList;
    }
    
    
    public String getSupplierId(String name) throws ClassNotFoundException, SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT sid FROM Supplier WHERE name=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
                return rs.getString(1);
        }
    return null;
    }
    
    public Supplier getSupplierFromId(String sid) throws ClassNotFoundException, SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        Supplier sup=null;
        String sql="SELECT * FROM Supplier WHERE sid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, sid);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
                sup=new Supplier(rs.getString(1),
                       rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6)==1?"Active" : "Inactive"
                        
                );
        }
    return sup;
    }
    
    
    public Supplier getSupplierFromName(String name) throws ClassNotFoundException, SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        Supplier sup=null;
        String sql="SELECT * FROM Supplier WHERE name=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
                sup=new Supplier(rs.getString(1),
                       rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6)==1?"Active" : "Inactive"
                );
        }
    return sup;
    }
    
    public List<String> getAllSupplierNames() throws ClassNotFoundException, SQLException{
        List<String> supplierNames=new ArrayList<>();
        for (int i = 0; i < getAllSuppliers().size(); i++) {
            supplierNames.add(getAllSuppliers().get(i).getName());
        }
        return supplierNames;
    }
    
    
    
    
    
    public boolean addSupplier() throws ClassNotFoundException,
         SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="INSERT INTO Supplier values(?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, txtSid.getText());
        ps.setString(2, txtName.getText());
        ps.setString(3, txtEmail.getText());
        ps.setString(4, txtAddress.getText());
        ps.setString(5, txtMobileNumber.getText());
        ps.setString(6, "1");
        return ps.executeUpdate()>0;
    }
    
    public void setSupplierId() throws ClassNotFoundException, SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT sid FROM Supplier ORDER BY sid DESC LIMIT 1";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String supId=null;
        if(rs.next()){
            supId=rs.getString(1);
        }
       
        if(supId!=null){
            String[] temp=supId.split("S");
            int tempNumber=Integer.parseInt(temp[1]);
            tempNumber++;
            
            if(tempNumber<10){
                txtSid.setText("S00"+tempNumber);
            }else if(tempNumber<100){
                txtSid.setText("S0"+tempNumber);
            }else{
                txtSid.setText("S"+tempNumber);
            }
        }else{
            txtSid.setText("S001");
        }
        
        //Clearing the fields
        txtName.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtMobileNumber.clear();
    }

    @FXML
    private void tblSupplierData_OnMouseClicked(MouseEvent event) {
        if(tblSupplierData.getSelectionModel().getSelectedIndex()>=0){
            btnActiveInactive.setDisable(false);
        btnUpdate.setDisable(false);
        Supplier selected=tblSupplierData.getSelectionModel().getSelectedItem();
        txtSid.setText(selected.getSid());
        txtName.setText(selected.getName());
        txtAddress.setText(selected.getAddress());
        txtEmail.setText(selected.getEmail());
        txtMobileNumber.setText(selected.getMobile());
        }else{
            AlertBox.showErrorMessage("Error", "Try a correct Selection");
        }
        
    }

    @FXML
    private void txtName_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtEmail.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtName);
    }

    @FXML
    private void txtName_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyLettersAndCharacters(event);
    }

    @FXML
    private void txtEmail_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtAddress.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtEmail);
        
    }

    @FXML
    private void txtAddress_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtMobileNumber.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtMobileNumber);
    }

    @FXML
    private void txtMobileNumber_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnCreate.requestFocus();
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

    ObservableList<ReportTableModel> getPayableSuppliers() throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list=FXCollections.observableArrayList();
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps=connection.prepareStatement("SELECT IFNULL(SUM(totalAmount),0) FROM GRNMaster WHERE sid=?");
        PreparedStatement ps1=connection.prepareStatement("SELECT IFNULL(SUM(amount),0) FROM Payment WHERE sid=?");
        PreparedStatement ps2=connection.prepareStatement("SELECT IFNULL(SUM(totalReturnAmount),0) FROM SupplierReturn WHERE sid=?");
        PreparedStatement ps3=connection.prepareStatement("SELECT IFNULL(SUM(Amount),0) FROM DebitNote WHERE sid=?");
        for (int i = 0; i <getAllSuppliers().size(); i++) {
            Supplier supplier=getAllSuppliers().get(i);
            String supId=supplier.getSid();
            ps.setString(1, supId);
            ps1.setString(1,supId);
            ps2.setString(1,supId);
            ps3.setString(1,supId);
            ResultSet rs= ps.executeQuery();
            ResultSet rs1= ps1.executeQuery();
            ResultSet rs2= ps2.executeQuery();
            ResultSet rs3= ps3.executeQuery();
            double totalSupplierAmount=0;
            double totalPayment=0;
            double totalReturn=0;
            double totalDebitNote=0;
            if(rs.next()){
                 totalSupplierAmount=rs.getDouble(1);
            }
            if(rs1.next()){
                 totalPayment=rs1.getDouble(1);
            }
            if(rs2.next()){
                 totalReturn=rs2.getDouble(1);
            }
            if(rs3.next()){
                 totalDebitNote=rs3.getDouble(1);
            }
            double total=totalSupplierAmount-totalPayment-totalReturn+totalDebitNote;
            
            if(total!=0){
                list.add(new ReportTableModel(supplier.getName(),
                        supId,
                        String.format("%,.2f", total),
                        String.format("%,.2f", totalSupplierAmount),
                        String.format("%,.2f", totalPayment),
                        String.format("%,.2f", totalReturn),
                        String.format("%,.2f", totalDebitNote)
                ));
            }
        }
        
        return list;
    }

    @FXML
    private void btnActiveInactive_OnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String status = tblSupplierData.getSelectionModel().getSelectedItem().getStatus();
        
        if(makeActiveInactive(status)){
                if (status.equals("Active")) {
                AlertBox.showDisplayMessage("Sucessful", txtName.getText()
                        + " is now on Inactive Mode.No GRNs can be raised from this customer now Onwards");
            } else {
                AlertBox.showDisplayMessage("Sucessful", txtName.getText()
                        + " is now on Active Mode.GRNs can be raised from this customer now Onwards");
            }
            loadSuppliers();
            clearFielsAndLoadAgain();
        } else {
            AlertBox.showErrorMessage("Error", "Try again with correct"
                    + " Selection");
        }
        clearFielsAndLoadAgain();
    }
    
    
    
}
