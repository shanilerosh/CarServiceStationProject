/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import animatefx.animation.Shake;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.Customer;
import lk.r4enterprises.system.model.Receipt;
import lk.r4enterprises.system.model.ReceiptTableModel;
import lk.r4enterprises.system.view.AlertBox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author shanil
 */
public class ReceiptController implements Initializable{

    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtAmount;
    @FXML
    private ComboBox<String> comboPaymentType;
    @FXML
    private TextField txtNarration;
    @FXML
    private Button btnAddReceipt;
    @FXML
    private TableView<ReceiptTableModel> tblReceiptData;
    @FXML
    private TableColumn<ReceiptTableModel, String> colCustomerName;
    @FXML
    private TableColumn<ReceiptTableModel, String> colDate;
    @FXML
    private TableColumn<ReceiptTableModel, Double> colAmount;
    @FXML
    private Text txtReceipt;
    
    @FXML
    private TextField txtSearchReceipts;
    @FXML
    private Button btnDelete;
    private Text txtTime;
    @FXML
    private TableColumn<ReceiptTableModel, String> colReceiptID;
    @FXML
    private TableColumn<ReceiptTableModel, String> colReceiptType;
    
    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    @FXML
    private TableColumn<ReceiptTableModel, String> colNarration;
    @FXML
    private Button btnReset;

    @FXML
    private void txtsearchItems_OnKeyReleased(KeyEvent event) throws SQLException, ClassNotFoundException {
        String val=txtSearchReceipts.getText();
        tblReceiptData.setItems(loadReceiptByFields(val));
    }

    @FXML
    private void btnAddReceipt_OnAction(ActionEvent event) throws SQLException, ClassNotFoundException, FileNotFoundException, JRException {
       boolean isPrint=false;
        if(txtCustomerName.getText().isEmpty()){
           animateEmptyField(txtCustomerName);
       }else if(txtAmount.getText().isEmpty()){
           animateEmptyField(txtAmount);
       }else if(comboPaymentType.getSelectionModel().isEmpty()){
               animateEmptyField(comboPaymentType);
       }else{
           Customer cust=new CustomerController().getCustomerFromName(txtCustomerName.getText());
           if(cust==null){
               animateEmptyField(txtCustomerName);
               AlertBox.showErrorMessage("Error", txtCustomerName.getText()+" not found");
           }else{
               Receipt receipt=new Receipt(txtReceipt.getText(),
                   cust.getCid(),
                   Double.parseDouble(txtAmount.getText()),
                   comboPaymentType.getSelectionModel().getSelectedItem(),
                   LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                   txtNarration.getText()
           );
               
               
           boolean isAdded=addReceipt(receipt);
           
           if(isAdded){
               tblReceiptData.getItems().add(new ReceiptTableModel(
                       receipt.getRid(),
                       txtCustomerName.getText(),
                       receipt.getrType(), 
                       receipt.getDateOfReceipt(),
                       receipt.getAmount(),
                       receipt.getNarration()
               ));
               isPrint=true;
               clearFields();
               setReceiptId();
           }else{
               AlertBox.showDisplayMessage("Fail", txtReceipt.getText()+" has been already created");
           }
           
           if(isPrint){
                   boolean print = AlertBox.showConfMessage(receipt.getRid()+" has been created successfully.Do you want to print the Receipt?", "Sucess,Proceed to print?");
                   if(print){
                       printReceipt(receipt.getRid());
                   }
           }
           
           
           tblReceiptData.getSelectionModel().selectRange(0, tblReceiptData.getItems().size());
           loadReceipts();
           }
           
           
           
           
       }
    }

