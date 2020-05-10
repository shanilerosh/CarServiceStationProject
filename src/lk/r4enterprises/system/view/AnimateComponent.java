/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.view;

import animatefx.animation.Shake;
import javafx.animation.PauseTransition;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 *
 * @author shanil
 */
public class AnimateComponent {
    public static PauseTransition pause = new PauseTransition(Duration.seconds(3));
    
    public static void animateEmptyField(TextField textField) {
        new Shake(textField).play();
        textField.setStyle("-fx-border-color:#ff0000");
        pause.setOnFinished(evt -> textField.setStyle("-fx-border-color:#097541"));
        pause.play();
    }
    
    
    public static void animateEmptyField(ComboBox<String> combo) {
        new Shake(combo).play();
        combo.setStyle("-fx-border-color:#ff0000");
        pause.setOnFinished(evt -> combo.setStyle("-fx-border-color:#097541"));
        pause.play();
    }
    
    public static void animateEmptyField(DatePicker datePicker) {
        new Shake(datePicker).play();
        datePicker.setStyle("-fx-border-color:#ff0000");
        pause.setOnFinished(evt -> datePicker.setStyle("-fx-border-color:#097541"));
        pause.play();
    }
    
    public static void animateEmptyField(TableView table) {
        new Shake(table).play();
        table.setStyle("-fx-border-color:#ff0000");
        pause.setOnFinished(evt -> table.setStyle("-fx-border-color:#097541"));
        pause.play();
    }
    
    
    public static void animateEmptyField(PasswordField pwd) {
        new Shake(pwd).play();
        pwd.setStyle("-fx-border-color:#ff0000");
        pause.setOnFinished(evt -> pwd.setStyle("-fx-border-color:#097541"));
        pause.play();
    }
}
