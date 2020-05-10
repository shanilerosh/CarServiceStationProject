/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.Car;
import lk.r4enterprises.system.model.Customer;
import lk.r4enterprises.system.model.Item;
import lk.r4enterprises.system.model.OrderDetail;
import lk.r4enterprises.system.model.Order;
import lk.r4enterprises.system.model.OrderTableModel;
import lk.r4enterprises.system.model.ReportTableModel;
import lk.r4enterprises.system.view.AlertBox;
import lk.r4enterprises.system.view.AnimateComponent;
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
public class OrderController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtMobile;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtVehicleRegistrationNumber;
    @FXML
    private TextField txtModel;
    @FXML
    private TableView<OrderTableModel> tblOrder;
    @FXML
    private TableColumn<OrderTableModel, Integer> colRowCount;
    @FXML
    private TableColumn<OrderTableModel, String> colItemName;
    @FXML
    private TableColumn<OrderTableModel, Double> colUnitPrice;
    @FXML
    private TableColumn<OrderTableModel, Integer> colQuantity;
//    @FXML
//    private TableColumn<Item, double> colTotal;
//    @FXML
    private Text txtOrderDate;
    @FXML
    private TableColumn<OrderTableModel, String> colItemModel;
    @FXML
    private TextField txtQuantity;
    @FXML
    private Text txtQuantityOnHand;
    @FXML
    private TableColumn<OrderTableModel, Double> colTotal;
    @FXML
    private TextField txtItemModel;
    private int counter = 1;

    @FXML
    private Button btnAddItem;
    @FXML
    private Text txtQuantityOnHand1;
    @FXML
    private Text txtTotalValue;
    @FXML
    private Text txtNetValue;
    @FXML
    private TextField txtDiscount;
    @FXML
    private Button btnPlaceOrder;
    @FXML
    private Text txtOid;
    private Text txtOrderTime;
    @FXML
    private Button btnDiscount;
    @FXML
    private Button btnDelete;
    @FXML
    private AnchorPane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //colRowCount.setCellFactory(new PropertyValueFactory<>(""));
        colRowCount.setCellValueFactory(new PropertyValueFactory<>("counter"));
        colItemModel.setCellValueFactory(new PropertyValueFactory<>("itemModel"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("itemTotal"));
        try {
            TextFields.bindAutoCompletion(txtName, new CustomerController().getAllCustomerNames());
            TextFields.bindAutoCompletion(txtVehicleRegistrationNumber, new CarController().getAllCarRegNumbers());
            TextFields.bindAutoCompletion(txtItemModel, new ItemController().getAllItemsByName());
            loadTable();
            setOrderId();
            clearAllFields();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        btnAddItem.setDisable(true);

    }

    private void loadTable() {
        tblOrder.getColumns().clear();
        tblOrder.getColumns().addAll(colRowCount, colItemModel, colItemName, colQuantity, colUnitPrice, colTotal);
    }

    @FXML
    private void txtName_OnKeyPressed(KeyEvent event) throws SQLException, ClassNotFoundException {
        if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
            Customer foundCustomer = new CustomerController().getCustomerFromName(txtName.getText());
            if (foundCustomer != null) {
                txtMobile.setText(foundCustomer.getMobileNumber());
                txtAddress.setText(foundCustomer.getAddress());
                txtVehicleRegistrationNumber.requestFocus();
            } else {
                txtMobile.requestFocus();
            }
        } else if (event.getCode() == KeyCode.ESCAPE) {
            txtName.clear();
        }

    }

    @FXML
    private void txtVehicleRegistrationNumber_OnKeyPressed(KeyEvent event) throws SQLException, ClassNotFoundException {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            Car foundCar = new CarController().getCarFromRegistration(txtVehicleRegistrationNumber.getText());
            if (foundCar != null) {
                txtModel.setText(foundCar.getModelNumber());
                txtItemModel.requestFocus();
            }
            txtModel.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtVehicleRegistrationNumber);
    }

    @FXML
    private void txtQuantity_OnKeyReleased(KeyEvent event) throws ClassNotFoundException, SQLException {
        if (!txtItemModel.getText().isEmpty() && new ItemController().checkItemExist(txtItemModel.getText())) {
            Item item = new ItemController().getItemFromModel(txtItemModel.getText());
            
            if(item.getCategory().equals("Service")){
                txtQuantityOnHand.setText("Service");
                btnAddItem.setDisable(false);
            }else{
                int qtyOnHand=item.getQuantityOnHand();
                txtQuantityOnHand.setText(Integer.toString(qtyOnHand));
                btnAddItem.setDisable(false);
            }
            
        } else {
            AlertBox.showErrorMessage("Error", "Item Model is wrong");
            txtQuantity.clear();
            txtItemModel.requestFocus();

        }

    }

    @FXML
    private void btnAddItem_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (txtItemModel.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtItemModel);
        } else if (txtQuantity.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtQuantity);
        } else {
            Item item = new ItemController().getItemFromModel(txtItemModel.getText());

            if (item == null) {
                AlertBox.showErrorMessage("Error", txtItemModel.getText() + " doesn't Exist as a valid Item");
            } else {
                int quantityOnHand = item.getQuantityOnHand();
                int requested = Integer.parseInt(txtQuantity.getText());

                if (quantityOnHand < requested) {
                    AlertBox.showErrorMessage("Error", "Requested stock is out of "
                            + "quantity.Please check the existing balance and place the order");
                    txtQuantity.clear();
                    txtQuantity.requestFocus();
                } else {
                    String modelName = txtItemModel.getText();
                    int rowIndex = isAlreadyExist(modelName);
                    int qty = Integer.parseInt(txtQuantity.getText());
                    if (rowIndex == -1) {
                        tblOrder.getItems().add(new OrderTableModel(counter++, item.getModel(), item.getName(), item.getUnitPrice(), qty, (qty * item.getUnitPrice())));
                    } else {
                        qty += tblOrder.getItems().get(rowIndex).getQuantity();
                        tblOrder.getItems().get(rowIndex).setQuantity(qty);
                        loadTable();
                    }
                }
                setTotalValues();
                txtItemModel.clear();
                txtQuantity.clear();
                txtQuantityOnHand.setText("");
                txtItemModel.requestFocus();
            }
        }
    }

    private int isAlreadyExist(String name) {
        for (int i = 0; i < tblOrder.getItems().size(); i++) {
            String temp = tblOrder.getItems().get(i).getItemModel();
            if (name.equalsIgnoreCase(temp)) {
                return i;
            }

        }
        return -1;
    }

    private double qtyOnHand() {
        double userQty = Double.parseDouble(txtQuantity.getText());
        double stock = userQty = Double.parseDouble(txtQuantityOnHand.getText());
        return stock - userQty;
    }

    private OrderTableModel getTableModel(Item temp) throws ClassNotFoundException, SQLException {
        int requestedQuantity = Integer.parseInt(txtQuantity.getText());
        OrderTableModel ol = new OrderTableModel(counter++, temp.getModel(), temp.getName(), temp.getUnitPrice(), requestedQuantity, (requestedQuantity * temp.getUnitPrice()));
        return ol;
    }

    public void setTotalValues() {
        double total = 0;
        for (int i = 0; i < tblOrder.getItems().size(); i++) {
            total += tblOrder.getItems().get(i).getItemTotal();
        }
        txtTotalValue.setText(Double.toString(total));
        if (txtDiscount.getText().isEmpty()) {
            txtNetValue.setText(Double.toString(total));
        }
    }

    private double getTotal(){
        return Double.parseDouble(txtTotalValue.getText());
    }
    
    private void btnDiscount_OnAction(ActionEvent event) {
        if (!txtDiscount.getText().isEmpty()) {
            double rate = 100 - Double.parseDouble(txtDiscount.getText());
            double netTotal = (rate * Double.parseDouble(txtTotalValue.getText())) / 100;
            txtNetValue.setText(Double.toString(netTotal));
        } else {
            AlertBox.showErrorMessage("Error", "Input a Discount % in order to use");
        }
    }

    @FXML
    private void btnPlaceOrder_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException, JRException, FileNotFoundException {
        boolean printCheck=true;
        String orderId = txtOid.getText();
        String orderDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        Car car = null;
        Customer customer = null;
        boolean isSave = false;
        Connection con = DBConnection.getInstance().getConnection();
        con.setAutoCommit(false);

        if (txtName.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtName);
        } else if (txtMobile.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtMobile);
        } else if (txtAddress.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtAddress);
        } else if (txtVehicleRegistrationNumber.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtVehicleRegistrationNumber);
        } else if (txtModel.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtModel);
        } else if (tblOrder.getItems().isEmpty()) {
            AnimateComponent.animateEmptyField(tblOrder);
        }
        else {
            customer = new CustomerController().getCustomerFromName(txtName.getText());
            if (customer == null) {
                customer = new Customer(new CustomerController().getLatestCid(), txtAddress.getText(), txtName.getText(), txtMobile.getText());
                isSave = new CustomerController().addCustomer(customer);
            } else {
                customer.setAddress(txtAddress.getText());
                customer.setMobileNumber(txtMobile.getText());
                isSave = new CustomerController().updateCustomerFromName(customer);
            }

            if (!isSave) {
                con.rollback();
            } else {
                car = new CarController().getCarFromRegistration(txtVehicleRegistrationNumber.getText());
                if (car == null) {
                    car = new Car(new CarController().getLatestCrid(),
                            txtVehicleRegistrationNumber.getText(), txtModel.getText(), customer.getCid());
                    isSave = new CarController().addCar(car);
                } else {
                    isSave = new CarController().updateCar(car);
                }
            }

            if (isSave) {
                ArrayList<OrderDetail> list = new ArrayList<>();

                for (int i = 0; i < tblOrder.getItems().size(); i++) {
                    String itemCode = new ItemController().getItemFromModel(
                            tblOrder.getItems().get(i).getItemModel()).getIid();
                    double qty = tblOrder.getItems().get(i).getQuantity();
                    double price = tblOrder.getItems().get(i).getUnitPrice();
                    
                    Item item = new ItemController().getItemFromIid(itemCode);

                    if(!item.getCategory().equals("Service")){
                    list.add(new OrderDetail(itemCode, orderId, qty, price));
                    }
                }

                double discount = txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText());
                Order order = new Order(
                        orderId,
                        orderDate,
                        discount,
                        Double.parseDouble(txtNetValue.getText()),
                        customer.getCid(),
                        car.getCrid(),
                        list
                );

                isSave = placeOrder(order);

                if (isSave) {
                    printCheck=AlertBox.showConfMessage(order.getOid()+" has been created successfully.Do you want to print the Order?", "Sucess,Proceed to print?");
                    con.commit();
                    clearAllFields();
                    //setOrderId();
                } else {
                    AlertBox.showErrorMessage("Error", order.getOid() + " has not been created");
                    con.rollback();
                }

                con.setAutoCommit(true);
                if(printCheck){
                    printOrder(order.getOid()); 
                }
                
                setOrderId();
            }
        }

    }

    private boolean placeOrder(Order order) throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO Order_ VALUES(?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, order.getOid());
        stm.setDouble(2, order.getDiscount());
        stm.setDouble(3, order.getAmount());
        stm.setString(4, order.getDate());
        stm.setString(5, order.getCustomerId());
        stm.setString(6, order.getCarId());

        boolean isAdded = stm.executeUpdate() > 0;

        if (isAdded) {
            boolean isAddedDetails = new OrderDetailController().addItemDetail(order.getList());

            if (isAddedDetails) {
                boolean isUpdted = new ItemController().updateQty(order.getList());

                if (isUpdted) {
                    return true;
                }

            }
        }
        return false;

    }

    public void setOrderId() throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT oid FROM Order_ ORDER BY oid DESC LIMIT 1";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String ordId = null;
        if (rs.next()) {
            ordId = rs.getString(1);
        }

        if (ordId != null) {
            String[] temp = ordId.split("O");
            int tempNumber = Integer.parseInt(temp[1]);
            tempNumber++;

            if (tempNumber < 10) {
                txtOid.setText("O00" + tempNumber);
            } else if (tempNumber < 100) {
                txtOid.setText("O0" + tempNumber);
            } else {
                txtOid.setText("O" + tempNumber);
            }
        } else {
            txtOid.setText("O001");
        }
    }

    private void clearAllFields() {
        txtAddress.clear();
        txtDiscount.clear();
        txtItemModel.clear();
        txtVehicleRegistrationNumber.clear();
        txtMobile.clear();
        txtModel.clear();
        txtName.clear();
        txtNetValue.setText("0.00");
        txtQuantityOnHand.setText("0");
        tblOrder.getItems().clear();
        txtName.requestFocus();
        btnDelete.setDisable(true);
    }

    
    @FXML
    private void txtName_OnKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyLettersAndCharacters(event);
    }

    @FXML
    private void txtMobile_OnKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            txtAddress.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtMobile);
    }

    @FXML
    private void txtMobile_OnKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
    }

    @FXML
    private void txtAddress_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            txtVehicleRegistrationNumber.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtAddress);
    }

    @FXML
    private void txtModel_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            txtItemModel.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtMobile);
    }

    @FXML
    private void txtQuantity_OnKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            btnAddItem.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtQuantity);

    }

    @FXML
    private void txtQuantity_OnKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
    }

    @FXML
    private void btnAddItem_OnKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            btnAddItem.fire();
        }

    }

    @FXML
    private void txtDiscount_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            btnDiscount.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtDiscount);
    }

    @FXML
    private void txtDiscount_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
        if(txtDiscount.getText().length()>1){
            event.consume();
        }   

    }

    @FXML
    private void btnPlaceOrder_OnKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            btnPlaceOrder.fire();
        }
    }

    @FXML
    private void txtItemModel_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            txtQuantity.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtItemModel);
    }

    ObservableList<ReportTableModel> getOrdersByDates(String datePickerFrom, String datePickerTo) throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Order_ WHERE dateOfOrder BETWEEN ? and ?");
        ps.setString(1, datePickerFrom);
        ps.setString(2, datePickerTo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new ReportTableModel(
                    rs.getString(1),
                    new CustomerController().getCustomerNameFromId(rs.getString(5)),
                    rs.getString(4),
                    String.format("%,.2f", rs.getDouble(3)),
                    String.format("%,.1f", rs.getDouble(2))
            ));
        }
        return list;
    }
    
    ObservableList<ReportTableModel> getOrdersCountByDates(String datePickerFrom, String datePickerTo) throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT Order_.dateOfOrder,SUM(OrderDetail.quantity) FROM Order_ INNER JOIN OrderDetail ON OrderDetail.oid=Order_.oid WHERE Order_.dateOfOrder BETWEEN ? AND ? GROUP BY Order_.dateOfOrder");
        ps.setString(1, datePickerFrom);
        ps.setString(2, datePickerTo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new ReportTableModel(
                    rs.getString(1),
                    String.format("%,.1f", rs.getDouble(2))
            ));
        }
        return list;
    }

    
        ObservableList<ReportTableModel> getOrdersSumByDates(String datePickerFrom, String datePickerTo) throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT dateOfOrder,SUM(amount) FROM Order_ WHERE dateOfOrder BETWEEN ? and ? GROUP BY dateOfOrder");
        ps.setString(1, datePickerFrom);
        ps.setString(2, datePickerTo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new ReportTableModel(
                    rs.getString(1),
                    String.format("%,.2f", rs.getDouble(2))
            ));
        }
        return list;
    }
    
    ObservableList<ReportTableModel> getCustomerWiseSale(String dateFrom, String dateTo) throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT cid,SUM(amount) FROM Order_ WHERE dateOfOrder BETWEEN ? AND ? GROUP BY cid");
        ps.setString(1, dateFrom);
        ps.setString(2, dateTo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Customer customer = new CustomerController().getCustomerFromId(rs.getString(1));
            if (customer == null) {
                return null;
            }
            list.add(new ReportTableModel(
                    customer.getName(),
                    customer.getMobileNumber(),
                    customer.getCid(),
                    String.format("%,.1f", rs.getDouble(2))
            ));
        }
        return list;
    }

    ObservableList<ReportTableModel> getCarWiseSale(String dateFrom, String dateTo) throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT crid,SUM(amount),cid FROM Order_ WHERE dateOfOrder BETWEEN ? AND ? GROUP BY crid");
        ps.setString(1, dateFrom);
        ps.setString(2, dateTo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String cusName = new CustomerController().getCustomerNameFromId(rs.getString(3));
            Car car = new CarController().getCarFromId(rs.getString(1));
            if (car == null || cusName == null) {
                return null;
            }
            list.add(new ReportTableModel(
                    car.getCid(),
                    cusName,
                    car.getRegistrationNumber(),
                    String.format("%,.2f", rs.getDouble(2)),
                    car.getModelNumber()
            ));
        }
        return list;

    }

    @FXML
    private void btnDiscount_onAction(ActionEvent event) {
        if(!txtDiscount.getText().isEmpty()){
                double rate=100-Double.parseDouble(txtDiscount.getText());
                double netTotal=(rate*Double.parseDouble(txtTotalValue.getText()))/100;
                txtNetValue.setText(Double.toString(netTotal));
        }else{
            AlertBox.showErrorMessage("Error", "Enter a discount and try ");
        }
    }

    @FXML
    private void btnDiscount_onKeyPressed(KeyEvent event) {
        if(TextFieldEventsHandling.isEnterPressed(event)){
            btnDiscount.fire();
        }
        
        btnPlaceOrder.requestFocus();
    }

    public String getRevenueForLastDay(String datePickerFrom, String datePickerTo) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT SUM(amount) FROM Order_ WHERE dateOfOrder BETWEEN ? and ?");
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
    
    public String loadTotalReceivale() throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT SUM(amount)-(SELECT SUM(amount) FROM Receipt)-(SELECT SUM(pricePerReturn*returnQty) FROM ReturnInward)+(SELECT SUM(Amount) FROM CreditNote) FROM Order_;");
        double value=0;
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
                value=rs.getDouble(1);
        }
        String format = String.format("%,.2f", value);
        return format;
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) {
        if(!tblOrder.getSelectionModel().isEmpty()){
        int selectedIndex = tblOrder.getSelectionModel().getSelectedIndex();
        tblOrder.getItems().remove(selectedIndex);
        loadTable();
        btnDelete.setDisable(true);
        txtItemModel.requestFocus();
        }
    }

    @FXML
    private void tblOrder_onMouseClicked(MouseEvent event) {
        btnDelete.setDisable(false);
    }

    @FXML
    private void mainPane_onMouseClicked(MouseEvent event) {
        tblOrder.getSelectionModel().clearSelection();
        btnDelete.setDisable(true);
    }

    private void printOrder(String oid) throws JRException, ClassNotFoundException, SQLException, FileNotFoundException{
            InputStream in=new FileInputStream("/home/shanil/NetBeansProjects/CarServiceStationProject/src/lk/r4enterprises/system/view/Reports/Bill.jrxml");
            JasperReport ja=JasperCompileManager.compileReport(in);
            Map<String,Object> para=new HashMap<>();
            para.put("OrderID", oid);
            para.put("oidValueForTable", oid);
            JasperPrint jp=JasperFillManager.fillReport(ja,para,DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jp);
            //JasperPrintManager.printReport(jp, false);
    } 

    
}
