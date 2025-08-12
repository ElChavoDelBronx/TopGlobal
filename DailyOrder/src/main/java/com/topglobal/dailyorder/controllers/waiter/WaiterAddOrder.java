package com.topglobal.dailyorder.controllers.waiter;

import com.topglobal.dailyorder.utils.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class WaiterAddOrder {
    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
    }
    @FXML
    TextField tfOrderNumber;
    @FXML TextField tfArea;
    @FXML TextField tfTableCapacity;
    @FXML
    CheckBox cbAvailability;
    @FXML
    Button btnCancel;

    @FXML
    public void onAddOrder() {
        int orderNumber = Integer.parseInt(tfOrderNumber.getText().trim());

    }
    @FXML
    public void onCancel() {
        View.closeWindow(btnCancel);
    }
}

