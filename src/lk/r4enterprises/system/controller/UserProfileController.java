/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;
import lk.r4enterprises.system.db.DBConnection;
import lk.r4enterprises.system.model.User;
import lk.r4enterprises.system.view.AlertBox;
import lk.r4enterprises.system.view.AnimateComponent;

/**
 *
 * @author shanil
 */
public class UserProfileController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField txtNUserName;
    @FXML
    private Button btnUpdate;
    @FXML
    private ComboBox<String> comboRole;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnFollow;
    @FXML
    private Button btnCreate;
    @FXML
    private TableView<User> tblUser;
    @FXML
    private Text txtUid;

    @FXML
    private PasswordField pwdNewPass;
    @FXML
    private PasswordField pwdRetypeNewPwd;
    @FXML
    private Text txtRole;
    @FXML
    private Text txtName;
    @FXML
    private TableColumn<User, String> colUserId;
    @FXML
    private TableColumn<User, String> colUserName;
    @FXML
    private TableColumn<User, String> colUserRole;

    private User user;
    @FXML
    private Button btnResetPwd;
    @FXML
    private Button btnMakeActive;
    @FXML
    private AnchorPane btnClearSelection;
    @FXML
    private Button btnResetSelection;
    @FXML
    private TableColumn<User, String> colStatus;

    @FXML
    private void txtNUserName_onKeyPressed(KeyEvent event) {
    }

    @FXML
    private void txtNUserName_onKeyTyped(KeyEvent event) {
    }

    @FXML
    private void btnUpdate_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        boolean showConfMessage = AlertBox.showConfMessage("Are you sure to Update", "Update Confirmation");
        if (showConfMessage && validateFieds()) {
            if (!user.getRole().equals("Admin")) {
                boolean isSucess = updateUser(new User(txtUid.getText(), txtNUserName.getText(), pwdNewPass.getText(), user.getRole(), user.getStatus()));
                if (isSucess) {
                    AlertBox.showDisplayMessage("Sucess", txtNUserName.getText() + " updated.You must login with the valid credentials now.Please press okay to exit the system.So that you can login again");
                    System.exit(0);
                }
            }else if(tblUser.getSelectionModel().getSelectedItem().getUid().equals(user.getUid())) {
                if (!user.getRole().equals(comboRole.getSelectionModel().getSelectedItem())) {
                    AlertBox.showErrorMessage("Error", "You are an Admin.An admin is mandatory.You cannot change your own role");
                } else {
                    boolean isSucess = updateUser(new User(txtUid.getText(), txtNUserName.getText(), pwdNewPass.getText(), user.getRole(), user.getStatus()));
                    if (isSucess) {
                        AlertBox.showDisplayMessage("Sucess", txtNUserName.getText() + " has been updated");
                    }
                }
            }else if(!tblUser.getSelectionModel().getSelectedItem().getUid().equals(user.getUid())){
                User updatedNonAdminUser = new User(comboRole.getSelectionModel().getSelectedItem(), txtUid.getText());
                boolean isSave = updateNonAdminsByAdmin(updatedNonAdminUser);
                System.out.println("Comeshere2");
                System.out.println(isSave);
                if (isSave) {
                    AlertBox.showDisplayMessage("Sucess", txtUid.getText() + " only the role has been changed");
                } else {
                    AlertBox.showErrorMessage("Error", "Try again");
                }
                                      
            }
        
        }
        loadTable();

    }

    @FXML
    private void comboCategory_onKeyPressed(KeyEvent event) {
        
    }

    @FXML
    private void btnDelete_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException{
        if (tblUser.getSelectionModel().isEmpty()) {
            AlertBox.showErrorMessage("Error", "Make a selection in order to do a Delete Operation");
        } else {
            boolean showConfMessage = AlertBox.showConfMessage("Are you sure to delete " + txtUid.getText(), "Confirmation");
            if (showConfMessage) {
                if (user.getUid().equals(txtUid.getText())) {
                    AlertBox.showErrorMessage("Error", "You are trying to delete your account.This is not possible Admin is mandatory");
                } else {
                    boolean isDeleted = deleteUser();
                    if (isDeleted) {
                        AlertBox.showDisplayMessage("Sucess", txtUid.getText() + " has been deleted");
                        clearFields();
                        loadTable();
                    } else {
                        AlertBox.showErrorMessage("Error", "Try again");   
                    }
                }
            }
        }
    }

    @FXML
    private void btnFollow_onKeyPressed(KeyEvent event) {

    }

    @FXML
    private void btnCreate_onKeyPressed(KeyEvent event) {
    }

    @FXML
    private void btnCreate_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        boolean exist = isUserAlreadyExist(txtNUserName.getText());
        if (exist) {
            AlertBox.showErrorMessage("Error", txtNUserName.getText() + " already Exist.Try again with a new name");
            clearFields();
        } else if (validateFieds()) {
            boolean confMessage = AlertBox.showConfMessage("Are you sure to create a new User", "Create New User");
            System.out.println("Confimation " + confMessage);
            if (confMessage) {
                User myUsr = new User(getNewUserId(), txtNUserName.getText(), pwdNewPass.getText(), comboRole.getSelectionModel().getSelectedItem(), "Active");
                boolean isSave = createNewUser(myUsr);
                if (isSave) {
                    AlertBox.showDisplayMessage("Sucess", myUsr.getUserName() + " has been created");
                    clearFields();
                    loadTable();
                } else {
                    AlertBox.showErrorMessage("Error", "Try  again with valid information");
                }
            }
        }

        loadTable();
    }

    @FXML
    private void mainPane_OnMouseClicked(MouseEvent event) {

    }

    public void setUser(User user) {
        this.user = user;
        if (!user.getRole().equals("Admin")) {
            btnCreate.setVisible(false);
            btnDelete.setVisible(false);
            tblUser.setVisible(false);
            btnMakeActive.setVisible(false);
            comboRole.setVisible(false);
            btnResetSelection.setVisible(false);
            btnResetPwd.setVisible(false);

        } else {
            comboRole.getSelectionModel().select(user.getRole());
        }
        txtName.setText(user.getUserName());
        txtRole.setText(user.getRole());
        txtNUserName.setText(user.getUserName());
        txtUid.setText(user.getUid());
    }

    @FXML
    private void pwdNewPass_onKeyPressed(KeyEvent event) {
    }

    @FXML
    private void pwdRetypeNewPwd(KeyEvent event) {
    }

    private boolean validateFieds() {
        boolean isValidated = false;
        if (txtNUserName.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(txtNUserName);
            return isValidated;
        } else if (comboRole.getItems().isEmpty()) {
            AnimateComponent.animateEmptyField(comboRole);
            return isValidated;
        } else if (pwdNewPass.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(pwdNewPass);
            return isValidated;
        } else if (pwdRetypeNewPwd.getText().isEmpty()) {
            AnimateComponent.animateEmptyField(pwdRetypeNewPwd);
            return isValidated;
        } else if (pwdNewPass.getText().length() < 8) {
            AnimateComponent.animateEmptyField(pwdNewPass);
            AlertBox.showErrorMessage("Error", "Password must contain atleast 8 characters");
            return isValidated;
        } else if (!isPasswordNotWeak(pwdNewPass.getText())) {
            AnimateComponent.animateEmptyField(pwdRetypeNewPwd);
            AlertBox.showErrorMessage("Weak Password", "Your password should containt atleast one Uppercase,One LowerCase and one Digit");
            return isValidated;
        } else if (!pwdNewPass.getText().equals(pwdRetypeNewPwd.getText())) {
            AnimateComponent.animateEmptyField(pwdNewPass);
            AlertBox.showErrorMessage("Error", "Passwords doesn't match.Try again");
            return isValidated;
        }
        isValidated = true;
        return isValidated;
    }

    private boolean isPasswordNotWeak(String pw) {
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

        System.out.println(lowerCase);
        System.out.println(upperCase);
        System.out.println(number);
        System.out.println(lowerCase && upperCase && number);
        return (lowerCase && upperCase && number);
    }

    private boolean createNewUser(User user) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO User VALUES(?,?,md5(?),?,?)");
        ps.setString(1, user.getUid());
        ps.setString(2, user.getUserName());
        ps.setString(3, user.getPasKey());
        ps.setString(4, user.getRole());
        ps.setString(5, "Active");

        return ps.executeUpdate() > 0;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadComboBox();
        colUserId.setCellValueFactory(new PropertyValueFactory<>("uid"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        try {
            loadTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadComboBox() {
        comboRole.getItems().addAll("Cashier", "Invoicing officer");
    }

    private boolean deleteUser() throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM User WHERE uid=?");
        ps.setString(1, txtUid.getText());
        return ps.executeUpdate() > 0;
    }

    private boolean updateNonAdminsByAdmin(User updatedNonAdminUser) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("UPDATE User SET role=? WHERE uid=?");
        ps.setString(1, updatedNonAdminUser.getRole());
        ps.setString(2, updatedNonAdminUser.getUid());

        return ps.executeUpdate() > 0;
    }

    private boolean updateUser(User user) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("UPDATE User SET userName=?,passKey=md5(?),role=? WHERE uid=?");
        ps.setString(1, user.getUserName());
        ps.setString(2, user.getPasKey());
        ps.setString(3, user.getRole());
        ps.setString(4, user.getUid());

        return ps.executeUpdate() > 0;

    }

    public String getNewUserId() throws ClassNotFoundException, SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT uid FROM User ORDER BY uid DESC LIMIT 1";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String userId = null;
        if (rs.next()) {
            userId = rs.getString(1);
        }
        String id = null;
        if (userId != null) {
            String[] temp = userId.split("U");
            int tempNumber = Integer.parseInt(temp[1]);
            tempNumber++;

            if (tempNumber < 10) {
                id = "U00" + tempNumber;
            } else if (tempNumber < 100) {
                id = "U0" + tempNumber;
            } else {
                id = "U" + tempNumber;
            }
        } else {
            id = "U001";
        }

        return id;
    }

    @FXML
    private void btnResetPwd_onAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (tblUser.getSelectionModel().isEmpty()) {
            AlertBox.showErrorMessage("Error", "Make a selection from the table");
        } else if (tblUser.getSelectionModel().getSelectedItem().getRole().equals("Admin")) {
            AlertBox.showErrorMessage("Error", "You can't Reset a password of an Admin");
        } else {
            String firstPwd = AlertBox.displayMessageInputBox("Confirmation", "Type the password", "Enter a valid Password");
            if (firstPwd.isEmpty()) {
                AlertBox.showErrorMessage("Error", "Password cannot be empty");
            } else if (firstPwd.length() < 8) {
                AlertBox.showErrorMessage("Error", "Password must be atlease 8 characters long");
            } else if (!isPasswordNotWeak(firstPwd)) {
                AlertBox.showErrorMessage("Weak Password", "Your password should containt atleast one Uppercase,One LowerCase and one Digit");
            } else {
                String secondPwd = AlertBox.displayMessageInputBox("Re-Confirmation", "Re-Type the password", "Enter the same password as the previously entered");
                if (firstPwd.equals(secondPwd)) {
                    if (updateUserPassword(firstPwd)) {
                        AlertBox.showDisplayMessage("Success", "Password of " + txtNUserName.getText() + " has been reseted");
                    } else {
                        AlertBox.showDisplayMessage("Errpr", "Try again");
                    }

                } else {
                    AlertBox.showErrorMessage("Error", "Passwords Doesn't Match");
                }
            }

        }

    }

    @FXML
    private void tblUser_onMouseClicked(MouseEvent event) {
        if (tblUser.getSelectionModel().getSelectedIndex() < 0) {
            AlertBox.showErrorMessage("Error", "Choose a correct selection from the table");
        } else {
            User selectedUser = tblUser.getSelectionModel().getSelectedItem();
            if (!selectedUser.getUid().equals(user.getUid())) {
                pwdNewPass.setVisible(false);
                pwdRetypeNewPwd.setVisible(false);
                btnResetPwd.setVisible(true);
                btnMakeActive.setVisible(true);
                txtNUserName.setEditable(false);
            } else {
                pwdNewPass.setVisible(true);
                pwdRetypeNewPwd.setVisible(true);
                btnResetPwd.setVisible(false);
                btnMakeActive.setVisible(false);
                txtNUserName.setEditable(true);
            }
            txtNUserName.setText(selectedUser.getUserName());
            comboRole.getSelectionModel().select(selectedUser.getRole());
            txtUid.setText(selectedUser.getUid());
        }
    }

    @FXML
    private void btnMakeActive_onAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        User selectedItem = tblUser.getSelectionModel().getSelectedItem();

        String status = selectedItem.getStatus();

        if (status.equals("Active")) {
            boolean conf = AlertBox.showConfMessage(selectedItem.getUserName() + " is on Active Mode.This action will make " + selectedItem.getUserName() + " Non active.Are you sure?", "Make Deactive Confirmation");
            if (conf) {
                boolean makeUserActiveDeactive = makeUserActiveDeactive("Deactive", selectedItem.getUid());
                if (makeUserActiveDeactive) {
                    AlertBox.showDisplayMessage("Sucess", selectedItem.getUserName() + " has been tranfered to Non active Mode");
                }
            }
        } else {
            boolean conf = AlertBox.showConfMessage(selectedItem.getUserName() + " is on Non-Active Mode.This action will make " + selectedItem.getUserName() + " active.Are you sure?", "Make Activr Confirmation");
            if (conf) {
                boolean makeUserActiveDeactive = makeUserActiveDeactive("Active", selectedItem.getUid());
                if (makeUserActiveDeactive) {
                    AlertBox.showDisplayMessage("Sucess", selectedItem.getUserName() + " has been tranfered to active Mode");
                }
            }
        }
        loadTable();
    }

    private void loadTable() throws ClassNotFoundException, SQLException {
        tblUser.getColumns().clear();
        tblUser.getColumns().addAll(colUserId, colUserName, colUserRole, colStatus);
        tblUser.setItems(getUsers());
    }

    private ObservableList<User> getUsers() throws ClassNotFoundException, SQLException {
        ObservableList<User> list = FXCollections.observableArrayList();
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("SELECT uid,userName,role,status FROM User");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        return list;
    }

    private boolean isUserAlreadyExist(String userName) throws ClassNotFoundException, SQLException {

        for (int i = 0; i < getUsers().size(); i++) {
            if (userName.equals(getUsers().get(i).getUserName())) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void btnClearSelection_onMouseClicked(MouseEvent event) {
        tblUser.getSelectionModel().clearSelection();
        txtNUserName.clear();
        pwdNewPass.setVisible(true);
        pwdRetypeNewPwd.setVisible(true);
    }

    @FXML
    private void btnResetSelection_OnAction(ActionEvent event) {
    }

    private boolean updateUserPassword(String firstPwd) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("UPDATE User SET passKey=md5(?) WHERE uid=?");
        ps.setString(1, firstPwd);
        ps.setString(2, txtUid.getText());
        return ps.executeUpdate() > 0;

    }

    private void clearFields() {
        txtNUserName.clear();
        pwdNewPass.clear();
        pwdRetypeNewPwd.clear();
    }

    private boolean makeUserActiveDeactive(String status, String uid) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("UPDATE User SET status=? WHERE uid=?");
        ps.setString(1, status);
        ps.setString(2, uid);
        return ps.executeUpdate() > 0;
    }

}
