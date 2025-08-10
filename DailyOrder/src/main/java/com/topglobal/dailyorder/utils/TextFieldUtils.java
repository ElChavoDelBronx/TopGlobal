package com.topglobal.dailyorder.utils;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class TextFieldUtils {
    /**
     * Aplica un filtro para que el TextField solo acepte números enteros
     * @param textField El campo de texto al que se aplicará el filtro
     */
    public static void applyIntegerFilter(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(cambio -> {
            if (cambio.getControlNewText().matches("\\d*")) {
                return cambio; // acepta si son solo dígitos
            }
            return null; // rechaza si no es número
        }));
    }

    /**
     * Aplica un filtro para que el TextField solo acepte números decimales
     * @param textField El campo de texto al que se aplicará el filtro
     */
    public static void applyDecimalFilter(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(cambio -> {
            if (cambio.getControlNewText().matches("\\d*(\\.\\d*)?")) {
                return cambio; // acepta dígitos con un punto opcional
            }
            return null;
        }));
    }
}
