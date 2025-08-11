package com.topglobal.dailyorder.controllers.admin.menu;

import com.topglobal.dailyorder.dao.MenuCategoryDAO;
import com.topglobal.dailyorder.dao.MenuItemDAO;
import com.topglobal.dailyorder.models.objects.MenuCategory;
import com.topglobal.dailyorder.models.objects.MenuItem;
import com.topglobal.dailyorder.utils.CustomAlert;
import com.topglobal.dailyorder.utils.TextFieldUtils;
import com.topglobal.dailyorder.utils.View;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.util.List;

public class EditMenuItemFormController {
    private List<MenuCategory> categories;
    private MenuItem currentItem;
    @FXML TextField tfName;
    @FXML TextArea taDescription;
    @FXML TextField tfCost;
    @FXML ComboBox<String> cbCategory;
    @FXML CheckBox chbxAvailability;
    @FXML Button btnCancel;

    public void setFormData(MenuItem item) {
        currentItem = item;
        tfName.setText(item.getName());
        taDescription.setText(item.getDescription());
        tfCost.setText(String.valueOf(item.getCost()));
        chbxAvailability.setSelected(item.getIsActive() == 1);
        //Carga las categorías de los platillos del menú
        try {
            categories = MenuCategoryDAO.findCategories();
            List<String> categoryDesc = categories.stream()
                    .map(MenuCategory::getDescription)
                    .toList();
            cbCategory.setItems(FXCollections.observableList(categoryDesc));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cbCategory.getSelectionModel().select(item.getCategory().getDescription());
    }

    public void initialize() {
        //Aplica filtros a estos TextField para que solo acepten números decimales
        TextFieldUtils.applyDecimalFilter(tfCost);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
    }

    @FXML
    public void onUpdateMenuItem() {
        String itemName = tfName.getText().trim();
        String itemDescription = taDescription.getText().trim();
        double itemCost = Double.parseDouble(tfCost.getText().trim());
        String selectedCategory = cbCategory.getSelectionModel().getSelectedItem();

        MenuItem newItem = new MenuItem();
        newItem.setId(currentItem.getId()); // Mantiene el ID del platillo
        newItem.setName(itemName);
        newItem.setDescription(itemDescription);
        newItem.setCost(itemCost);

        if(chbxAvailability.isSelected()) {
            newItem.setIsActive(1);
        }else{
            newItem.setIsActive(0);
        }
        try {
            newItem.setCategory(MenuCategoryDAO.findCategory(selectedCategory));
            MenuItemDAO.update(newItem, currentItem.getCategory());
            CustomAlert.showInfoAlert("ÉXITO", "Platillo actualizado correctamente");
            View.closeWindow(btnCancel);
        } catch (Exception e) {
            CustomAlert.showErrorAlert("ERROR", "No se pudo actualizar el platillo. Verifique los datos ingresados.");
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onCancel() {
        View.closeWindow(btnCancel);
    }
}
