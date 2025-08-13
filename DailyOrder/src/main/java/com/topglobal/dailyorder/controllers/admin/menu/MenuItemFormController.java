package com.topglobal.dailyorder.controllers.admin.menu;

import com.topglobal.dailyorder.dao.DiningTableDAO;
import com.topglobal.dailyorder.dao.MenuCategoryDAO;
import com.topglobal.dailyorder.dao.MenuItemDAO;
import com.topglobal.dailyorder.models.objects.DiningTable;
import com.topglobal.dailyorder.models.objects.MenuCategory;
import com.topglobal.dailyorder.models.objects.MenuItem;
import com.topglobal.dailyorder.utils.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.util.List;

public class MenuItemFormController {
    private SessionData sessionData;
    @FXML TextField tfName;
    @FXML TextArea taDescription;
    @FXML TextField tfCost;
    @FXML ComboBox<String> cbCategory;
    @FXML CheckBox chbxAvailability;
    @FXML Button btnCancel;

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
        //Carga las categorías de los platillos del menú
        try {
            List<String> categoryDesc = sessionData.getMenuCategories().stream()
                    .map(MenuCategory::getDescription)
                    .toList();
            cbCategory.setItems(FXCollections.observableList(categoryDesc));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        //Aplica filtros a estos TextField para que solo acepten números decimales
        TextFieldUtils.applyDecimalFilter(tfCost);

        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
    }

    @FXML
    public void onAddMenuItem() {
        String itemName = tfName.getText().trim();
        String itemDescription = taDescription.getText().trim();
        String itemCost = tfCost.getText().trim();
        String selectedCategory = cbCategory.getSelectionModel().getSelectedItem();

        MenuItem newItem = new MenuItem();
        newItem.setName(itemName);
        newItem.setDescription(itemDescription);
        newItem.setCost(Double.parseDouble(itemCost));

        if(chbxAvailability.isSelected()) {
            newItem.setIsActive(1);
        }else{
            newItem.setIsActive(0);
        }
        try {
            if(itemName.isEmpty() || itemDescription.isEmpty() || !Character.isDigit(itemCost.charAt(0)) || Double.parseDouble(itemCost) == 0 || selectedCategory.isEmpty()){
                throw new CustomException("Campos vacíos o datos inválidos");
            }else{
                newItem.setCategory(MenuCategoryDAO.findCategory(selectedCategory));
                MenuItemDAO.create(newItem);
                CustomAlert.showInfoAlert("ÉXITO", "Platillo agregado correctamente");
                sessionData.addElement(newItem);
                View.closeWindow(btnCancel);
            }

        } catch (Exception e) {
            CustomAlert.showErrorAlert("ERROR", "No se pudo agregar el platillo. Verifique los datos ingresados.");
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onCancel() {
        View.closeWindow(btnCancel);
    }
}
