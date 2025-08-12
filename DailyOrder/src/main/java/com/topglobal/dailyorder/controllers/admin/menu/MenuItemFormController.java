package com.topglobal.dailyorder.controllers.admin.menu;

import com.topglobal.dailyorder.dao.DiningTableDAO;
import com.topglobal.dailyorder.dao.MenuCategoryDAO;
import com.topglobal.dailyorder.dao.MenuItemDAO;
import com.topglobal.dailyorder.models.objects.DiningTable;
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

public class MenuItemFormController {
    private List<MenuCategory> categories;
    @FXML TextField tfName;
    @FXML TextArea taDescription;
    @FXML TextField tfCost;
    @FXML ComboBox<String> cbCategory;
    @FXML CheckBox chbxAvailability;
    @FXML Button btnCancel;
    public void initialize() {
        //Aplica filtros a estos TextField para que solo acepten números decimales
        TextFieldUtils.applyDecimalFilter(tfCost);
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
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
    }

    @FXML
    public void onAddMenuItem() {
        String itemName = tfName.getText().trim();
        String itemDescription = taDescription.getText().trim();
        double itemCost = Double.parseDouble(tfCost.getText().trim());
        String selectedCategory = cbCategory.getSelectionModel().getSelectedItem();

        MenuItem newItem = new MenuItem();
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
            MenuItemDAO.create(newItem);
            CustomAlert.showInfoAlert("ÉXITO", "Platillo agregado correctamente");
            View.closeWindow(btnCancel);
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
