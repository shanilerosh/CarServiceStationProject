/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.User;
import lk.r4enterprises.system.view.AlertBox;
import lk.r4enterprises.system.view.AnimateComponent;

/**
 *
 * @author shanil
 */
public class UserController implements Initializable {

    @FXML
    private AnchorPane bkground;
    @FXML
    private ComboBox<String> comboRole;
    @FXML
    private PasswordField passwordRetype;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<User> tblUserData;
    @FXML
    private TableColumn<User, String> colUid;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private Text txtUid;
    @FXML
    private Text txtNewUID;

    private User user;
    @FXML
    private TableColumn<User, String> colRole;
    @FXML
    private TableColumn<User, String> colStatus;
    @FXML
    private TextField txtUserName;
    private PasswordField PasswordBefore;
    @FXML
    private PasswordField passwordBefore;

    @FXML
    private void txtName_onKeyPressed(KeyEvent event) {
    }

    @FXML
    private void txtName_onKeyTyped(KeyEvent event) {
    }

    @FXML
    private void btnCreate_onKeyPressed(KeyEvent event) {
    }

    @FXML
    private void btnCreate_OnAction(ActionEvent event) {
    }

    @FXML
    private void btnUpdate_onAction(ActionEvent event) throws ClassNotFoundException, SQLException {
            if (!user.getRole().equals("Admin")) {
                AlertBox.showDisplayMessage("Sucess", txtUserName.getText() + " updated.You must login with the valid credentials now.Please press okay to exit the system.");
                System.exit(0);
            } else if (user.getUid().equals(txtUid.getText())) {
                updateUser(new User(txtUid.getText(), txtUserName.getText(), passwordBefore.getText(), comboRole.getSelectionModel().getSelectedItem(), "Active"));
                AlertBox.showDisplayMessage("Sucess", txtUserName.getText() + " updated successfully.");
            } else {
                updateNonAdminsByAdmin(new User(comboRole.getSelectionModel().getSelectedItem(), txtUid.getText()));
                AlertBox.showDisplayMessage("Sucess", user.getUid() + " role has been Changed");
                txtUserName.setEditable(true);
            }
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) {
    }

    private boolean updateUser(User user) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("UPDATE User SET userName=?,passKey=md5(?),role=? WHERE uid=?");
        ps.setString(1, user.getUserName());
        ps.setString(2, user.getPasKey());
        ps.setString(3, user.getRole());
        ps.setString(4, user.getUid());

        return ps.executeUpdate() > 0;

    }

    @FXML
    private void bkground_OnMouseClicked(MouseEvent event) {
        tblUserData.getSelectionModel().clearSelection();
        txtUserName.clear();
        comboRole.getSelectionModel().clearSelection();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnCreate.setDisable(true);
    }

    @FXML
    private void tblUserData_onMouseClicked(MouseEvent event) {
        if (tblUserData.getSelectionModel().getSelectedIndex() < 0) {
            AlertBox.showErrorMessage("Error", "Selection was wrong from the table");
        } else {
            btnUpdate.setDisable(false);
            User selectedUser = tblUserData.getSelectionModel().getSelectedItem();
            txtUid.setText(selectedUser.getUid());
            txtUserName.setText(selectedUser.getUserName());
            comboRole.getSelectionModel().select(selectedUser.getRole());
            
            
            if(user.getRole().equals("Admin")){
                btnCreate.setDisable(false);
                btnDelete.setDisable(false);
                if(!selectedUser.getUid().equals(user.getUid())){
                    txtUserName.setEditable(false);
                }
            }
            
            
            
        }
        
        
        
        

    }

    public void setUser(User user) throws ClassNotFoundException, SQLException {
        this.user = user;
        loadTable();

        if (!this.user.getRole().equals("Admin")) {
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            btnCreate.setDisable(true);
            comboRole.setEditable(false);
        }
        btnUpdate.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadComboBox();
        colUid.setCellValueFactory(new PropertyValueFactory<>("uid"));
        colName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

    }

    private void loadComboBox() {
        comboRole.getItems().addAll("Cashier", "Invoicing officer");
    }

    private void loadTable() throws ClassNotFoundException, SQLException {
        tblUserData.getColumns().clear();
        tblUserData.getColumns().addAll(colUid, colName, colRole, colStatus);
        tblUserData.setItems(getUsers());
    }

    private ObservableList<User> getUsers() throws SQLException, ClassNotFoundException {
        ObservableList<User> list = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT uid,userName,role,status FROM User");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            if (!user.getRole().equals("Admin")) {
                if (rs.getString(1).equals(user.getUid())) {
                    list.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                }
            } else {
                list.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
        }
        return list;
    }

    private boolean validateFieds() {
        boolean isValidated = false;
        if (txtUserName.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtUserName);
            return isValidated;
        } else if (comboRole.getItems().isEmpty()) {
            AnimateComponent.animateEmptyField(comboRole);
            return isValidated;
        } else if (passwordBefore.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(passwordBefore);
            return isValidated;
        } else if (passwordRetype.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(passwordRetype);
            return isValidated;
        } else if (PasswordBefore.getText().length() < 8) {
            AnimateComponent.animateEmptyField(passwordBefore);
            AlertBox.showErrorMessage("Error", "Password must contain atleast 8 characters");
            return isValidated;
        } else if (!isPasswordWeak(passwordBefore.getText())) {
            AnimateComponent.animateEmptyField(passwordBefore);
            AlertBox.showErrorMessage("Weak Password", "Your password should containt atleast one Uppercase,One LowerCase and one Digit");
            return isValidated;
        } else if (!passwordRetype.getText().equals(passwordRetype.getText())) {
            AnimateComponent.animateEmptyField(passwordRetype);
            AlertBox.showErrorMessage("Error", "Passwords doesn't match.Try again");
            return isValidated;
        }
        isValidated = true;
        return isValidated;
    }

    private boolean updateNonAdminsByAdmin(User updatedNonAdminUser) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("UPDATE User SET role=? WHERE uid=?");
        ps.setString(1, updatedNonAdminUser.getRole());
        ps.setString(2, updatedNonAdminUser.getUid());

        return ps.executeUpdate() > 0;
    }

    private boolean isPasswordWeak(String pw) {
        boolean lowerCase = false;
        boolean upperCase = false;
        boolean number = false;
        for (int i = 0; i < pw.length(); i++) {
            if (Character.isLowerCase(pw.charAt(i))) {
                lowerCase = true;
            } else if (Character.isUpperCase(pw.charAt(i))) {
                upperCase = true;
            } else if (Character.isDigit(pw.charAt(i))) {
                number = true;
            }

        }
         return (lowerCase && upperCase && number);
    }
}
