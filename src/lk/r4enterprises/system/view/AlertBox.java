/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.view;

import java.awt.Color;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author shanil
 */
public class AlertBox {
   

    public static boolean showConfMessage(String message,String title){
        Alert alertInf=new Alert(Alert.AlertType.CONFIRMATION);
        alertInf.setTitle(title);
        alertInf.setHeaderText("");
        alertInf.setContentText(message);
        Optional<ButtonType> result=alertInf.showAndWait();
        return result.get()==ButtonType.OK;
    }
    
   
    public static void showDisplayMessage(String title,String message){
        Alert info=new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(title);
        info.setHeaderText("");
        info.setContentText(message);
        info.showAndWait();
    }
    
    
    
    public static void showErrorMessage(String title,String message){
        Alert info=new Alert(Alert.AlertType.ERROR);
        info.setTitle(title);
        info.setHeaderText("");
        info.setContentText(message);
        info.showAndWait();
    }
    
    public static void showWarningMessage(String title,String message){
        Alert info=new Alert(Alert.AlertType.WARNING);
        info.setTitle(title);
        info.setHeaderText("");
        info.setContentText(title);
        info.showAndWait();
    }
    
    
    public static String displayMessageInputBox(String title,String heading,String detail){
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle(title);
    dialog.setHeaderText(heading);
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    PasswordField pwd = new PasswordField();
    HBox content = new HBox();
    content.setAlignment(Pos.CENTER_LEFT);
    content.setSpacing(10);
    content.getChildren().addAll(new Label(detail), pwd);
    dialog.getDialogPane().setContent(content);
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == ButtonType.OK) {
            return pwd.getText();
        }
        return null;
    });

    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
        return result.get();
    }
        return "";
    }
    
}
