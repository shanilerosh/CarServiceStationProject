/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.converter.LocalDateStringConverter;
import lk.r4enterprises.system.model.ReportTableModel;
import lk.r4enterprises.system.view.AlertBox;
import lk.r4enterprises.system.view.AnimateComponent;

/**
 *
 * @author shanil
 */
public class ReportController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private ComboBox<String> comboAreaOfChoice;
    @FXML
    private ComboBox<String> comboSubCategories;
    @FXML
    private ComboBox<String> comboTimeDuration;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    @FXML
    private TextField txtSearchDetail;
    @FXML
    private TableView<ReportTableModel> tblReportData;

    @FXML
    private DatePicker datePickerFrom;
    @FXML
    private DatePicker datePickerTo;

    private Text txtDate;
    private Text txtTime;
    @FXML
    private Text txtTotal;
    @FXML
    private Text txtTotalCount;
    NumberFormat format = NumberFormat.getCurrencyInstance(Locale.FRANCE);

    @FXML
    private void btnCreate_OnAction(ActionEvent event) throws IOException, ClassNotFoundException, SQLException, ParseException {
        if (comboAreaOfChoice.getSelectionModel().isEmpty()) {
            AnimateComponent.animateEmptyField(comboSubCategories);
        } else if (comboSubCategories.getSelectionModel().isEmpty()) {
            AnimateComponent.animateEmptyField(comboSubCategories);
        } else if (datePickerFrom.getEditor().getText().isEmpty()) {
            AnimateComponent.animateEmptyField(datePickerFrom);
        } else if (datePickerTo.getEditor().getText().isEmpty()) {
            AnimateComponent.animateEmptyField(datePickerTo);
        } else {
            switch (comboSubCategories.getSelectionModel().getSelectedItem()) {
                case "Order-wise Sales":
                    orderReportTable();
                    break;
                case "Item-wise Sale":
                    ItemWiseReportTabe();
                    break;
                case "Customer-wise Sale":
                    CustomerWiseReportTabe();
                    break;
                case "Car-Wise Sale":
                    CarWiseReportTabe();
                    break;
                case "GRN-Wise Purchase":
                    GRNWiseTable();
                    break;
                case "Item-Wise Purchase":
                    ItemWisePurchaseTable();
                    break;
                case "Supplier-Wise Purchase":
                    SupplierWisePurchaseTable();
                    break;
                case "Receivable Customers":
                    AlertBox.showDisplayMessage("Reminder", "Receivable balances are always presented as at present date.It is advised to keep the date range as today");
                    CustomerWiseReceivableTable();
                    break;
                case "Credit Note":
                    CreditNoteTable();
                    break;
                case "Payable Suppliers":
                    AlertBox.showDisplayMessage("Reminder", "Payable balances are always presented as at present date.It is advised to keep the date range as today");
                    SupplierWiseReceivableTable();
                    break;
                case "Debit Note":
                    DebitNoteTable();
                    break;
                case "Sales Profit":
                    SalesProfitTable();
                    break;

            }

        }

    }

    @FXML
    private void btnUpdate_OnAction(ActionEvent event) {
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) {
    }

    @FXML
    private void searchSupplier(KeyEvent event) {
    }

    @FXML
    private void mainPane_OnMouseClicked(MouseEvent event) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadComboBoxData();
        comboSubCategories.setDisable(true);
        datePickerFrom.setConverter(new LocalDateStringConverter(DateTimeFormatter.ISO_DATE, DateTimeFormatter.ISO_DATE));
        datePickerTo.setConverter(new LocalDateStringConverter(DateTimeFormatter.ISO_DATE, DateTimeFormatter.ISO_DATE));
        datePickerFrom.setDisable(true);
        datePickerTo.setDisable(true);

    }

    private void loadComboBoxData() {
        comboAreaOfChoice.getItems().addAll("Sales", "Purchases", "Customer", "Supplier", "Profitability");
        comboTimeDuration.getItems().addAll("Today", "Yesterday", "Last Week", "Last Month", "Last 6 month", "Last Year", "Between");
    }

    @FXML
    private void comboAreaOfChoice_onAction(ActionEvent event) {
        comboSubCategories.setDisable(false);
        if (comboAreaOfChoice.getSelectionModel().isSelected(0)) {
            comboSubCategories.getItems().clear();
            comboSubCategories.getItems().addAll("Order-wise Sales", "Item-wise Sale", "Customer-wise Sale", "Car-Wise Sale");
        } else if (comboAreaOfChoice.getSelectionModel().isSelected(1)) {
            comboSubCategories.getItems().clear();
            comboSubCategories.getItems().addAll("GRN-Wise Purchase", "Supplier-Wise Purchase", "Item-Wise Purchase");
        } else if (comboAreaOfChoice.getSelectionModel().isSelected(2)) {
            comboSubCategories.getItems().clear();
            comboSubCategories.getItems().addAll("Receivable Customers", "Credit Note");
        } else if (comboAreaOfChoice.getSelectionModel().isSelected(3)) {
            comboSubCategories.getItems().clear();
            comboSubCategories.getItems().addAll("Payable Suppliers", "Debit Note");
        } else if (comboAreaOfChoice.getSelectionModel().isSelected(4)) {
            comboSubCategories.getItems().clear();
            comboSubCategories.getItems().addAll("Sales Profit", "Item Wise Profit");
        }
    }

    @FXML
    private void comboTimeDuration_onAction(ActionEvent event) {
        datePickerFrom.setDisable(false);
        datePickerTo.setDisable(false);
        LocalDate now = LocalDate.now();
        if (comboTimeDuration.getSelectionModel().isSelected(0)) {
            datePickerFrom.setValue(LocalDate.now());
            datePickerTo.setValue(LocalDate.now());
        } else if (comboTimeDuration.getSelectionModel().isSelected(1)) {
            datePickerFrom.setValue(LocalDate.now().minusDays(1));
            datePickerTo.setValue(LocalDate.now().minusDays(1));
        } else if (comboTimeDuration.getSelectionModel().isSelected(2)) {
            LocalDate weekStart = now.minusDays(7 + now.getDayOfWeek().getValue() - 1);
            LocalDate weekEnd = now.minusDays(now.getDayOfWeek().getValue());
            datePickerFrom.setValue(weekStart);
            datePickerTo.setValue(weekEnd);
        } else if (comboTimeDuration.getSelectionModel().isSelected(3)) {
            LocalDate previousMonth = now.minusMonths(1);
            LocalDate monthStart = previousMonth.withDayOfMonth(1);
            LocalDate monthEnd = previousMonth.withDayOfMonth(previousMonth.getMonth().maxLength());
            datePickerFrom.setValue(monthStart);
            datePickerTo.setValue(monthEnd);
        } else if (comboTimeDuration.getSelectionModel().isSelected(4)) {
            LocalDate thisMonthStart = now.withDayOfMonth(1);
            datePickerFrom.setValue(thisMonthStart);
            datePickerTo.setValue(thisMonthStart.minusMonths(6));
        } else if (comboTimeDuration.getSelectionModel().isSelected(5)) {
            LocalDate lastDayOfLastYear = now.withDayOfYear(1).minusDays(1);
            LocalDate FirstDayOfLastYear = now.minusYears(1).withDayOfYear(1);
            datePickerFrom.setValue(FirstDayOfLastYear);
            datePickerTo.setValue(lastDayOfLastYear);
        }

    }

    @FXML
    private void comboSubCategories_onAction(ActionEvent event) throws IOException {

    }

    private void orderReportTable() throws ClassNotFoundException, SQLException, ParseException {
        TableColumn<ReportTableModel, String> colOid = new TableColumn<>("OID");
        colOid.setCellValueFactory(new PropertyValueFactory<>("oid"));
        colOid.setMinWidth(150);
        TableColumn<ReportTableModel, String> colDate = new TableColumn<>("Order Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateOfTransaction"));
        colDate.setMinWidth(150);
        TableColumn<ReportTableModel, String> colCustomerName = new TableColumn<>("Customer Name");
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerName.setMinWidth(150);
        TableColumn<ReportTableModel, String> colDiscount = new TableColumn<>("Discount");
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colDiscount.setMinWidth(150);
        TableColumn<ReportTableModel, String> colAmount = new TableColumn<>("Amount");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setMinWidth(150);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(colOid, colDate, colCustomerName, colDiscount, colAmount);
        tblReportData.setItems(new OrderController().getOrdersByDates(datePickerFrom.getEditor().getText(), datePickerTo.getEditor().getText()));
        setTotalValueAndCount();
    }

    private void setTotalValueAndCount() throws ParseException {
        int counter = 0;
        double total = 0;

        for (int i = 0; i < tblReportData.getItems().size(); i++) {
            total += parseDecimal(tblReportData.getItems().get(i).getAmount());
            counter++;
        }
        txtTotal.setText(String.format("%,.2f", total));
        txtTotalCount.setText(Integer.toString(counter));
    }

    private double parseDecimal(String input) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        ParsePosition parsePosition = new ParsePosition(0);
        Number number = numberFormat.parse(input, parsePosition);

        if (parsePosition.getIndex() != input.length()) {
            throw new ParseException("Invalid input", parsePosition.getIndex());
        }

        return number.doubleValue();
    }

    private void ItemWiseReportTabe() throws SQLException, ClassNotFoundException, ParseException {
        TableColumn<ReportTableModel, String> coliid = new TableColumn<>("IID");
        coliid.setCellValueFactory(new PropertyValueFactory<>("iid"));
        coliid.setMinWidth(70);
        TableColumn<ReportTableModel, String> colItemModel = new TableColumn<>("Item Model");
        colItemModel.setCellValueFactory(new PropertyValueFactory<>("itemModel"));
        colItemModel.setMinWidth(125);
        TableColumn<ReportTableModel, String> colItemName = new TableColumn<>("Item Name");
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colItemName.setMinWidth(125);
        TableColumn<ReportTableModel, Integer> colSoldQuantity = new TableColumn<>("Sold Quantity");
        colSoldQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSoldQuantity.setMinWidth(125);
        TableColumn<ReportTableModel, String> colPrice = new TableColumn<>("Item Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        colPrice.setMinWidth(125);
        TableColumn<ReportTableModel, String> colAmount = new TableColumn<>("Estimated Revenue");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setMinWidth(125);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(coliid, colItemModel, colItemName, colSoldQuantity, colPrice, colAmount);
        tblReportData.setItems(new OrderDetailController().getItemSaleCountByDates(datePickerFrom.getEditor().getText(), datePickerTo.getEditor().getText()));
        setTotalValueAndCount();
    }

    private void CustomerWiseReportTabe() throws SQLException, ClassNotFoundException, ParseException {
        TableColumn<ReportTableModel, String> colCid = new TableColumn<>("CID");
        colCid.setCellValueFactory(new PropertyValueFactory<>("cid"));
        colCid.setMinWidth(70);
        TableColumn<ReportTableModel, String> colCustomerName = new TableColumn<>("Customer Name");
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerName.setMinWidth(160);
        TableColumn<ReportTableModel, String> colCustomerMobile = new TableColumn<>("Customer Mobile");
        colCustomerMobile.setCellValueFactory(new PropertyValueFactory<>("customerMobile"));
        colCustomerMobile.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colAmount = new TableColumn<>("Sale per Customer");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setMinWidth(400);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(colCid, colCustomerName, colCustomerMobile, colAmount);
        tblReportData.setItems(new OrderController().getCustomerWiseSale(datePickerFrom.getEditor().getText(), datePickerTo.getEditor().getText()));
        setTotalValueAndCount();
    }

    private void CarWiseReportTabe() throws ParseException, ClassNotFoundException, SQLException {
        TableColumn<ReportTableModel, String> colCrid = new TableColumn<>("Car ID");
        colCrid.setCellValueFactory(new PropertyValueFactory<>("oid"));
        colCrid.setMinWidth(70);
        TableColumn<ReportTableModel, String> colCustomerName = new TableColumn<>("Brought Customer");
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerName.setMinWidth(160);
        TableColumn<ReportTableModel, String> colRegNumber = new TableColumn<>("Car Resgistration No");
        colRegNumber.setCellValueFactory(new PropertyValueFactory<>("dateOfTransaction"));
        colRegNumber.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colModel = new TableColumn<>("Car Model");
        colModel.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colModel.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colAmount = new TableColumn<>("Sale per Car");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setMinWidth(240);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(colCrid, colRegNumber, colCustomerName, colModel, colAmount);
        tblReportData.setItems(new OrderController().getCarWiseSale(datePickerFrom.getEditor().getText(), datePickerTo.getEditor().getText()));
        setTotalValueAndCount();

    }

    private void GRNWiseTable() throws ClassNotFoundException, ParseException, SQLException {
        TableColumn<ReportTableModel, String> colGrn = new TableColumn<>("GRN");
        colGrn.setCellValueFactory(new PropertyValueFactory<>("oid"));
        colGrn.setMinWidth(70);
        TableColumn<ReportTableModel, String> colSupplierName = new TableColumn<>("Supplier Name");
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colSupplierName.setMinWidth(160);
        TableColumn<ReportTableModel, String> colGrDate = new TableColumn<>("GR Date");
        colGrDate.setCellValueFactory(new PropertyValueFactory<>("dateOfTransaction"));
        colGrDate.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colSupplierInvoice = new TableColumn<>("Supplier Invoice/N");
        colSupplierInvoice.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colSupplierInvoice.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colAmount = new TableColumn<>("GRN Amount");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setMinWidth(240);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(colGrn, colGrDate, colSupplierName, colSupplierInvoice, colAmount);
        tblReportData.setItems(new GRNController().getGRNWisePurchase(datePickerFrom.getEditor().getText(), datePickerTo.getEditor().getText()));
        setTotalValueAndCount();
    }

    private void ItemWisePurchaseTable() throws ParseException, ClassNotFoundException, SQLException {
        TableColumn<ReportTableModel, String> colIid = new TableColumn<>("Item ID");
        colIid.setCellValueFactory(new PropertyValueFactory<>("iid"));
        colIid.setMinWidth(70);
        TableColumn<ReportTableModel, String> colItemName = new TableColumn<>("Item Name");
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colItemName.setMinWidth(160);
        TableColumn<ReportTableModel, String> colItemModel = new TableColumn<>("Item Model");
        colItemModel.setCellValueFactory(new PropertyValueFactory<>("itemModel"));
        colItemModel.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colTotalQuantity = new TableColumn<>("Total Purchased quantity");
        colTotalQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotalQuantity.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colAmount = new TableColumn<>("Total Purchased Value");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setMinWidth(240);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(colIid, colItemModel, colItemName, colTotalQuantity, colAmount);
        tblReportData.setItems(new GRNController().getItemWisePurchase(datePickerFrom.getEditor().getText(), datePickerTo.getEditor().getText()));
        setTotalValueAndCount();

    }

    private void SupplierWisePurchaseTable() throws ParseException, ClassNotFoundException, SQLException {
        TableColumn<ReportTableModel, String> colSid = new TableColumn<>("Supplier ID");
        colSid.setCellValueFactory(new PropertyValueFactory<>("sid"));
        colSid.setMinWidth(70);
        TableColumn<ReportTableModel, String> colSupplierName = new TableColumn<>("Supplier Name");
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colSupplierName.setMinWidth(250);
        TableColumn<ReportTableModel, String> colTotalQty = new TableColumn<>("Total Purchased Qty");
        colTotalQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotalQty.setMinWidth(250);
        TableColumn<ReportTableModel, Integer> colAmount = new TableColumn<>("Total Amount Purchase(Per Supplier)");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setMinWidth(250);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(colSid, colSupplierName, colTotalQty, colAmount);
        tblReportData.setItems(new GRNController().getSupplierWisePurchase(datePickerFrom.getEditor().getText(), datePickerTo.getEditor().getText()));
        setTotalValueAndCount();

    }

    private void CustomerWiseReceivableTable() throws ClassNotFoundException, SQLException, ParseException {
        TableColumn<ReportTableModel, String> colCustIid = new TableColumn<>("Customer ID");
        colCustIid.setCellValueFactory(new PropertyValueFactory<>("cid"));
        colCustIid.setMinWidth(70);
        TableColumn<ReportTableModel, String> colCustomerName = new TableColumn<>("Customer Name");
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerName.setMinWidth(160);
        TableColumn<ReportTableModel, String> colTotalOrders = new TableColumn<>("Total Orders");
        colTotalOrders.setCellValueFactory(new PropertyValueFactory<>("totalOrder"));
        colTotalOrders.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colTotalReceipt = new TableColumn<>("Total Rceipts");
        colTotalReceipt.setCellValueFactory(new PropertyValueFactory<>("totalReceipts"));
        colTotalReceipt.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colTotalReturn = new TableColumn<>("Total Return");
        colTotalReturn.setCellValueFactory(new PropertyValueFactory<>("totalReturns"));
        colTotalReturn.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colTotalCreditNotes = new TableColumn<>("Total Credit Notes");
        colTotalCreditNotes.setCellValueFactory(new PropertyValueFactory<>("totalCreditNotes"));
        colTotalCreditNotes.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colAmount = new TableColumn<>("Net Receivable");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setMinWidth(160);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(colCustIid, colCustomerName, colTotalOrders, colTotalReceipt, colTotalReturn, colTotalCreditNotes, colAmount);
        tblReportData.setItems(new CustomerController().getReceivableCustomer());
        setTotalValueAndCount();

    }

    private void CreditNoteTable() throws ClassNotFoundException, SQLException, ParseException {
        TableColumn<ReportTableModel, String> colCnid = new TableColumn<>("CN Id");
        colCnid.setCellValueFactory(new PropertyValueFactory<>("cid"));
        colCnid.setMinWidth(70);
        TableColumn<ReportTableModel, String> colCustomerName = new TableColumn<>("Customer Name");
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerName.setMinWidth(250);
        TableColumn<ReportTableModel, String> colDateOfTransaction = new TableColumn<>("Date of Trans");
        colDateOfTransaction.setCellValueFactory(new PropertyValueFactory<>("customerMobile"));
        colDateOfTransaction.setMinWidth(250);
        TableColumn<ReportTableModel, Integer> colAmount = new TableColumn<>("Total Amount");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setMinWidth(250);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(colCnid, colDateOfTransaction, colCustomerName, colAmount);
        tblReportData.setItems(new CreditAndDebitNoteController().getAllCreditNotes(datePickerFrom.getEditor().getText(), datePickerTo.getEditor().getText()));
        setTotalValueAndCount();
    }

    private void SupplierWiseReceivableTable() throws ParseException, ClassNotFoundException, SQLException {
        TableColumn<ReportTableModel, String> colSupIid = new TableColumn<>("Supplier ID");
        colSupIid.setCellValueFactory(new PropertyValueFactory<>("cid"));
        colSupIid.setMinWidth(70);
        TableColumn<ReportTableModel, String> colSupplierName = new TableColumn<>("Supplier Name");
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colSupplierName.setMinWidth(160);
        TableColumn<ReportTableModel, String> colTotalGRNs = new TableColumn<>("Total GRN");
        colTotalGRNs.setCellValueFactory(new PropertyValueFactory<>("totalOrder"));
        colTotalGRNs.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colTotalPayment = new TableColumn<>("Total Payment");
        colTotalPayment.setCellValueFactory(new PropertyValueFactory<>("totalReceipts"));
        colTotalPayment.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colTotalReturn = new TableColumn<>("Total Return");
        colTotalReturn.setCellValueFactory(new PropertyValueFactory<>("totalReturns"));
        colTotalReturn.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colTotalDebitNotes = new TableColumn<>("Total Debit Notes");
        colTotalDebitNotes.setCellValueFactory(new PropertyValueFactory<>("totalCreditNotes"));
        colTotalDebitNotes.setMinWidth(160);
        TableColumn<ReportTableModel, Integer> colAmount = new TableColumn<>("Net Receivable");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setMinWidth(160);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(colSupIid, colSupplierName, colTotalGRNs, colTotalPayment, colTotalReturn, colTotalDebitNotes, colAmount);
        tblReportData.setItems(new SupplierController().getPayableSuppliers());
        setTotalValueAndCount();
    }

    private void DebitNoteTable() throws ClassNotFoundException, SQLException, ParseException {
        TableColumn<ReportTableModel, String> colDnid = new TableColumn<>("DN Id");
        colDnid.setCellValueFactory(new PropertyValueFactory<>("cid"));
        colDnid.setMinWidth(70);
        TableColumn<ReportTableModel, String> colSupplierName = new TableColumn<>("Supplier Name");
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colSupplierName.setMinWidth(250);
        TableColumn<ReportTableModel, String> colDateOfTransaction = new TableColumn<>("Date of Trans");
        colDateOfTransaction.setCellValueFactory(new PropertyValueFactory<>("customerMobile"));
        colDateOfTransaction.setMinWidth(250);
        TableColumn<ReportTableModel, Integer> colAmount = new TableColumn<>("Total Amount");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setMinWidth(250);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(colDnid, colDateOfTransaction, colSupplierName, colAmount);
        tblReportData.setItems(new CreditAndDebitNoteController().getAllDebitNotes(datePickerFrom.getEditor().getText(), datePickerTo.getEditor().getText()));
        setTotalValueAndCount();
    }

    private void SalesProfitTable() throws ParseException, ClassNotFoundException, SQLException {
        TableColumn<ReportTableModel, String> colOrderId = new TableColumn<>("Order Id");
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("oid"));
        colOrderId.setMinWidth(70);
        TableColumn<ReportTableModel, String> colOrderAmount = new TableColumn<>("Order Amount");
        colOrderAmount.setCellValueFactory(new PropertyValueFactory<>("totalOrder"));
        colOrderAmount.setMinWidth(250);
        TableColumn<ReportTableModel, String> colCustomerName = new TableColumn<>("Customer Name");
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerName.setMinWidth(250);
        TableColumn<ReportTableModel, String> colDate = new TableColumn<>("Date of Trans");
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateOfTransaction"));
        colDate.setMinWidth(250);
        TableColumn<ReportTableModel, String> colExpenses = new TableColumn<>("Cost per order");
        colExpenses.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colExpenses.setMinWidth(250);
        TableColumn<ReportTableModel, Integer> colNetProfit = new TableColumn<>("Net Profit");
        colNetProfit.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colNetProfit.setMinWidth(250);
        tblReportData.getColumns().clear();
        tblReportData.getColumns().addAll(colOrderId,colDate,colCustomerName,colOrderAmount,colExpenses,colNetProfit);
        tblReportData.setItems(new OrderController().getProfitPerOrder(datePickerFrom.getEditor().getText(), datePickerTo.getEditor().getText()));
        setTotalValueAndCount();
    }

}
