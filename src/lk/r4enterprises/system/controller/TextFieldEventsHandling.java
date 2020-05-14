/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author shanil
 */
public class TextFieldEventsHandling {

    public static void allowOnlyLettersAndCharacters(KeyEvent evnt) {
        String character = evnt.getCharacter();
        char charAt = character.charAt(0);
        if (Character.isDigit(charAt)) {
            evnt.consume();
        }
    }

    public static void allowOnlyNumbers(KeyEvent evnt) {
        String character = evnt.getCharacter();
        char charAt = character.charAt(0);
        if (Character.isLetter(charAt) && !evnt.isAltDown()) {
            evnt.consume();
        }
    }

    public static void clearIfEscapeIsPressed(KeyEvent event, TextField textField) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            textField.clear();
        }
    }

    public static boolean isEnterPressed(KeyEvent event) {
        return event.getCode().equals(KeyCode.ENTER);
    }

    public static void AllowOnlyDecimal(TextField text) {
        text.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("[0-9]+\\.")){
                    text.setText(oldValue);
                }
            }
        });
    }

}
