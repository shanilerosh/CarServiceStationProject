/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import animatefx.animation.Shake;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
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
import lk.r4enterprises.system.model.Customer;
import lk.r4enterprises.system.model.CustomerReturn;
import lk.r4enterprises.system.model.Item;
import lk.r4enterprises.system.model.ReturnInwards;
import lk.r4enterprises.system.model.ReturnOutward;
import lk.r4enterprises.system.model.ReturnsTableModel;
import lk.r4enterprises.system.model.Supplier;
import lk.r4enterprises.system.model.SupplierReturn;
import lk.r4enterprises.system.view.AlertBox;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author shanil
 */
public class ReturnsController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    private TextField txtPartName;
    @FXML
    private TextField txtDocId;
    @FXML
    private ComboBox<String> comboxBoxReturn;
    @FXML
    private Button btnAddItem;
    @FXML
    private TableView<ReturnsTableModel> tblItemData;
    
    @FXML
    private TextField txtSearchItem;
    @FXML
    private Text txtInwardsOutwards;
    @FXML
    private TextField txtQuantity;
    @FXML
    private Text txtGrn1;
    private Text txtDate;
    @FXML
    private Text txtReturnNumber;
    @FXML
    private Text txtTotalValue;
    @FXML
    private TableColumn<ReturnsTableModel, String> colItemModel;
    @FXML
    private TableColumn<ReturnsTableModel, Double> colQty;
    @FXML
    private TableColumn<ReturnsTableModel, Double> colItemTotal;
    @FXML
    private TableColumn<ReturnsTableModel, Integer> colRowCount;
    @FXML
    private TableColumn<ReturnsTableModel, Double> colPrice;

    private int rowCount=1;
    @FXML
    private Button btnPlaceReturn;
    private Text txtTime;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtSupplierName;
    
    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    @FXML
    private TextField txtPrice;
    @FXML
    private Text txtQtyOnHand;
    @FXML
    private Button btnDelete;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colRowCount.setCellValueFactory(new PropertyValueFactory<>("rowCount"));
        colItemModel.setCellValueFactory(new PropertyValueFactory<>("itemModel")); 
        colPrice.setCellValueFactory(new PropertyValueFactory<>("returningPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("returnQuantity"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        disableFields();
        loadComboBox();
    }    

    @FXML
    private void btnAddItem_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        if(txtSearchItem.getText().isEmpty()){
            animateEmptyField(txtSearchItem);
        }else if(txtQuantity.getText().isEmpty()){
            animateEmptyField(txtSearchItem);
        }else if(txtPrice.getText().isEmpty()){
            animateEmptyField(txtPrice);
        }
        else{
        Item item = new ItemController().getItemFromModel(txtSearchItem.getText());
        
                if(!item.getCategory().equals("Serivce")){
                    if(comboxBoxReturn.getSelectionModel().isSelected(1)){
                        double qtyAvailble=item.getQuantityOnHand();
                        double returningQty=Double.parseDouble(txtQuantity.getText());
                        if(qtyAvailble<returningQty){
                            AlertBox.showErrorMessage("Error", "Your chosen quantity to be returned is greated than the existing quantity for the item "+item.getModel());
                        }else{
                        tblItemData.getItems().add(new ReturnsTableModel(rowCount++,
                        item.getModel(),
                        Double.parseDouble(txtPrice.getText()),
                        Double.parseDouble(txtQuantity.getText()),
                        Double.parseDouble(txtPrice.getText())*Double.parseDouble(txtQuantity.getText())
                        ));
                        }
                    }else{
                        tblItemData.getItems().add(new ReturnsTableModel(rowCount++,
                        item.getModel(),
                        Double.parseDouble(txtPrice.getText()),
                        Double.parseDouble(txtQuantity.getText()),
                        Double.parseDouble(txtPrice.getText())*Double.parseDouble(txtQuantity.getText())
                        ));
                    }
                }else{
                    AlertBox.showErrorMessage("Error", "This is a service Item.Not applicable for returns");
                }
        }
        
        txtSearchItem.clear();
        txtPrice.clear();
        txtQuantity.clear();
        txtQtyOnHand.setText("");
        txtSearchItem.requestFocus();
        btnDelete.setDisable(true);
        setTotalValue();
    }

    private void animateEmptyField(TextField textField) {
        new Shake(textField).play();
        textField.setStyle("-fx-border-color:#ff0000");
        pause.setOnFinished(evt -> textField.setStyle("-fx-border-color:#097541"));
        pause.play();
    }
    
    
    
    
    @FXML
    private void tblItemData_OnMouseClicked(MouseEvent event) {
        if(tblItemData.getSelectionModel().getSelectedIndex()>=0){
            btnDelete.setDisable(false);
        }else{
            AlertBox.showErrorMessage("Error", "Select a correct Item from the list");
        }
    }

    @FXML
    private void txtsearchItems_OnKeyReleased(KeyEvent event) {
    }



    @FXML
    private void mainPane_OnMouseClicked(MouseEvent event) {
        tblItemData.getSelectionModel().clearSelection();
        btnDelete.setDisable(true);
    }

    @FXML
    private void comboxBoxReturn_onAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        if(comboxBoxReturn.getSelectionModel().isSelected(0)){
            TextFields.bindAutoCompletion(txtCustomerName, new CustomerController().getAllCustomerNames());
            setCustomerReturnsId();
            enableFields();
            txtSupplierName.setDisable(true);
        }else{
            TextFields.bindAutoCompletion(txtSupplierName, new SupplierController().getAllSupplierNames());
            setSupplierReturnsId();
            enableFields();
            txtCustomerName.setDisable(true);
        }
        
        
    }

    private void loadComboBox() {
        comboxBoxReturn.getItems().setAll("Return Inwards","Return Outwards");
        
    }

    

    private void disableFields() {
        txtCustomerName.setDisable(true);
        txtSupplierName.setDisable(true);
        txtDocId.setDisable(true);
        txtSearchItem.setDisable(true);
        txtQuantity.setDisable(true);
        btnAddItem.setDisable(true);
        btnPlaceReturn.setDisable(true);
        btnDelete.setDisable(true);
    }
    
    private void enableFields() throws ClassNotFoundException, SQLException {
        txtCustomerName.setDisable(false);
        txtSupplierName.setDisable(false);
        txtDocId.setDisable(false);
        txtSearchItem.setDisable(false);
        txtQuantity.setDisable(false);
        btnAddItem.setDisable(false);
        btnPlaceReturn.setDisable(false);
        TextFields.bindAutoCompletion(txtSearchItem, new ItemController().getAllItemsByName());
    }
    
    private void clearFields(){
        txtCustomerName.clear();
        txtSupplierName.clear();
        txtDocId.clear();
        txtSearchItem.clear();
        txtQtyOnHand.setText("");
        txtQuantity.clear();
        txtPrice.clear();
        txtReturnNumber.setText("");
        txtInwardsOutwards.setText("");
        tblItemData.getItems().clear();
        comboxBoxReturn.requestFocus();
        txtTotalValue.setText("");
    }


    @FXML
    private void btnPlaceReturn_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
          Connection connection = DBConnection.getInstance().getConnection();
          connection.setAutoCommit(false);
        if(comboxBoxReturn.getSelectionModel().isSelected(0)){
            if(txtCustomerName.getText().isEmpty()){
                animateEmptyField(txtCustomerName);
            }
            else{
                Customer customer = new CustomerController().getCustomerFromName(txtCustomerName.getText());
                
                if(customer==null){
                    AlertBox.showErrorMessage("Error", txtCustomerName.getText()+" doesn't exist.Try again");
                }
                else{
                    ArrayList<ReturnInwards> list=new ArrayList<>();
                for (int i = 0; i < tblItemData.getItems().size(); i++) {
                    list.add(new ReturnInwards(
                            txtReturnNumber.getText(),
                            new ItemController().getItemFromModel(tblItemData.getItems().get(i).getItemModel()).getIid(),
                            tblItemData.getItems().get(i).getReturningPrice(),
                            tblItemData.getItems().get(i).getReturnQuantity()
                    ));
                }
                
                CustomerReturn customerReturn = new CustomerReturn(
                        txtReturnNumber.getText(),
                        customer.getCid(),
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                        txtDocId.getText(),
                        Double.parseDouble(txtTotalValue.getText()),
                        list
                );
                
                
                boolean upateQty=new ItemController().updateQtyForReturnInwards(list);
                boolean isSave = new CustomerReturnsConroller().addCustomerReturns(customerReturn);
                
                if(isSave && upateQty){
                   connection.commit();
                   AlertBox.showDisplayMessage("Success", txtReturnNumber.getText()+" has been created");
                }else{
                    connection.rollback();
                   AlertBox.showErrorMessage("Failure", "Try again");
                }
                
                }
                
                
            }
            
           
        }else if(comboxBoxReturn.getSelectionModel().isSelected(1)){
            if(txtSupplierName.getText().isEmpty()){
                animateEmptyField(txtSupplierName);
            }else{
                
                Supplier supplier = new SupplierController().getSupplierFromName(txtSupplierName.getText());
                
                if(supplier==null){
                 AlertBox.showErrorMessage("Error", txtSupplierName.getText()+" doens;t exist");
                }else{
                    ArrayList<ReturnOutward> list=new ArrayList<>();
                for (int i = 0; i < tblItemData.getItems().size(); i++) {
                    list.add(new ReturnOutward(
                            txtReturnNumber.getText(),
                            new ItemController().getItemFromModel(tblItemData.getItems().get(i).getItemModel()).getIid(),
                            tblItemData.getItems().get(i).getReturningPrice(),
                            tblItemData.getItems().get(i).getReturnQuantity()
                    ));
                }
                
                SupplierReturn supplierReturn = new SupplierReturn(
                        txtReturnNumber.getText(),
                        new SupplierController().getSupplierFromName(txtSupplierName.getText()).getSid(),
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                        txtDocId.getText(),
                        Double.parseDouble(txtTotalValue.getText()),
                        list
                );
                boolean updateQty=new ItemController().updateQtyForReturnOutwards(list);
                boolean isSave = new SupplierReturnsController().addSupplierReturns(supplierReturn);
            
                if(isSave && updateQty){
                   connection.commit();
                   AlertBox.showDisplayMessage("Success", txtReturnNumber.getText()+" has been created");
                }else{
                    connection.rollback();
                   AlertBox.showErrorMessage("Failure", "Try again");
                }
                }
        }
        }
        
        clearFields();
        disableFields();
        connection.setAutoCommit(true);
    }
    

    public void setCustomerReturnsId() throws ClassNotFoundException, SQLException{
        txtInwardsOutwards.setText("Inwards - ");
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT creturnid FROM CustomerReturn ORDER BY creturnid DESC LIMIT 1";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String returnId=null;
        if(rs.next()){
            returnId=rs.getString(1);
        }
       
        if(returnId!=null){
            String[] temp=returnId.split("CRI-");
            int tempNumber=Integer.parseInt(temp[1]);
            tempNumber++;
            
            if(tempNumber<10){
                txtReturnNumber.setText("CRI-00"+tempNumber);
            }else if(tempNumber<100){
                txtReturnNumber.setText("CRI-0"+tempNumber);
            }else{
                txtReturnNumber.setText("CRI"+tempNumber);
            }
        }else{
            txtReturnNumber.setText("CRI-001");
        }
        disableFields();
    }
    
    
    public void setSupplierReturnsId() throws ClassNotFoundException, SQLException{
        txtInwardsOutwards.setText("Outwards - ");
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT sreturnid FROM SupplierReturn ORDER BY sreturnid DESC LIMIT 1";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String returnId=null;
        if(rs.next()){
            returnId=rs.getString(1);
        }
       
        if(returnId!=null){
            String[] temp=returnId.split("SRI-");
            int tempNumber=Integer.parseInt(temp[1]);
            tempNumber++;
            
            if(tempNumber<10){
                txtReturnNumber.setText("SRI-00"+tempNumber);
            }else if(tempNumber<100){
                txtReturnNumber.setText("SRI-0"+tempNumber);
            }else{
                txtReturnNumber.setText("SRI"+tempNumber);
            }
        }else{
            txtReturnNumber.setText("SRI-001");
        }
        disableFields();
    }

    @FXML
    private void txtQuantity_onKeyPressed(KeyEvent event) throws ClassNotFoundException, SQLException {
        Item item= new ItemController().getItemFromModel(txtSearchItem.getText());
        if(item==null){
            AlertBox.showErrorMessage("Error", txtSearchItem.getText()+" doesn't exist");
        }else if(item.getModel().equals("Service")){
            txtQtyOnHand.setText("Service");
        }else{
            txtQtyOnHand.setText(Double.toString(item.getQuantityOnHand()));
        }
        
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtPrice.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtQuantity);
        
        
    }

    private void setTotalValue() {
        double total=0;
        for (int i = 0; i < tblItemData.getItems().size(); i++) {
            total+=tblItemData.getItems().get(i).getTotalAmount();
        }
        txtTotalValue.setText(Double.toString(total));
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) {
        int selectedIndex = tblItemData.getSelectionModel().getSelectedIndex();
        tblItemData.getItems().remove(selectedIndex);
        setTotalValue();
    }

    @FXML
    private void txtCustomerName_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtDocId.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtCustomerName);
    }

    @FXML
    private void txtCustomerName_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyLettersAndCharacters(event);
    }

    @FXML
    private void txtDocId_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtSearchItem.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtDocId);
    }

    @FXML
    private void comboxBoxReturn_onKeyPressed(KeyEvent event) {
    }

    @FXML
    private void txtSupplierName_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtDocId.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtSupplierName);
    }

    @FXML
    private void txtSupplierName_onKeyReleased(KeyEvent event) {
    }

    @FXML
    private void btnAddItem_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnAddItem.fire();
        }
        
    }

    @FXML
    private void txtsearchItems_OnKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtQuantity.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtSearchItem);
    }

    @FXML
    private void txtQuantity_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
    }

    @FXML
    private void txtPrice_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnAddItem.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtPrice);
    }

    @FXML
    private void txtPrice_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbersAndDecimal(event);
    }
}
