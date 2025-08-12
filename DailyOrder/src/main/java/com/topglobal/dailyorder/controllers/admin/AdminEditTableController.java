package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.dao.DiningTableDAO;
import com.topglobal.dailyorder.models.objects.DiningTable;
import com.topglobal.dailyorder.utils.CustomAlert;
import com.topglobal.dailyorder.utils.TextFieldUtils;
import com.topglobal.dailyorder.utils.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class AdminEditTableController {
    @FXML TextField tfTableNumber;
    @FXML TextField tfArea;
    @FXML TextField tfTableCapacity;
    @FXML CheckBox cbAvailability;
    @FXML Button btnCancel;
    public void setDiningTable(DiningTable diningTable) {
        tfTableNumber.setText(String.valueOf(diningTable.getTableNumber()));
        tfArea.setText(diningTable.getArea());
        tfTableCapacity.setText(String.valueOf(diningTable.getCapacity()));
        cbAvailability.setSelected(diningTable.getStatus() == 1);
    }

    public void initialize() {
        //Aplica filtros a estos TextField para que solo acepten números enteros
        TextFieldUtils.applyIntegerFilter(tfTableCapacity);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
    }
    @FXML
    public void onUpdateTable() {
        int tableNumber = Integer.parseInt(tfTableNumber.getText().trim());
        String area = tfArea.getText().trim();
        int tableCapacity = Integer.parseInt(tfTableCapacity.getText().trim());

        DiningTable newTable = new DiningTable();
        newTable.setTableNumber(tableNumber);
        newTable.setArea(area);
        newTable.setCapacity(tableCapacity);
        if(cbAvailability.isSelected()) {
            newTable.setStatus(1);
        }else{
            newTable.setStatus(0);
        }
        System.out.println(tableNumber);
        System.out.println(area);
        System.out.println(tableCapacity);
        System.out.println(newTable.getStatus());
        try {
            DiningTableDAO.update(newTable);
            CustomAlert.showInfoAlert("ÉXITO", "Mesa actualizada correctamente");
            View.closeWindow(btnCancel);
        } catch (Exception e) {
            CustomAlert.showErrorAlert("ERROR", "No se pudo actualizar la mesa.");
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onCancel() {
        View.closeWindow(btnCancel);
    }
}
