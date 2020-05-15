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
    @FXML
    private PasswordField passwordBefore;
    @FXML
    private Text txtNewUserLabel;
    @FXML
    private Button btnResetPwd;
    @FXML
    private Button btnMakeActive;

    @FXML
    private void txtName_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            comboRole.requestFocus();
        }
        TextFieldEventsHandling.clearIfEscapeIsPressed(event, txtUserName);
    }

    @FXML
    private void txtName_onKeyTyped(KeyEvent event) {
    }

    @FXML
    private void btnCreate_onKeyPressed(KeyEvent event) {
    }

    @FXML
    private void btnCreate_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        boolean exist = isUserAlreadyExist(txtUserName.getText());
        if (exist) {
            AlertBox.showErrorMessage("Error", txtUserName.getText() + " already Exist.Try again with a new name");
            clearFields();
        } else if (validateFieds()) {
            User myUsr = new User(txtNewUID.getText(), txtUserName.getText(), passwordBefore.getText(), comboRole.getSelectionModel().getSelectedItem(), "Active");
            boolean isSave = createNewUser(myUsr);
            if (isSave) {
                AlertBox.showDisplayMessage("Sucess", myUsr.getUserName() + " has been created");
                clearFields();
                loadTable();
            } else {
                AlertBox.showErrorMessage("Error", "Try  again with valid information");
            }
        }
        loadTable();
        txtNewUID.setText(getNewUserId());
        comboRole.setDisable(true);
    }

    @FXML
    private void btnUpdate_onAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (tblUserData.getSelectionModel().isEmpty()) {
            AlertBox.showDisplayMessage("Error", user.getUserName() + "Make a selection from the table");
        } else if (!user.getRole().equals("Admin")) {
            if (validateFieds()) {
                updateUser(new User(user.getUid(), txtUserName.getText(), passwordBefore.getText(), user.getRole(), user.getStatus()));
                AlertBox.showDisplayMessage("Sucess", txtUserName.getText() + " updated.You must login with the valid credentials now.Please press okay to exit the system.");
                System.exit(0);
            }
        } else if (user.getUid().equals(txtUid.getText())) {
            if (validateFieds()) {
                updateUser(new User(txtUid.getText(), txtUserName.getText(), passwordBefore.getText(), comboRole.getSelectionModel().getSelectedItem(), "Active"));
                AlertBox.showDisplayMessage("Sucess", txtUserName.getText() + " updated successfully.");
            }
        } else if (!(user.getUid().equals(txtUid.getText()))) {
            updateNonAdminsByAdmin(new User(comboRole.getSelectionModel().getSelectedItem(), txtUid.getText()));
            AlertBox.showDisplayMessage("Sucess", user.getUid() + " role has been Changed");
            txtUserName.setEditable(true);
        }
        loadTable();
        clearFields();
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
    private void btnDelete_OnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (tblUserData.getSelectionModel().isEmpty()) {
            AlertBox.showErrorMessage("Error", "Make a selection in order to do a Delete Operation");
        } else {
            boolean showConfMessage = AlertBox.showConfMessage("Are you sure to delete " + txtUid.getText(), "Confirmation");
            if (showConfMessage) {
                if (user.getUid().equals(txtUid.getText())) {
                    AlertBox.showErrorMessage("Error", "You are trying to delete your account.This is not possible Admin is mandatory");
                } else {
                    boolean isDeleted = deleteUser(tblUserData.getSelectionModel().getSelectedItem().getUid());
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

    private boolean updateUser(User user) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("UPDATE User SET userName=?,passKey=md5(?),role=? WHERE uid=?");
        ps.setString(1, user.getUserName());
        ps.setString(2, user.getPasKey());
        ps.setString(3, user.getRole());
        ps.setString(4, user.getUid());

        return ps.executeUpdate() > 0;

    }

    private boolean deleteUser(String uid) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM User WHERE uid=?");
        ps.setString(1, uid);
        return ps.executeUpdate() > 0;
    }

    @FXML
    private void bkground_OnMouseClicked(MouseEvent event) {
        tblUserData.getSelectionModel().clearSelection();
        txtUserName.clear();
        comboRole.getSelectionModel().clearSelection();
        txtUid.setText("");
        if (user.getRole().equals("Admin")) {
            passwordBefore.setVisible(true);
            passwordRetype.setVisible(true);
            comboRole.setDisable(false);
            txtUserName.setEditable(true);
        }

    }

    @FXML
    private void tblUserData_onMouseClicked(MouseEvent event) {
        if (tblUserData.getSelectionModel().getSelectedIndex() < 0) {
            AlertBox.showErrorMessage("Error", "Selection was wrong from the table");
        } else if (!user.getRole().equals("Admin")) {
            comboRole.setDisable(true);
            txtUserName.setText(tblUserData.getSelectionModel().getSelectedItem().getUserName());
            comboRole.getSelectionModel().select(tblUserData.getSelectionModel().getSelectedItem().getRole());
            txtUid.setText(tblUserData.getSelectionModel().getSelectedItem().getUid());
        } else {
            User selectedUser = tblUserData.getSelectionModel().getSelectedItem();
            if (!selectedUser.getUid().equals(user.getUid())) {
                txtUserName.setEditable(false);
                passwordBefore.setVisible(false);
                passwordRetype.setVisible(false);
                txtUserName.setText(selectedUser.getUserName());
                comboRole.getSelectionModel().select(selectedUser.getRole());
                txtUid.setText(selectedUser.getUid());
                comboRole.setDisable(false);
            } else {
                txtUserName.setEditable(true);
                comboRole.setDisable(true);
                passwordBefore.setVisible(true);
                passwordRetype.setVisible(true);
                passwordBefore.setEditable(true);
                passwordRetype.setEditable(true);
                txtUserName.setText(selectedUser.getUserName());
                comboRole.getSelectionModel().select(selectedUser.getRole());
                txtUid.setText(selectedUser.getUid());
            }
        }

    }

    public void setUser(User user) throws ClassNotFoundException, SQLException {
        this.user = user;
        if (!user.getRole().equals("Admin")) {
            btnCreate.setVisible(false);
            btnDelete.setVisible(false);
            btnUpdate.setVisible(true);
            txtNewUID.setVisible(false);
            txtNewUserLabel.setVisible(false);
            btnMakeActive.setVisible(false);
            btnResetPwd.setVisible(false);
        }

        loadTable();

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadComboBox();
        try {
            txtNewUID.setText(getNewUserId());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        } else if (passwordBefore.getText().length() < 8) {
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

    private void clearFields() {
        txtUserName.clear();
        passwordBefore.clear();
        passwordRetype.clear();
        txtUid.setText("");

    }

    private boolean createNewUser(User myUsr) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO User VALUES(?,?,md5(?),?,?)");
        ps.setString(1, myUsr.getUid());
        ps.setString(2, myUsr.getUserName());
        ps.setString(3, myUsr.getPasKey());
        ps.setString(4, myUsr.getRole());
        ps.setString(5, "Active");
        return ps.executeUpdate() > 0;

    }

    @FXML
    private void btnResetPwd_onAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (tblUserData.getSelectionModel().isEmpty()) {
            AlertBox.showErrorMessage("Error", "Make a selection from the table");
        } else if (tblUserData.getSelectionModel().getSelectedItem().getRole().equals("Admin")) {
            AlertBox.showErrorMessage("Error", "You can't Reset a password of an Admin");
        } else {
            String firstPwd = AlertBox.displayMessageInputBox("Confirmation", "Type the password", "Enter a valid Password");
            if (firstPwd.isEmpty()) {
                AlertBox.showErrorMessage("Error", "Password cannot be empty");
            } else if (firstPwd.length() < 8) {
                AlertBox.showErrorMessage("Error", "Password must be atlease 8 characters long");
            } else if (!isPasswordWeak(firstPwd)) {
                AlertBox.showErrorMessage("Weak Password", "Your password should containt atleast one Uppercase,One LowerCase and one Digit");
            } else {
                String secondPwd = AlertBox.displayMessageInputBox("Re-Confirmation", "Re-Type the password", "Enter the same password as the previously entered");
                if (firstPwd.equals(secondPwd)) {
                    if (updateUserPassword(firstPwd)) {
                        AlertBox.showDisplayMessage("Success", "Password of " + txtUserName.getText() + " has been reseted");
                    } else {
                        AlertBox.showDisplayMessage("Error", "Try again");
                    }

                } else {
                    AlertBox.showErrorMessage("Error", "Passwords Doesn't Match");
                }
            }

        }

    }

    @FXML
    private void btnMakeActive_onAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (tblUserData.getSelectionModel().isEmpty()) {
            AlertBox.showErrorMessage("Error", "Make a selection from the table");
        } else {
            User selectedUser = tblUserData.getSelectionModel().getSelectedItem();
            if (selectedUser.getRole().equals("Admin")) {
                AlertBox.showErrorMessage("Wrong Choice", "You are the only Admin.You can't make your account Deactive");
            } else if (selectedUser.getStatus().equals("Active")) {
                boolean conf = AlertBox.showConfMessage(selectedUser.getUserName() + " is on Active Mode.This action will make " + selectedUser.getUserName() + " Non active.Are you sure?", "Make Deactive Confirmation");
                if (conf) {
                    boolean makeUserActiveDeactive = makeUserActiveDeactive("Deactive", selectedUser.getUid());
                    if (makeUserActiveDeactive) {
                        AlertBox.showDisplayMessage("Sucess", selectedUser.getUserName() + " has been tranfered to Non active Mode");
                    }
                }
            } else {
                boolean conf = AlertBox.showConfMessage(selectedUser.getUserName() + " is on Non-Active Mode.This action will make " + selectedUser.getUserName() + " active.Are you sure?", "Make Activr Confirmation");
                if (conf) {
                    boolean makeUserActiveDeactive = makeUserActiveDeactive("Active", selectedUser.getUid());
                    if (makeUserActiveDeactive) {
                        AlertBox.showDisplayMessage("Sucess", selectedUser.getUserName() + " has been tranfered to active Mode");
                    }
                }
            }

        }

        loadTable();
    }

    private boolean updateUserPassword(String firstPwd) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("UPDATE User SET passKey=md5(?) WHERE uid=?");
        ps.setString(1, firstPwd);
        ps.setString(2, txtUid.getText());
        return ps.executeUpdate() > 0;

    }

    private boolean makeUserActiveDeactive(String status, String uid) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement("UPDATE User SET status=? WHERE uid=?");
        ps.setString(1, status);
        ps.setString(2, uid);
        return ps.executeUpdate() > 0;
    }

    @FXML
    private void comboRole_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            passwordBefore.requestFocus();
        }

    }

    @FXML
    private void passwordBefore_onKeyPressed(KeyEvent event) {
        if (TextFieldEventsHandling.isEnterPressed(event)) {
            passwordRetype.requestFocus();
        }

    }

    @FXML
    private void passwordRetype_onKeyPressed(KeyEvent event) {
    }

}
