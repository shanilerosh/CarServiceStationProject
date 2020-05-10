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
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.Item;
import lk.r4enterprises.system.model.OrderDetail;
import lk.r4enterprises.system.model.ReturnInwards;
import lk.r4enterprises.system.model.ReturnOutward;
import lk.r4enterprises.system.model.SupplierDetail;
import lk.r4enterprises.system.model.User;
import lk.r4enterprises.system.view.AlertBox;
import lk.r4enterprises.system.view.AnimateComponent;

/**
 *
 * @author shanil
 */
public class ItemController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtUnitPrice;
    @FXML
    private TextField txtItemOnHand;
    @FXML
    private TextField txtModel;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<Item> tblItemData;
    @FXML
    private TableColumn<Item, String> colName;
    @FXML
    private TextField txtsearchItems;
    @FXML
    private Text txtIid;
    @FXML
    private TableColumn<Item, String> colIid;
    @FXML
    private TableColumn<Item, String> colCategory;
    @FXML
    private TableColumn<Item, Double> colUnitPrice;
    @FXML
    private TableColumn<Item, Integer> colQtyOnHand;
    @FXML
    private TableColumn<Item, String> colModel;
    @FXML
    private ComboBox<String> comboCategory;
    
    private User user;
    
    @FXML
    private void btnCreate_OnAction(ActionEvent event) {
        if(txtName.getText().isEmpty()){
            AnimateComponent.animateEmptyField(txtName);
        }else if(txtUnitPrice.getText().isEmpty()){
            AnimateComponent.animateEmptyField(txtUnitPrice);
        }
        else{
        try {
            if(addItem()){
                tblItemData.getItems().add(
                new Item(
                    txtIid.getText(),
                    txtName.getText(),
                    comboCategory.getSelectionModel().getSelectedItem(),
                    Double.parseDouble(txtUnitPrice.getText()), 
                    txtModel.getText(),
                    Integer.parseInt(txtItemOnHand.getText())
               ));
                AlertBox.showDisplayMessage("Successful", txtName.getText()
                        +"added Succesfully");
                loadItems();
                clearFielsAndLoadAgain();
            }else{
                AlertBox.showErrorMessage("Error", "Try again");
                loadItems();
            }
            
        } catch (Exception ex) {
                AlertBox.showErrorMessage("Error", ex.toString());
        }
        }
    }

    public void setUser(User user) {
        this.user = user;
        if(!user.getRole().equals("Admin")){
               btnDelete.setVisible(false);
               btnUpdate.setVisible(false);
        }
        
        
    }

    

    @FXML
    private void btnUpdate_OnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
             if(txtName.getText().isEmpty()){
                 AnimateComponent.animateEmptyField(txtName);
             }else if(txtUnitPrice.getText().isEmpty()){
                AnimateComponent.animateEmptyField(txtUnitPrice);
             }
            else{
                updateItem();
                AlertBox.showDisplayMessage("Sucessful", txtIid.getText()+
                        " succesfully updated.");
                loadItems();
                clearFielsAndLoadAgain();
            }
    }
    
    public boolean updateItem() throws SQLException, ClassNotFoundException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="UPDATE Item SET name=?,category=?,unitprice=?,model=?,QtyOnHad=? WHERE iid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, txtName.getText());
        ps.setString(2, comboCategory.getSelectionModel().getSelectedItem());
        ps.setDouble(3, Double.parseDouble(txtUnitPrice.getText()));
        ps.setString(4, txtModel.getText());
        ps.setInt(5, Integer.parseInt(txtItemOnHand.getText()));
        ps.setString(6, txtIid.getText());
        return ps.executeUpdate()>0;

    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
            if(deleteItem()){
                AlertBox.showDisplayMessage("Sucessful", txtIid.getText()+
                        " succesfully deleted.");
                loadItems();
                clearFielsAndLoadAgain();
            }else{
                 AlertBox.showErrorMessage("Error", "Try again with correct"
                        + " Selection");
            }
    }
    
    private boolean deleteItem() throws SQLException, 
            ClassNotFoundException {
        Connection con=DBConnection.getInstance().getConnection();
        String sql="DELETE FROM Item WHERE iid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, txtIid.getText());
        return ps.executeUpdate()>0;
    }
    
    
    

    @FXML
    private void txtsearchItems_OnKeyReleased(KeyEvent event) throws SQLException, ClassNotFoundException {
        String value=txtsearchItems.getText();
        tblItemData.setItems(loadItemByFields(value));
    }
    
    private ObservableList<Item> loadItemByFields(String value) throws
            SQLException, ClassNotFoundException {
        ObservableList<Item> itemList=FXCollections.
                observableArrayList();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Item WHERE name LIKE ? || category"
                + " LIKE ? || unitprice LIKE ? || model LIKE ? || QtyOnHad LIKE ?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, "%"+value+"%");
        ps.setString(2, "%"+value+"%");
        ps.setString(3, "%"+value+"%");
        ps.setString(4, "%"+value+"%");
        ps.setString(5, "%"+value+"%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            itemList.add(new Item(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getDouble(4),
                    rs.getString(5),
                    rs.getInt(6)
            ));
        }
    return itemList;
    }

    @FXML
    private void mainPane_OnMouseClicked(MouseEvent event) throws ClassNotFoundException, SQLException {
        tblItemData.getSelectionModel().clearSelection();
        loadItems();
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        
    }
    
    
    private void clearFielsAndLoadAgain() throws ClassNotFoundException,
            SQLException {
        setItemId();
        txtName.clear();
        txtModel.clear();
        txtItemOnHand.clear();
        txtUnitPrice.clear();
        loadItems();
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }
    
    public List<String> getAllItemsByName() throws ClassNotFoundException, SQLException{
        List<String> itemList=new ArrayList<>();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT model FROM Item";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            itemList.add(new String(
                    rs.getString(1)
            ));
        }
    return itemList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colIid.setCellValueFactory(new PropertyValueFactory<>("iid"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("quantityOnHand"));
        try {
            loadItems();
            loadComboBox();
            clearFielsAndLoadAgain();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadItems() throws ClassNotFoundException, SQLException{
        tblItemData.setItems(getAllItems());
        tblItemData.getColumns().clear();
        tblItemData.getColumns().addAll(colIid,colName,colCategory,colUnitPrice,colModel,colQtyOnHand);
        setItemId();
    }
    
    public ObservableList<Item> getAllItems() throws ClassNotFoundException, SQLException{
        ObservableList<Item> itemList=FXCollections.
                observableArrayList();
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Item";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            itemList.add(new Item(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getDouble(4),
                    rs.getString(5),
                    rs.getInt(6)
            ));
        }
        
    return itemList;
    }
    
    public Item getItemFromModel(String model) throws ClassNotFoundException, SQLException{
        Item foundItem=null;
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Item WHERE model=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, model);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            foundItem=new Item(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getDouble(4),
                    rs.getString(5),
                    rs.getInt(6)
            );
        }
        System.out.println(foundItem.getName());
        return foundItem;
    }
    
    public Item getItemFromIid(String iid) throws ClassNotFoundException, SQLException{
        Item foundItem=null;
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM Item WHERE iid=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, iid);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            foundItem=new Item(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getDouble(4),
                    rs.getString(5),
                    rs.getInt(6)
            );
        }
        return foundItem;
    }
    
    
    
    public boolean addItem() throws ClassNotFoundException,
         SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="INSERT INTO Item values(?,?,?,?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, txtIid.getText());
        ps.setString(2, txtName.getText());
        ps.setString(3, comboCategory.getSelectionModel().getSelectedItem());
        ps.setDouble(4, Double.parseDouble(txtUnitPrice.getText()));
        ps.setString(5, comboCategory.getSelectionModel().getSelectedItem());
        ps.setInt(6, Integer.parseInt(txtItemOnHand.getText()));
        return ps.executeUpdate()>0;
    }
    
    public void setItemId() throws ClassNotFoundException, SQLException{
        Connection con=DBConnection.getInstance().getConnection();
        String sql="SELECT iid FROM Item ORDER BY iid DESC LIMIT 1";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String iid=null;
        if(rs.next()){
            iid=rs.getString(1);
        }
       
        if(iid!=null){
            String[] temp=iid.split("I");
            int tempNumber=Integer.parseInt(temp[1]);
            tempNumber++;
            
            if(tempNumber<10){
                txtIid.setText("I00"+tempNumber);
            }else if(tempNumber<100){
                txtIid.setText("I0"+tempNumber);
            }else{
                txtIid.setText("I"+tempNumber);
            }
        }else{
            txtIid.setText("I001");
        }
        
        //Clearing the fields
        txtName.clear();
        txtModel.clear();
        txtUnitPrice.clear();
        txtItemOnHand.clear();
    }
    
    public boolean checkItemExist(String model) throws ClassNotFoundException, SQLException{
        ObservableList<Item> allItems = getAllItems();
        for (int i = 0; i < allItems.size(); i++) { 
            if(allItems.get(i).getModel().equals(model)){
                return true;
            }
        }
        return false;
    }
    
    
    

    @FXML
    private void tblItemData_OnMouseClicked(MouseEvent event) {
        if(tblItemData.getSelectionModel().getSelectedIndex()>=0){
        btnDelete.setDisable(false);
        btnUpdate.setDisable(false);
        Item selected=tblItemData.getSelectionModel().getSelectedItem();
        txtIid.setText(selected.getIid());
        comboCategory.getSelectionModel().select(selected.getCategory());
        txtModel.setText(selected.getModel());
        txtName.setText(selected.getName());
        txtUnitPrice.setText(Double.toString(selected.getUnitPrice()));
        txtItemOnHand.setText(Integer.toString(selected.getQuantityOnHand()));
        }
        else{
            AlertBox.showErrorMessage("Error", "Selection on the table is not valid");
        }
    }
    
    public boolean updateQty(ArrayList<OrderDetail> list) throws ClassNotFoundException, SQLException{
        for(OrderDetail itemDetail:list){
            PreparedStatement ps = DBConnection.getInstance().getConnection().
           prepareStatement("UPDATE Item SET QtyOnHad=(QtyOnHad-?) WHERE iid=?");
            ps.setDouble(1, itemDetail.getQty());
            ps.setString(2, itemDetail.getIid());
            
            if(!(ps.executeUpdate()>0)){return false;}
        }
        return true;
    }
    
    public boolean updateQtyForReturnInwards(ArrayList<ReturnInwards> list) throws ClassNotFoundException, SQLException{
        for(ReturnInwards itemDetail:list){
            PreparedStatement ps = DBConnection.getInstance().getConnection().
           prepareStatement("UPDATE Item SET QtyOnHad=(QtyOnHad+?) WHERE iid=?");
            ps.setDouble(1, itemDetail.getReturnQty());
            ps.setString(2, itemDetail.getIid());
            if(!(ps.executeUpdate()>0)){return false;}
        }
        return true;
    }
    
    
    public boolean updateQtyBasedOnGRN(ArrayList<SupplierDetail> list) throws ClassNotFoundException, SQLException{
        for(SupplierDetail itemDetail:list){
            PreparedStatement ps = DBConnection.getInstance().getConnection().
           prepareStatement("UPDATE Item SET QtyOnHad=(QtyOnHad+?) WHERE iid=?");
            ps.setDouble(1, itemDetail.getQuantity());
            ps.setString(2, itemDetail.getIid());
            if(!(ps.executeUpdate()>0)){return false;}
        }
        return true;
    }

    public boolean updateQtyForReturnOutwards(ArrayList<ReturnOutward> list) throws ClassNotFoundException, SQLException {
        for(ReturnOutward itemDetail:list){
            PreparedStatement ps = DBConnection.getInstance().getConnection().
           prepareStatement("UPDATE Item SET QtyOnHad=(QtyOnHad-?) WHERE iid=?");
            ps.setDouble(1, itemDetail.getReturnQty());
            ps.setString(2, itemDetail.getIid());
            if(!(ps.executeUpdate()>0)){return false;}
        }
        return true;
    }

    @FXML
    private void txtName_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            comboCategory.requestFocus();
        }
        
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtName);
    }

    private void txtName_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyLettersAndCharacters(event);
    }

    @FXML
    private void txtUnitPrice_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtModel.requestFocus();
        }
        
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtUnitPrice);
    }

    @FXML
    private void txtUnitPrice_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
    }

    @FXML
    private void txtItemOnHand_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnCreate.requestFocus();
        }
        
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtItemOnHand);
    }

    @FXML
    private void txtItemOnHand_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
    }

    @FXML
    private void txtModel_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtItemOnHand.requestFocus();
        }
        
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtModel);
    }

    @FXML
    private void comboCategory_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            txtUnitPrice.requestFocus();
        }
        
    }

    @FXML
    private void btnCreate_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnCreate.fire();
        }
        
    }


    private void loadComboBox() {
        comboCategory.getItems().addAll(
                "Shop",
                "Service"
        );
    }

    
}
