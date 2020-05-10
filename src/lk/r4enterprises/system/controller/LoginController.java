/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import animatefx.animation.Shake;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.User;
import lk.r4enterprises.system.view.AlertBox;

/**
 * FXML Controller class
 *
 * @author shanil
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtUserName;

    @FXML
    private Button btnLogin;

    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    @FXML
    private FontAwesomeIconView iconMinimize;
    @FXML
    private FontAwesomeIconView iconClose;
    @FXML
    private PasswordField txtPassword;

    private User userInformation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void btnLogin_OnAction(ActionEvent event){
        if (txtUserName.getText().isEmpty()) {
            new Shake(txtUserName).play();
            txtUserName.setStyle("-fx-border-color:#ff0000");
            pause.setOnFinished(evt -> txtUserName.setStyle("-fx-border-color:#097541"));
            pause.play();
        } else if (txtPassword.getText().isEmpty()) {
            new Shake(txtPassword).play();
            txtUserName.setStyle("-fx-border-color:#ff0000");
            pause.setOnFinished(evt -> txtUserName.setStyle("-fx-unfocus-color:#097541"));
            pause.play();
        } else {
            userInformation = getUserInformation(txtUserName.getText(), txtPassword.getText());
            
            if (userInformation != null) {
                if(!userInformation.getStatus().equals("Active")){
                    AlertBox.showErrorMessage("Not active", "You account is on Non active Mode.Please request the Admin to make it 'Active' in order to Login");
                    txtUserName.clear();
                    txtPassword.clear();
                }else{
                    try {
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        
                        stage.close();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lk/r4enterprises/system/view/FXMLDocument.fxml"));
                        Parent parent = fxmlLoader.load();
                        FXMLDocumentController controller = fxmlLoader.<FXMLDocumentController>getController();
                        controller.setUser(userInformation);
                        controller.setTxtLoginUserName(userInformation.getRole()+" : "+userInformation.getUserName());
                        Scene scene = new Scene(parent);
                        
                        scene.getStylesheets().add(getClass().getResource("/lk/r4enterprises/system/view/mainmenue.css").toExternalForm());
                        stage.setScene(scene);
                        stage.show();
                        AlertBox.showDisplayMessage("Success", txtUserName.getText()
                                + " welcome back");
                    } catch (IOException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                AlertBox.showErrorMessage("Error", "Try again with correct"
                        + " Credentials");
                txtPassword.clear();
                txtUserName.clear();
                txtUserName.requestFocus();
            }

        }

    }

    private User getUserInformation(String userName, String passKey) {
        try {
            PreparedStatement ps = DBConnection.getInstance().getConnection().
                    prepareStatement("SELECT uid,userName,role,status FROM User where userName=? and "
                            + "passKey=md5(?)");
            ps.setString(1, userName);
            ps.setString(2, passKey);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
    private void txtUserName_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            txtPassword.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtUserName);
    }

    @FXML
    private void btnLogin_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            btnLogin.fire();
        }

    }

    @FXML
    private void txtPassword_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            btnLogin.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtPassword);
    }

}