    @FXML
    private void tblItemData_OnMouseClicked(MouseEvent event) {
        if(tblReceiptData.getSelectionModel().getSelectedIndex()>=0){
            ReceiptTableModel selectedItem = tblReceiptData.getSelectionModel().getSelectedItem();
            txtCustomerName.setText(selectedItem.getCustName());
            txtAmount.setText(Double.toString(selectedItem.getAmount()));
            comboPaymentType.getSelectionModel().select(selectedItem.getReceiptType());
            txtNarration.setText(selectedItem.getNarration());
            txtReceipt.setText(selectedItem.getRid());
            btnDelete.setDisable(false);
            btnAddReceipt.setDisable(true);
        }else{
            AlertBox.showErrorMessage("Wrong Choice", "Select a correct data row");
        }
        
        
        
    }

    

    @FXML
    private void btnDelete_OnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(deleteReceipt()){
            AlertBox.showDisplayMessage("Success", txtReceipt.getText()+" has been deleted");
            loadReceipts();
        }else{
                AlertBox.showErrorMessage("Error", "Try again");
        }
        btnDelete.setDisable(true);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colReceiptID.setCellValueFactory(new PropertyValueFactory<>("rid"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("custName")); 
        colReceiptType.setCellValueFactory(new PropertyValueFactory<>("receiptType"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateOfReceipt"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
        try {
            TextFields.bindAutoCompletion(txtCustomerName, new CustomerController().getAllCustomerNames());
            loadReceipts();
            loadPaymentOptions();
            btnDelete.setDisable(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReceiptController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
    
        public ObservableList<ReceiptTableModel> getAllReceipts() throws
            ClassNotFoundException, SQLException{
        ObservableList<ReceiptTableModel> receiptList=FXCollections.
                observableArrayList();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Receipt";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            receiptList.add(new ReceiptTableModel(
                    rs.getString(1),
                    new CustomerController().getCustomerNameFromId(rs.getString(2)),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getDouble(3),
                    rs.getString(6)
            ));
        }
    return receiptList;
    }
    
    private void animateEmptyField(TextField textField) {
        new Shake(textField).play();
        textField.setStyle("-fx-border-color:#ff0000");
        pause.setOnFinished(evt -> textField.setStyle("-fx-border-color:#097541"));
        pause.play();
    }
    
    private void animateEmptyField(ComboBox<String> combo) {
        new Shake(combo).play();
        combo.setStyle("-fx-border-color:#ff0000");
        pause.setOnFinished(evt -> combo.setStyle("-fx-border-color:#097541"));
        pause.play();
    }
    
   
    public void loadReceipts() throws ClassNotFoundException, SQLException{
        try {
            tblReceiptData.setItems(getAllReceipts());
            tblReceiptData.getColumns().clear();
            tblReceiptData.getColumns().addAll(colReceiptID,colCustomerName,colAmount,
                    colDate,colReceiptType,colNarration);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(
                    Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
       setReceiptId();
    }
    
    
    public boolean addReceipt(Receipt receipt) throws ClassNotFoundException,
         SQLException{
        
        Connection con=DBConnection.getInstance().getConnection();
        String sql="INSERT INTO Receipt values(?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, receipt.getRid());
        ps.setString(2, receipt.getCid());
        ps.setDouble(3, receipt.getAmount());
        ps.setString(4, receipt.getrType());
        ps.setString(5, receipt.getDateOfReceipt());
        ps.setString(6, receipt.getNarration());
        return ps.executeUpdate()>0;
    }

    
    @FXML
    private void btnReset_onAction(ActionEvent event) {
        tblReceiptData.getSelectionModel().clearSelection();
        btnDelete.setDisable(true);
        btnAddReceipt.setDisable(false);
        clearFields();
    }

    
    public void setReceiptId() throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT rid FROM Receipt ORDER BY rid DESC LIMIT 1";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String receiptId = null;
        if (rs.next()) {
            receiptId = rs.getString(1);
        }

        if (receiptId != null) {
            String[] temp = receiptId.split("REC-");
            int tempNumber = Integer.parseInt(temp[1]);
            tempNumber++;

            if (tempNumber < 10) {
                txtReceipt.setText("REC-00" + tempNumber);
            } else if (tempNumber < 100) {
                txtReceipt.setText("REC-0" + tempNumber);
            } else {
                txtReceipt.setText("REC-" + tempNumber);
            }
        } else {
            txtReceipt.setText("REC-001");
        }
    }
    

    private boolean deleteReceipt() throws SQLException, 
            ClassNotFoundException {
        Connection con=DBConnection.getInstance().getConnection();
        
        con.setAutoCommit(false);
        String sql="DELETE FROM Receipt WHERE rid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, tblReceiptData.getSelectionModel().getSelectedItem().getRid());
        return ps.executeUpdate()>0;
    }

    private ObservableList<ReceiptTableModel> loadReceiptByFields(String value) throws
            SQLException, ClassNotFoundException {
        ObservableList<ReceiptTableModel> receiptList=FXCollections.
                observableArrayList();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT Receipt.rid,Customer.name,Receipt.amount,Receipt."
                + "dateOfReceipt,Receipt.rType,Receipt.narration FROM Receipt INNER JOIN Customer"
                + " ON Customer.cid=Receipt.cid"
                + " WHERE Receipt.cid LIKE ? || "
                + "Customer.name LIKE ? || Receipt.rid LIKE ? || Receipt.amount"
                + " LIKE ? || Receipt.dateOfReceipt LIKE ? || Receipt.rType LIKE ?"; 
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, "%"+value+"%");
        ps.setString(2, "%"+value+"%");
        ps.setString(3, "%"+value+"%");
        ps.setString(4, "%"+value+"%");
        ps.setString(5, "%"+value+"%");
        ps.setString(6, "%"+value+"%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            receiptList.add(new ReceiptTableModel(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(5),
                    rs.getString(4),
                    Double.parseDouble(rs.getString(3)),
                    rs.getString(6)
            ));
        }
    return receiptList;
    }

   
    private void clearFields() {
    txtAmount.clear();
        txtCustomerName.clear();
        txtNarration.clear();
        txtSearchReceipts.clear();
        txtCustomerName.requestFocus();
        comboPaymentType.getSelectionModel().clearSelection();
    }
    

