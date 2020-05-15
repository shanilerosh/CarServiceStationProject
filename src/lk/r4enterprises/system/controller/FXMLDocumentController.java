/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import animatefx.animation.BounceInUp;
import animatefx.animation.FadeIn;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.r4enterprises.system.model.User;
import lk.r4enterprises.system.view.AlertBox;

/**
 *
 * @author shanil
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Button btnOrder;
    @FXML
    private Button btnCustomer;
    @FXML
    private Button btnSupplier;
    @FXML
    private Button btnItem;
    @FXML
    private FontAwesomeIconView iconMinimize;
    @FXML
    private FontAwesomeIconView iconClose;
    @FXML
    private Button btnGrn;
    @FXML
    private Button btnReceipt;
    @FXML
    private Button btnPayment;
    @FXML
    private Button btnReturn;
    @FXML
    private Button btnDebitCreditNote;
    @FXML
    private Button btnDashboard;

    private User user;
    @FXML
    private Text txtOrderDate;
    @FXML
    private Text txtOrderTime;
    @FXML
    private FontAwesomeIconView iconSetting;
    @FXML
    private Text txtLoginUserName;
    @FXML
    private Button btnReports;

    @FXML
    void btnCustomer_OnAction(ActionEvent event) throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/lk/r4enterprises/system/view/CustomerScene.fxml"));
        Parent root = fXMLLoader.load();
        root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
        CustomerController customerController = fXMLLoader.<CustomerController>getController();
        customerController.setUserInformation(user);
        borderPane.setCenter(root);
        new BounceInUp(root).play();

    }

    public void setTxtLoginUserName(String name) {
        txtLoginUserName.setText(name);
    }

    @FXML
    void btnSupplier_OnAction(ActionEvent event) throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/lk/r4enterprises/system/view/SupplierScene.fxml"));
        Parent root = fXMLLoader.load();
        SupplierController controller = fXMLLoader.<SupplierController>getController();
        controller.setUser(user);
        root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
        borderPane.setCenter(root);
        new BounceInUp(root).play();

    }

    public void setUser(User user) {
        this.user = user;
        if (this.user.getRole().equals("Cashier")) {
            btnOrder.setDisable(true);
            btnGrn.setDisable(true);
            btnReturn.setDisable(true);
            btnReports.setDisable(true);
        } else if (this.user.getRole().equals("Invoice Officer")) {
            btnReceipt.setDisable(true);
            btnPayment.setDisable(true);
            btnDebitCreditNote.setDisable(true);
            btnReports.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDate();
    }

    @FXML
    private void btnOrder_OnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/lk/r4enterprises/system/view/Order.fxml"));
        root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
        borderPane.setCenter(root);
        new BounceInUp(root).play();
    }

    @FXML
    private void btnItem_onAction(ActionEvent event) throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/lk/r4enterprises/system/view/Item.fxml"));
        Parent root = fXMLLoader.load();
        root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
        ItemController controller = fXMLLoader.<ItemController>getController();
        controller.setUser(user);
        borderPane.setCenter(root);
        new BounceInUp(root).play();
    }

    @FXML
    private void iconMinimize_OnMouseClick(MouseEvent event) {
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    @FXML
    private void iconClose_OnMouseCliked(MouseEvent event) {
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.close();
    }

    @FXML
    private void btnGrn_OnAction(ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/lk/r4enterprises/system/view/GRN.fxml"));
        root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
        borderPane.setCenter(root);
        new BounceInUp(root).play();
    }

    @FXML
    private void btnReceipt_OnAction(ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/lk/r4enterprises/system/view/Receipt.fxml"));
        root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
        borderPane.setCenter(root);
        new BounceInUp(root).play();
    }

    @FXML
    private void btnPayment_OnAction(ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/lk/r4enterprises/system/view/Payment.fxml"));
        root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
        borderPane.setCenter(root);
        new BounceInUp(root).play();
    }

    @FXML
    private void btnReturn_OnAction(ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/lk/r4enterprises/system/view/Returns.fxml"));
        root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
        borderPane.setCenter(root);
        new BounceInUp(root).play();
    }

    @FXML
    private void btnReturn_onAction(ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/lk/r4enterprises/system/view/Reports.fxml"));
        root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
        borderPane.setCenter(root);
        new BounceInUp(root).play();
    }

    @FXML
    private void btnDebitCreditNote_onAction(ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/lk/r4enterprises/system/view/CreditAndDebitNote.fxml"));
        root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
        borderPane.setCenter(root);
        new BounceInUp(root).play();
    }

    @FXML
    private void btnDashboard_onAction(ActionEvent event) {
        loadDashboard();

    }

    private void loadDate() {
        Timeline timeLine = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            txtOrderDate.setText(date.format(DateTimeFormatter.ISO_DATE));
            txtOrderTime.setText(time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());

        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.play();

    }

    public void loadDashboard() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/lk/r4enterprises/system/view/Dashboard.fxml"));
            root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
            borderPane.setCenter(root);
            new BounceInUp(root).play();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void iconSetting_onMouseEntered(MouseEvent event) {
        iconSetting.setStyle("-fx-fill: white");
    }

    @FXML
    private void iconSetting_onMouseClicked(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/lk/r4enterprises/system/view/User.fxml"));
        Parent root = fXMLLoader.load();
        UserController controller = fXMLLoader.<UserController>getController();
        controller.setUser(user);
        root.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/general.css").toExternalForm());
        borderPane.setCenter(root);
        new BounceInUp(root).play();
    }

    @FXML
    private void iconSetting_onMouseExited(MouseEvent event) {
        iconSetting.setStyle("-fx-fill: #8E3A9F");
    }

}
