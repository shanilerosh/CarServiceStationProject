/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.r4enterprises.system.controller;

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
        if (!Character.isDigit(charAt)) {
            evnt.consume();
        }
    }
    
    public static void allowOnlyNumbersAndDecimal(KeyEvent evnt) {
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

}
