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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.CreditNote;
import lk.r4enterprises.system.model.Customer;
import lk.r4enterprises.system.model.DebitNote;
import lk.r4enterprises.system.model.ReportTableModel;
import lk.r4enterprises.system.model.Supplier;
import lk.r4enterprises.system.view.AlertBox;
import lk.r4enterprises.system.view.AnimateComponent;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author shanil
 */
public class CreditAndDebitNoteController implements Initializable{

    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtCustomerAmount;
    @FXML
    private TextField txtCustomerNarration;
    @FXML
    private Button btnAddCreditNote;
    @FXML
    private Text txtCreditNote;
    private Text txtDate;
    @FXML
    private Button btnResetCreditNote;
    @FXML
    private TextField txtSuplierName;
    @FXML
    private TextField txtSupplierAmount;
    @FXML
    private TextField txtSupplierNarration;
    @FXML
    private Button btnAddDebitNote;
    @FXML
    private Button btnResetDebitNote;
    @FXML
    private Text txtDebitNote;

    @FXML
    private void txtCustomerName_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtCustomerAmount.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtCustomerName);
    }

    @FXML
    private void txtCustomerName_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyLettersAndCharacters(event);
    }

    @FXML
    private void txtAmount_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtCustomerNarration.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtCustomerAmount);
    }

    @FXML
    private void txtAmount_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
    }

    @FXML
    private void txtNarration_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnAddCreditNote.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtCustomerNarration);
    }

    @FXML
    private void btnAddCreditNote_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnAddCreditNote.fire();
        }
    }

    @FXML
    private void btnAddCreditNote_OnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(txtCustomerName.getText().isEmpty()){
            AnimateComponent.animateEmptyField(txtCustomerName);
        }else if(txtCustomerAmount.getText().isEmpty()){
            AnimateComponent.animateEmptyField(txtCustomerAmount);
        }else{
            Customer customer = new CustomerController().getCustomerFromName(txtCustomerName.getText());
            if(customer==null){
                AnimateComponent.animateEmptyField(txtCustomerName);
                AlertBox.showErrorMessage("Error", txtCustomerName.getText()+" doesn't Exist");
            }else{
                CreditNote cn=new CreditNote(
                        txtCustomerName.getText(),
                        Double.parseDouble(txtCustomerAmount.getText()),
                        txtCustomerNarration.getText(),
                        txtCreditNote.getText(),
                        customer.getCid(),
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                );
                
                boolean isSave = addCreditNote(cn); 
                if(isSave){
                    AlertBox.showDisplayMessage("Sucess", txtCreditNote.getText()+" has been created sucessfully");
                }else{
                        AlertBox.showErrorMessage("Error", "Try again");
                }
                
                
            }
        
        }
        setCredtNoteId();
        txtCustomerName.clear();
        txtCustomerAmount.clear();
        txtCustomerNarration.clear();
        txtCustomerName.requestFocus();
    }

    @FXML
    private void btnResetCreditNote_onAction(ActionEvent event) {
        txtCustomerAmount.clear();
        txtCustomerName.clear();
        txtCustomerNarration.clear();
        txtCustomerName.clear();
    }

    @FXML
    private void txtSuplierName_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtSupplierAmount.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtSuplierName);
    }

    @FXML
    private void txtSuplierName_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyLettersAndCharacters(event);
    }

    @FXML
    private void txtSupplierAmount_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtSupplierNarration.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtSupplierAmount);
    }

    @FXML
    private void txtSupplierAmount_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
    }

    @FXML
    private void txtSupplierNarration_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnAddDebitNote.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtSupplierNarration);
    }

    @FXML
    private void btnAddDebitNote_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnAddDebitNote.fire();
        }
        
    }

    @FXML
    private void btnAddDebitNote_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        if(txtSuplierName.getText().isEmpty()){
            AnimateComponent.animateEmptyField(txtSuplierName);
        }else if(txtSupplierAmount.getText().isEmpty()){
            AnimateComponent.animateEmptyField(txtSupplierAmount);
        }else{
            Supplier supplier = new SupplierController().getSupplierFromName(txtSuplierName.getText());
            
            if(supplier==null){
                AnimateComponent.animateEmptyField(txtSuplierName);
                AlertBox.showErrorMessage("Error", txtSuplierName.getText()+" doesn't Exist");
            }else{
                DebitNote db=new DebitNote(
                        txtSuplierName.getText(),
                        Double.parseDouble(txtSupplierAmount.getText()),
                        txtSupplierNarration.getText(),
                        txtDebitNote.getText(),
                        supplier.getSid(),
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                );
                
                boolean isSave = addDebitNote(db); 
                if(isSave){
                    AlertBox.showDisplayMessage("Sucess", txtDebitNote.getText()+" has been created sucessfully");
                }else{
                        AlertBox.showErrorMessage("Error", "Try again");
                }
                
                
            }
        
        }
            txtSuplierName.clear();
            txtSupplierAmount.clear();
            txtSupplierNarration.clear();
            txtSuplierName.requestFocus();
            setDebitNoteId();
            
    }
    
    public void setDebitNoteId() throws ClassNotFoundException, SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT dnId FROM DebitNote ORDER BY dnId DESC LIMIT 1";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String dnId=null;
        if(rs.next()){
            dnId=rs.getString(1);
        }
       
        if(dnId!=null){
            String[] temp=dnId.split("DNI-");
            int tempNumber=Integer.parseInt(temp[1]);
            tempNumber++;
            
            if(tempNumber<10){
                txtDebitNote.setText("DNI-00"+tempNumber);
            }else if(tempNumber<100){
                txtDebitNote.setText("DNI-0"+tempNumber);
            }else{
                txtDebitNote.setText("DNI"+tempNumber);
            }
        }else{
            txtDebitNote.setText("DNI-001");
        }
        btnResetDebitNote.fire();
    }
    
    public void setCredtNoteId() throws ClassNotFoundException, SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT cnId FROM CreditNote ORDER BY cnId DESC LIMIT 1";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String cnId=null;
        if(rs.next()){
            cnId=rs.getString(1);
        }
       
        if(cnId!=null){
            String[] temp=cnId.split("CNI-");
            int tempNumber=Integer.parseInt(temp[1]);
            tempNumber++;
            
            if(tempNumber<10){
                txtCreditNote.setText("CNI-00"+tempNumber);
            }else if(tempNumber<100){
                txtCreditNote.setText("CNI-0"+tempNumber);
            }else{
                txtCreditNote.setText("CNI"+tempNumber);
            }
        }else{
            txtCreditNote.setText("CNI-001");
        }
        btnResetCreditNote.fire();
    }

    @FXML
    private void btnResetDebitNote_onAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        txtSuplierName.clear();
        txtSupplierAmount.clear();
        txtSupplierNarration.clear();
        txtSuplierName.requestFocus();
    }

    @FXML
    private void mainPane_OnMouseClicked(MouseEvent event) {
    }

    private boolean addDebitNote(DebitNote db) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO DebitNote VALUES(?,?,?,?,?)");
        ps.setString(1, db.getDnId());
        ps.setString(2, db.getSid());
        ps.setDouble(3, db.getAmount());
        ps.setString(4, db.getNarration());
        ps.setString(5, db.getDateOfTransaction());
        return ps.executeUpdate()>0;
            
    }

    private boolean addCreditNote(CreditNote cn) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO CreditNote VALUES(?,?,?,?,?)");
        ps.setString(1, cn.getCnID());
        ps.setString(2, cn.getCid());
        ps.setDouble(3, cn.getAmount());
        ps.setString(4, cn.getNarration());
        ps.setString(5, cn.getDateOfTransaction());
        return ps.executeUpdate()>0;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            TextFields.bindAutoCompletion(txtSuplierName, new SupplierController().getAllSupplierNames());
            TextFields.bindAutoCompletion(txtCustomerName, new CustomerController().getAllCustomerNames());
             setCredtNoteId();
            setDebitNoteId();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CreditAndDebitNoteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    ObservableList<ReportTableModel> getAllCreditNotes(String dateFrom, String dateTo) throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list=FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM CreditNote WHERE dateOfTransaction BETWEEN ? AND ?");
        ps.setString(1, dateFrom);
        ps.setString(2, dateTo);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            list.add(new ReportTableModel(new CustomerController().getCustomerNameFromId(rs.getString(2)),
                    rs.getString(5),
                    rs.getString(2),
                    String.format("%,.2f", rs.getDouble(3)
                    )));
        }
        return list;
    }
    
    
    ObservableList<ReportTableModel> getAllDebitNotes(String dateFrom, String dateTo) throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list=FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM DebitNote WHERE dateOfTransaction BETWEEN ? AND ?");
        ps.setString(1, dateFrom);
        ps.setString(2, dateTo);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            list.add(new ReportTableModel(new SupplierController().getSupplierFromId(rs.getString(2)).getName(),
                    rs.getString(5),
                    rs.getString(2),
                    String.format("%,.2f", rs.getDouble(3)
                    )));
        }
        return list;
    }
    
}