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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.Payment;
import lk.r4enterprises.system.model.PaymentTableModel;
import lk.r4enterprises.system.model.Supplier;
import lk.r4enterprises.system.view.AlertBox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author shanil
 */
public class PaymentController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    
    @FXML
    private TextField txtSupplierInvoiceNumbers;
    @FXML
    private TextField txtAmount;
    @FXML
    private Button btnAddPayment;
    
    @FXML
    private TableColumn<PaymentTableModel, String> colPayID;
    @FXML
    private TableColumn<PaymentTableModel, String> colSupplierName;
    @FXML
    private TableColumn<PaymentTableModel, String> colDate;
    @FXML
    private TableColumn<PaymentTableModel, Double> colAmount;
    
    @FXML
    private TextField txtSearchPayment;
    @FXML
    private Button btnDelete;

    @FXML
    private TableColumn<PaymentTableModel, String> colSupInvoiceNumber;
    @FXML
    private Text txtPayId;
    @FXML
    private TableColumn<PaymentTableModel, String> colPaymentType;
    @FXML
    private TableView<PaymentTableModel> tblPaymentData;
    @FXML
    private TextField txtSupplierName;
    @FXML
    private ComboBox<String> comboPaymentType;
    
    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    @FXML
    private Button btnReset;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colPayID.setCellValueFactory(new PropertyValueFactory<>("sid"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName")); 
        colPaymentType.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateOfPayment"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colSupInvoiceNumber.setCellValueFactory(new PropertyValueFactory<>("supplierInvoiceNumber"));
        try {
            TextFields.bindAutoCompletion(txtSupplierName, new SupplierController().getAllSupplierNames());
            loadPayments();
            loadPaymentOptions();
            btnDelete.setDisable(true);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ReceiptController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void loadPayments() throws ClassNotFoundException, SQLException{
        try {
            tblPaymentData.setItems(getAllPayments());
            tblPaymentData.getColumns().clear();
            tblPaymentData.getColumns().addAll(colPayID,colSupplierName,colDate,
                    colAmount,colPaymentType,colSupInvoiceNumber);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
       setPaymentId();
    }
    
    
    public void setPaymentId() throws ClassNotFoundException, SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT pid FROM Payment ORDER BY pid DESC LIMIT 1";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String paymentId=null;
        if(rs.next()){
            paymentId=rs.getString(1);
        }
       
        if(paymentId!=null){
            String[] temp=paymentId.split("PAY-");
            int tempNumber=Integer.parseInt(temp[1]);
            tempNumber++;
            
            if(tempNumber<10){
                txtPayId.setText("PAY-00"+tempNumber);
            }else if(tempNumber<100){
                txtPayId.setText("PAY-0"+tempNumber);
            }else{
                txtPayId.setText("PAY"+tempNumber);
            }
        }else{
            txtPayId.setText("PAY-001");
        }
        clearFields();
    }
    
    
    
    public ObservableList<PaymentTableModel> getAllPayments() throws
            ClassNotFoundException, SQLException{
        ObservableList<PaymentTableModel> paymenttList=FXCollections.
                observableArrayList();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Payment";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            paymenttList.add(new PaymentTableModel(rs.getString(1),
                    new SupplierController().getSupplierFromId(rs.getString(2)).getName(),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getDouble(3),
                    rs.getString(6)
            ));
                    
        }
    return paymenttList;
    }
    
    
    

    @FXML
    private void txtsearchItems_OnKeyReleased(KeyEvent event) throws ClassNotFoundException, SQLException {
        String val=txtSearchPayment.getText();
        tblPaymentData.setItems(loadPaymentsByFields(val));
    }

    @FXML
    private void btnAddPayment_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException, FileNotFoundException, JRException {
        boolean isPrint=false;
        if(txtSupplierName.getText().isEmpty()){
           animateEmptyField(txtSupplierName);
       }else if(txtAmount.getText().isEmpty()){
           animateEmptyField(txtAmount);
       }else if(comboPaymentType.getSelectionModel().isEmpty()){
               animateEmptyField(comboPaymentType);
       }else if(txtSupplierInvoiceNumbers.getText().isEmpty()){
               animateEmptyField(txtSupplierInvoiceNumbers);
       }else{
           Supplier sup=new SupplierController().getSupplierFromName(txtSupplierName.getText());
           if(sup==null){
               animateEmptyField(txtSupplierName);
               AlertBox.showErrorMessage("Error", txtSupplierName.getText()+" not found in our supplier Database");
           }else{
               Payment payment=new Payment(txtPayId.getText(),
                   sup.getSid(),
                   Double.parseDouble(txtAmount.getText()),
                   comboPaymentType.getSelectionModel().getSelectedItem(),
                   LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                   txtSupplierInvoiceNumbers.getText()
           );
               
               
           boolean isAdded=addPayment(payment);
           System.out.println("boolean ="+isAdded);
           
           if(isAdded){
               tblPaymentData.getItems().add(new PaymentTableModel(txtPayId.getText()
                       , txtSupplierName.getText(),
                       comboPaymentType.getSelectionModel().getSelectedItem(),
                       LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                       Double.parseDouble(txtAmount.getText()),
                       txtSupplierInvoiceNumbers.getText()
               ));
               isPrint=true;
               clearFields();
               setPaymentId();
           }else{
               AlertBox.showErrorMessage("Fail", txtPayId.getText()+" has been already created");
           }
           
           
           if(isPrint){
               boolean print = AlertBox.showConfMessage(payment.getPid()+" has been created successfully.Do you want to print the Payment Slip?", "Sucess,Proceed to print?");
                   if(print){
                       printPayment(payment.getPid());
                   }
               
           }
           
           
           loadPayments();
           }
           
           
           
           
       }
    }

    @FXML
    private void tblItemData_OnMouseClicked(MouseEvent event) {
        if(tblPaymentData.getSelectionModel().getSelectedIndex()>=0){
            PaymentTableModel selectedItem = tblPaymentData.getSelectionModel().getSelectedItem();
            txtSupplierName.setText(selectedItem.getSupplierName());
            txtAmount.setText(Double.toString(selectedItem.getAmount()));
            comboPaymentType.getSelectionModel().select(selectedItem.getPaymentType());
            txtSupplierInvoiceNumbers.setText(selectedItem.getSupplierInvoiceNumber());
            txtPayId.setText(selectedItem.getPid());
            btnDelete.setDisable(false);
            btnAddPayment.setDisable(true);
        }else{
            AlertBox.showErrorMessage("Wrong Choice", "Select a correct data row");
        }
        
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(deletePayment()){
            loadPayments();
            AlertBox.showDisplayMessage("Success", txtPayId.getText()+" has been deleted");
        }else{
                AlertBox.showErrorMessage("Error", "Try again");
        }
        
        btnDelete.setDisable(true);
    }

    

    private void clearFields() {
        txtSupplierName.clear();
        txtSupplierInvoiceNumbers.clear();
        txtAmount.clear();
        comboPaymentType.getSelectionModel().clearSelection();
        txtSearchPayment.clear();
        txtSupplierName.requestFocus();
    }
    
    private boolean deletePayment() throws SQLException, 
            ClassNotFoundException {
        Connection con=DBConnection.getInstance().getConnection();
        
        con.setAutoCommit(false);
        String sql="DELETE FROM Payment WHERE pid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, tblPaymentData.getSelectionModel().getSelectedItem().getPid());
        boolean isDelete= ps.executeUpdate()>0;
       
        if(isDelete){
            sql="DELETE FROM AccountTransaction WHERE docId=?";
            ps=con.prepareStatement(sql);
            ps.setString(1, txtPayId.getText());
            isDelete=ps.executeUpdate()>0;
            if(isDelete){
                con.commit();
            }else{
                con.rollback();
            }
        }else{
            con.rollback();
        }
        
        con.setAutoCommit(true);
        return isDelete;
    }
    
    
    private void loadPaymentOptions() {
        comboPaymentType.getItems().addAll(
                "Cash",
                "Credit Card",
                "Cheque",
                "Bank Transfer"
        );
    }

    private ObservableList<PaymentTableModel> loadPaymentsByFields(String value) throws ClassNotFoundException, SQLException {
        ObservableList<PaymentTableModel> paymentList=FXCollections.
                observableArrayList();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT Payment.pid,Supplier.name,Payment.amount,Payment."
                + "dateOfPayment,Payment.pType,Payment.supplierInvoiceNumber FROM Payment INNER JOIN Supplier"
                + " ON Supplier.sid=Payment.sid"
                + " WHERE Payment.pid LIKE ? || "
                + "Supplier.name LIKE ? || Payment.amount"
                + " LIKE ? || Payment.dateOfPayment LIKE ? || Payment.pType LIKE ?"; 
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, "%"+value+"%");
        ps.setString(2, "%"+value+"%");
        ps.setString(3, "%"+value+"%");
        ps.setString(4, "%"+value+"%");
        ps.setString(5, "%"+value+"%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            paymentList.add(new PaymentTableModel(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(5),
                    rs.getString(4),
                    Double.parseDouble(rs.getString(3)),
                    rs.getString(6)
            ));
        }
    return paymentList;
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

    private boolean addPayment(Payment payment) throws SQLException, ClassNotFoundException {
        Connection con=DBConnection.getInstance().getConnection();
        String sql="INSERT INTO Payment values(?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, payment.getPid());
        ps.setString(2, payment.getSupID());
        ps.setDouble(3, payment.getAmount());
        ps.setString(4, payment.getpType());
        ps.setString(5, payment.getDateOfPayment());
        ps.setString(6, payment.getSupplierInvoiceNumber());
        return ps.executeUpdate()>0;
    }

    @FXML
    private void btnReset_onAction(ActionEvent event) {
        tblPaymentData.getSelectionModel().clearSelection();
        btnDelete.setDisable(true);
        btnAddPayment.setDisable(false);
        clearFields();
    }

    @FXML
    private void mainPane_OnMouseClicked(MouseEvent event) {
        tblPaymentData.getSelectionModel().clearSelection();
        btnDelete.setDisable(true);
    }
    
    
    public String getPaymentsForLastDay(String datePickerFrom, String datePickerTo) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT SUM(amount) FROM Payment WHERE dateOfPayment BETWEEN ? and ?");
        double value=0;
        ps.setString(1, datePickerFrom);
        ps.setString(2, datePickerTo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
                value=rs.getDouble(1);
        }
        String format = String.format("%,.2f", value);
        return format;
    }
    

    @FXML
    private void txtSupplierName_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtSupplierInvoiceNumbers.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtSupplierName);
    }

    @FXML
    private void txtSupplierName_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyLettersAndCharacters(event);
    }

    @FXML
    private void txtSupplierInvoiceNumbers_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtAmount.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtSupplierInvoiceNumbers);
    }

    @FXML
    private void txtAmount_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            comboPaymentType.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtAmount);
    }

    @FXML
    private void txtAmount_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
        
        
    }

    @FXML
    private void comboPaymentType_onKeyPressed(KeyEvent event) {
    }

    @FXML
    private void btnAddPayment_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnAddPayment.fire();
        }
        
    }

    private void printPayment(String pid) throws FileNotFoundException, JRException, ClassNotFoundException, SQLException {
           InputStream in=new FileInputStream("/home/shanil/NetBeansProjects/CarServiceStationProject/src/lk/r4enterprises/system/view/Reports/Payment.jrxml");
            JasperReport ja=JasperCompileManager.compileReport(in);
            Map<String,Object> para=new HashMap<>();
            para.put("PaymentID", pid);
            JasperPrint jp=JasperFillManager.fillReport(ja,para,DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jp,false);
    
    }

    
    
    
}