    private void loadPaymentOptions() {
        comboPaymentType.getItems().addAll(
                "Cash",
                "Credit Card",
                "Cheque",
                "Bank Transfer"
        );
    }

    @FXML
    private void mainPane_OnMouseClicked(MouseEvent event) {
        tblReceiptData.getSelectionModel().clearSelection();
        btnDelete.setDisable(true);
    }

    @FXML
    private void txtCustomerName_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtAmount.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtCustomerName);
    }

    @FXML
    private void txtCustomerName_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyLettersAndCharacters(event);
    }

    private void txtAmount_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            comboPaymentType.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtAmount);
    }

    @FXML
    private void txtAmount_onKeyTyped(KeyEvent event) {
       TextFieldEventsHandling.allowOnlyNumbersAndDecimal(event);
        
       
    }

    @FXML
    private void comboPaymentType_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            comboPaymentType.show();
            if(TextFieldEventsHandling.isEnterPressed(event)){
                txtNarration.requestFocus();
            }
        }
        
    }

    @FXML
    private void txtNarration_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnAddReceipt.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtCustomerName);
    }

    @FXML
    private void btnAddReceipt_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnAddReceipt.fire();
        }
    }

    private void printReceipt(String rid) throws FileNotFoundException, JRException, ClassNotFoundException, SQLException {
           InputStream in=new FileInputStream("/home/shanil/NetBeansProjects/CarServiceStationProject/src/lk/r4enterprises/system/view/Reports/Receipt.jrxml");
            JasperReport ja=JasperCompileManager.compileReport(in);
            Map<String,Object> para=new HashMap<>();
            para.put("ReceiptID", rid);
            JasperPrint jp=JasperFillManager.fillReport(ja,para,DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jp,false);
    }

    @FXML
    private void txtAmount_onKeyPresed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            comboPaymentType.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtAmount);
    }


    
}

