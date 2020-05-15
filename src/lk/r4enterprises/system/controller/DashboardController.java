/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.r4enterprises.system.model.ReportTableModel;

/**
 *
 * @author shanil
 */
public class DashboardController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private LineChart<String,Double> lineChartDailySale;
    @FXML
    private NumberAxis ySalesAxis;
    @FXML
    private CategoryAxis xSalesAxis;
    private Text txtDate;
    private Text txtTime;
    @FXML
    private Text txtSaleDifference;
    @FXML
    private NumberAxis yItemSalesAxis1;
    @FXML
    private CategoryAxis xItemSalesAxis1;
    @FXML
    private NumberAxis yRetunsAxis1;
    @FXML
    private CategoryAxis xReturnsAxis;
    @FXML
    private Text txtPaymentValue;
    @FXML
    private Text txtRevenueValue;
    @FXML
    private Text txtTotalPayableValue;
    @FXML
    private Text txtTotalReceivable;
    @FXML
    private LineChart<String, Double> lineChartDailySaleCount;
    @FXML
    private Text txtItemCountDifference;
    @FXML
    private LineChart<String, Double> lineChartReturnCount;

    @FXML
    private void mainPane_OnMouseClicked(MouseEvent event) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadSalesLineChart();
            loadSalesCountChart();
            loadPaymentsForLastDay();
            loadRevenueForLastDay();
            loadTotalPayableBalance();
            loadTotalReceivableBalance();
            loadReturnCountChart();
        } catch (ClassNotFoundException | SQLException | ParseException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadSalesLineChart() throws ClassNotFoundException, SQLException, ParseException {
        XYChart.Series series = new XYChart.Series();
        series.setName("Daily Sale");
        LocalDate today = LocalDate.now();
        today.format(DateTimeFormatter.ISO_DATE);

        LocalDate fiveDaysBack = today.minusDays(5);
        fiveDaysBack.format(DateTimeFormatter.ISO_DATE);

        ObservableList<ReportTableModel> ordersByDates = new OrderController().getOrdersSumByDates(fiveDaysBack.toString(), today.toString());
        for (int i = 0; i < ordersByDates.size(); i++) {
            series.getData().add(new XYChart.Data(LocalDate.parse(ordersByDates
                    .get(i).getDateOfTransaction()).getDayOfWeek().toString().substring(0, 1), parseDecimal(ordersByDates.get(i).getAmount())));
        }
        setTheChangeForText(ordersByDates, txtSaleDifference);
        lineChartDailySale.setLegendVisible(false);
        lineChartDailySale.getData().addAll(series);

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

    private void loadPaymentsForLastDay() throws ClassNotFoundException, SQLException {
        LocalDate today = LocalDate.now();
        today.format(DateTimeFormatter.ISO_DATE);

        LocalDate oneDayBack = today.minusDays(1);
        oneDayBack.format(DateTimeFormatter.ISO_DATE);
        String value = new PaymentController().getPaymentsForLastDay(oneDayBack.toString(), today.toString());
        txtPaymentValue.setText(value);
    }

    private void loadRevenueForLastDay() throws ClassNotFoundException, SQLException {
        LocalDate today = LocalDate.now();
        today.format(DateTimeFormatter.ISO_DATE);

        LocalDate oneDayBack = today.minusDays(1);
        oneDayBack.format(DateTimeFormatter.ISO_DATE);
        String value = new OrderController().getRevenueForLastDay(oneDayBack.toString(), today.toString());
        txtRevenueValue.setText(value);
    }

    private void loadTotalPayableBalance() throws ClassNotFoundException, SQLException {
        String totalPayable = new GRNController().loadTotalPayable();
        txtTotalPayableValue.setText(totalPayable);
    }

    private void loadTotalReceivableBalance() throws ClassNotFoundException, SQLException {
        String totalPayable = new OrderController().loadTotalReceivale();
        txtTotalReceivable.setText(totalPayable);
    }

    private void loadSalesCountChart() throws ClassNotFoundException, SQLException, ParseException {
        XYChart.Series series = new XYChart.Series();
        LocalDate today = LocalDate.now();
        today.format(DateTimeFormatter.ISO_DATE);

        LocalDate fiveDaysBack = today.minusDays(5);
        fiveDaysBack.format(DateTimeFormatter.ISO_DATE);

        ObservableList<ReportTableModel> ordersByDates = new OrderController().getOrdersCountByDates(fiveDaysBack.toString(), today.toString());
        for (int i = 0; i < ordersByDates.size(); i++) {
            series.getData().add(new XYChart.Data(LocalDate.parse(ordersByDates
                    .get(i).getDateOfTransaction()).getDayOfWeek().toString().substring(0, 1), parseDecimal(ordersByDates.get(i).getAmount())));
        }
        setTheChangeForText(ordersByDates, txtItemCountDifference);
        lineChartDailySaleCount.setLegendVisible(false);
        lineChartDailySaleCount.getData().addAll(series);

    }

    private void loadReturnCountChart() throws ClassNotFoundException, SQLException, ParseException {
        XYChart.Series series = new XYChart.Series();
        series.setName("Return Count");
        LocalDate today = LocalDate.now();
        today.format(DateTimeFormatter.ISO_DATE);

        LocalDate fiveDaysBack = today.minusDays(5);
        fiveDaysBack.format(DateTimeFormatter.ISO_DATE);

        ObservableList<ReportTableModel> ordersByDates = new ReturnInwardController().getReturnCountByDates(fiveDaysBack.toString(), today.toString());
        for (int i = 0; i < ordersByDates.size(); i++) {
            series.getData().add(new XYChart.Data(LocalDate.parse(ordersByDates
                    .get(i).getDateOfTransaction()).getDayOfWeek().toString().substring(0, 1), ordersByDates.get(i).getQuantity()));
        }

        lineChartReturnCount.setLegendVisible(false);
        lineChartReturnCount.getData().addAll(series);
    }

    private void setTheChangeForText(ObservableList<ReportTableModel> list, Text text) throws ParseException {
        double todayBalance = 0;
        double lastDaybalance = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 2) {
                lastDaybalance = parseDecimal(list.get(i).getAmount());
            } else if (i == list.size() - 1) {
                todayBalance = parseDecimal(list.get(i).getAmount());
            }
        }

        double percentage = ((todayBalance - lastDaybalance) / lastDaybalance) * 100;
        if (percentage < 0) {
            text.setText((int) -percentage + "% decrease");
            text.setStyle("-fx-fill: #D83632");
        } else {
            text.setText((int) percentage + "% increase");
            text.setStyle("-fx-fill: #47A256");
        }

    }
}
