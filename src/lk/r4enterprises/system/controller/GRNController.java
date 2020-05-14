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
import java.util.ArrayList;
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
import javafx.scene.control.DatePicker;
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
import lk.r4enterprises.system.model.GRN;
import lk.r4enterprises.system.model.GRNTableModel;
import lk.r4enterprises.system.model.Item;
import lk.r4enterprises.system.model.ReportTableModel;
import lk.r4enterprises.system.model.Supplier;
import lk.r4enterprises.system.model.SupplierDetail;
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
public class GRNController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField txtSuplierName;
    @FXML
    private TextField txtInvoiceNumber;
    @FXML
    private DatePicker txtSupplierInvoiceDate;
    @FXML
    private Button btnAddItem;
    @FXML
    private TableView<GRNTableModel> tblItemData;
    @FXML
    private TableColumn<GRNTableModel, Integer> colRowCount;
    @FXML
    private TableColumn<GRNTableModel, String> colItemModel;
    @FXML
    private TableColumn<GRNTableModel, String> colItemName;
    @FXML
    private TableColumn<GRNTableModel, Double> colUnitPrice;
    @FXML
    private TableColumn<GRNTableModel, Double> colQty;
    @FXML
    private TableColumn<GRNTableModel, Double> colItemTotal;
    @FXML
    private TextField txtsearchItems;
    @FXML
    private Text txtGrn;
    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtAmount;

    private int counter = 1;
    @FXML
    private Text txtGrnTotalValue;
    @FXML
    private Button btnDelete;

    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    @FXML
    private Button btnPlaceOrder;

    @FXML
    private void btnAddItem_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException {

        if (txtsearchItems.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtsearchItems);
        } else if (txtAmount.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtAmount);
        } else if (txtQuantity.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtQuantity);
        } else {
            Item item = new ItemController().getItemFromModel(txtsearchItems.getText());
            if (item == null) {
                AlertBox.showErrorMessage("Error", txtsearchItems.getText() + "is not found");
            }else if(!item.getContinuity().equals("Con")){
                AlertBox.showErrorMessage("Error", item.getModel()+" is on Discontinued mode.Please request the admin to make it Continue in order to proceed with the GRN"); 
            }else {
                double supplierAmount = Double.parseDouble(txtAmount.getText());
                double supplierQty = Double.parseDouble(txtQuantity.getText());

                int index = isAlreadyExist(txtsearchItems.getText());
                if (index == -1) {
                    tblItemData.getItems().add(new GRNTableModel(counter++,
                            item.getModel(),
                            item.getName(),
                            supplierAmount,
                            supplierQty,
                            supplierAmount * supplierQty
                    ));
                } else {
                    double currentQty = tblItemData.getItems().get(index).getQuantity();
                    tblItemData.getItems().get(index).setQuantity(currentQty + supplierQty);
                    double price = tblItemData.getItems().get(index).getUnitPrice();
                    tblItemData.getItems().get(index).setTotal((currentQty + supplierQty) * price);
                    loadTable();
                }

                setTotalAmount();
                txtsearchItems.clear();
                txtQuantity.clear();
                txtAmount.clear();
                txtsearchItems.requestFocus();
            }
        }

    }

    private int isAlreadyExist(String name) {
        for (int i = 0; i < tblItemData.getItems().size(); i++) {
            String temp = tblItemData.getItems().get(i).getItemModel();
            if (name.equalsIgnoreCase(temp)) {
                return i;
            }

        }
        return -1;
    }

    private void setTotalAmount() {
        double total = 0;
        for (int i = 0; i < tblItemData.getItems().size(); i++) {
            total += tblItemData.getItems().get(i).getTotal();
        }
        txtGrnTotalValue.setText(Double.toString(total));
    }

    @FXML
    private void tblItemData_OnMouseClicked(MouseEvent event) {
        if (!tblItemData.getSelectionModel().isEmpty()) {
            btnDelete.setDisable(false);
        } else {
            btnDelete.setDisable(true);
        }

    }

    @FXML
    private void txtsearchItems_OnKeyReleased(KeyEvent event) {
    }

    private void btnCreate_OnAction(ActionEvent event) {
        double quantity = Double.parseDouble(txtQuantity.getText());
        double unitPrice = Double.parseDouble(txtAmount.getText());
    }

    @FXML
    private void mainPane_OnMouseClicked(MouseEvent event) {
        tblItemData.getSelectionModel().clearSelection();
        btnDelete.setDisable(true);
    }

    public void setGRN() throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT GRN FROM GRNMaster ORDER BY GRN DESC LIMIT 1";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String grn = null;
        if (rs.next()) {
            grn = rs.getString(1);
        }

        if (grn != null) {
            String[] temp = grn.split("GRN");
            int tempNumber = Integer.parseInt(temp[1]);
            tempNumber++;

            if (tempNumber < 10) {
                txtGrn.setText("GRN00" + tempNumber);
            } else if (tempNumber < 100) {
                txtGrn.setText("GRN0" + tempNumber);
            } else {
                txtGrn.setText("GRN" + tempNumber);
            }
        } else {
            txtGrn.setText("GRN001");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colRowCount.setCellValueFactory(new PropertyValueFactory<>("counter"));
        colItemModel.setCellValueFactory(new PropertyValueFactory<>("itemModel"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        loadTable();

        btnDelete.setDisable(true);
        try {
            setGRN();
            TextFields.bindAutoCompletion(txtSuplierName, new SupplierController().getAllSupplierNames());
            TextFields.bindAutoCompletion(txtsearchItems, new ItemController().getAllItemsByName());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(GRNController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTable() {
        tblItemData.getColumns().clear();
        tblItemData.getColumns().addAll(colRowCount, colItemModel, colItemName, colUnitPrice, colQty, colItemTotal);
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) {
        tblItemData.getItems().remove(tblItemData.getSelectionModel().getSelectedIndex());
        setTotalAmount();
        btnDelete.setDisable(true);
    }

    @FXML
    private void btnPlaceOrder_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException, JRException, FileNotFoundException {
        boolean isPrint = false;
        if (txtSuplierName.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtSuplierName);
        } else if (txtInvoiceNumber.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtInvoiceNumber);
        } else if (txtSupplierInvoiceDate.getEditor().getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtSupplierInvoiceDate);
        } else if (tblItemData.getItems().isEmpty()) {
            AlertBox.showErrorMessage("Error", "NO items as been inserted");
        } else {
            String grn = txtGrn.getText();
            Supplier supplier = new SupplierController().getSupplierFromName(txtSuplierName.getText());

            if (supplier == null) {
                AlertBox.showErrorMessage("Error", txtSuplierName.getText() + " doesn't Exist.First register a new Supplier in order to raise a GRN");
            } else if (!supplier.getStatus().equals("Active")) {
                AlertBox.showErrorMessage("Error", supplier.getName() + " is at Inactive Mode.Please request admin to switch it to Active Mode");
            } else {
                ArrayList<SupplierDetail> list = new ArrayList<>();

                for (int i = 0; i < tblItemData.getItems().size(); i++) {
                    list.add(new SupplierDetail(
                            grn,
                            supplier.getSid(),
                            new ItemController().getItemFromModel(tblItemData.getItems().get(i).getItemModel()).getIid(),
                            tblItemData.getItems().get(i).getQuantity(),
                            tblItemData.getItems().get(i).getUnitPrice()
                    ));
                    System.out.println("item id=" + list.get(i).getIid());

                }

                Connection con;

                con = DBConnection.getInstance().getConnection();
                con.setAutoCommit(false);

                GRN grnPlace = new GRN(
                        grn,
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                        txtInvoiceNumber.getText(),
                        txtSupplierInvoiceDate.getEditor().getText(),
                        new SupplierController().getSupplierId(txtSuplierName.getText()),
                        Double.parseDouble(txtGrnTotalValue.getText()),
                        list
                );

                boolean isSuccess = placeGRN(grnPlace);

                if (isSuccess) {
                    isPrint = AlertBox.showConfMessage(grnPlace.getGrn() + " has been created successfully.Do you want to print the GRN?", "Sucess,Proceed to print?");
                    con.commit();
                } else {
                    AlertBox.showErrorMessage("Error", "Try again");
                    con.rollback();
                }
                con.setAutoCommit(true);

                if (isPrint) {
                    printGrn(grnPlace.getGrn());
                }

            }
            
            clearFieldsAndFocus();
        }

    }

    private boolean placeGRN(GRN grn) throws SQLException, ClassNotFoundException {
        boolean isSave = false;
        PreparedStatement ps;
        ps = DBConnection.getInstance().getConnection().
                prepareCall("INSERT INTO GRNMaster values(?,?,?,?,?,?)");
        ps.setString(1, txtGrn.getText());
        ps.setString(2, LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        ps.setString(3, txtInvoiceNumber.getText());
        ps.setString(4, txtSupplierInvoiceDate.getEditor().getText());
        ps.setString(5, grn.getSid());
        ps.setDouble(6, Double.parseDouble(txtGrnTotalValue.getText()));
        isSave = ps.executeUpdate() > 0;
        System.out.println(" GRN place false= " + isSave);

        if (isSave) {
            boolean isAddedDetais = new SupplierDetailController().addItemDetail(grn.getList());
            System.out.println(" Details place false= " + isAddedDetais);
            if (isAddedDetais) {
                boolean isUpdated = new ItemController().updateQtyBasedOnGRN(grn.getList());
                System.out.println(" Items false= " + isUpdated);
                if (isUpdated) {
                    return true;
                }
            }

        }

        return false;

    }

    private void clearFieldsAndFocus() throws ClassNotFoundException, SQLException {
        txtSuplierName.clear();
        txtInvoiceNumber.clear();
        txtSupplierInvoiceDate.getEditor().clear();
        txtsearchItems.clear();
        txtAmount.clear();
        txtQuantity.clear();
        tblItemData.getItems().clear();
        txtGrnTotalValue.setText("0.00");
        setGRN();
        txtSuplierName.requestFocus();
    }

    @FXML
    private void txtSuplierName_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            txtInvoiceNumber.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtSuplierName);
    }

    @FXML
    private void txtSuplierName_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyLettersAndCharacters(event);
    }

    @FXML
    private void btnAddItem_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            btnAddItem.fire();
        }
    }

    @FXML
    private void txtsearchItems_OnKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            txtAmount.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtsearchItems);
    }

    @FXML
    private void txtAmount_OnKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            txtQuantity.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtAmount);
    }

    @FXML
    private void txtQuantity_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            btnAddItem.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtQuantity);
    }

    @FXML
    private void txtQuantity_onKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
    }

    @FXML
    private void txtAmount_OnKeyTyped(KeyEvent event) {
        TextFieldEventsHandling.allowOnlyNumbers(event);
    }

    @FXML
    private void txtInvoiceNumber_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            txtSupplierInvoiceDate.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtAmount);
    }

    @FXML
    private void txtSupplierInvoiceDate_onKeyPressed(KeyEvent event) {

    }

    @FXML
    private void txtSupplierInvoiceDate_onKeyReleased(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            txtSupplierInvoiceDate.show();
        }
    }

    ObservableList<ReportTableModel> getGRNWisePurchase(String dateFrom, String dateTo) throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM GRNMaster WHERE GRDate BETWEEN ? AND ?");
        ps.setString(1, dateFrom);
        ps.setString(2, dateTo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new ReportTableModel(
                    rs.getString(1),
                    rs.getString(5),
                    rs.getString(2),
                    String.format("%,.1f", rs.getDouble(6)),
                    rs.getString(3)
            ));
        }
        return list;
    }

    ObservableList<ReportTableModel> getItemWisePurchase(String dateFrom, String dateTo) throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT SupplierDetail.iid,SUM(SupplierDetail.quantity),SUM(SupplierDetail.quantity*SupplierDetail.amount) FROM SupplierDetail INNER JOIN GRNMaster ON SupplierDetail.GRN=GRNMaster.GRN WHERE GRNMaster.GRDate BETWEEN ? AND ?");
        ps.setString(1, dateFrom);
        ps.setString(2, dateTo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Item item = new ItemController().getItemFromIid(rs.getString(1));
            if (item == null) {
                return null;
            }
            list.add(new ReportTableModel(
                    (int) rs.getDouble(2),
                    item.getModel(),
                    item.getName(),
                    item.getIid(),
                    String.format("%,.1f", rs.getDouble(3))
            ));
        }
        return list;
    }

    ObservableList<ReportTableModel> getSupplierWisePurchase(String dateFrom, String dateTo) throws ClassNotFoundException, SQLException {
        ObservableList<ReportTableModel> list = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT GRNMaster.sid,SUM(SupplierDetail.quantity),SUM(SupplierDetail.amount*SupplierDetail.quantity) FROM SupplierDetail INNER JOIN GRNMaster ON SupplierDetail.GRN=GRNMaster.GRN WHERE GRNMaster.GRDate BETWEEN ? AND ? GROUP BY sid");
        ps.setString(1, dateFrom);
        ps.setString(2, dateTo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Supplier supplier = new SupplierController().getSupplierFromId(rs.getString(1));
            if (supplier == null) {
                return null;
            }
            list.add(new ReportTableModel(
                    String.format("%,.1f", rs.getDouble(3)),
                    (int) rs.getDouble(2),
                    supplier.getSid(),
                    supplier.getName()
            ));
        }
        return list;
    }

    public String loadTotalPayable() throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT SUM(totalAmount)-(SELECT SUM(amount) FROM Payment)-(SELECT SUM(pricePerReturn*returnQty) FROM ReturnOutward)+(SELECT SUM(Amount) FROM DebitNote) FROM GRNMaster");
        double value = 0;
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            value = rs.getDouble(1);
        }
        String format = String.format("%,.2f", value);
        return format;

    }

    private void printGrn(String grn) throws JRException, FileNotFoundException, ClassNotFoundException, SQLException {
        InputStream in = new FileInputStream("/home/shanil/NetBeansProjects/CarServiceStationProject/src/lk/r4enterprises/system/view/Reports/GRN.jrxml");
        JasperReport ja = JasperCompileManager.compileReport(in);
        Map<String, Object> para = new HashMap<>();
        para.put("grID", grn);
        para.put("GRNValueForTable", grn);
        JasperPrint jp = JasperFillManager.fillReport(ja, para, DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jp, false);
    }

}
