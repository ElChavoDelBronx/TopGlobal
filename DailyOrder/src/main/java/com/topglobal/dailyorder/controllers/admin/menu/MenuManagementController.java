package com.topglobal.dailyorder.controllers.admin.menu;

import com.topglobal.dailyorder.dao.DiningTableDAO;
import com.topglobal.dailyorder.dao.MenuCategoryDAO;
import com.topglobal.dailyorder.dao.MenuItemDAO;
import com.topglobal.dailyorder.models.objects.MenuCategory;
import com.topglobal.dailyorder.models.objects.MenuItem;

import com.topglobal.dailyorder.utils.CustomAlert;
import com.topglobal.dailyorder.utils.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import org.controlsfx.control.ToggleSwitch;

import java.util.List;
import java.util.Optional;

public class MenuManagementController {
    private MenuItem selectedDish;
    private List<String> categoriesDesc;
    @FXML Button btnAllDishes;
    @FXML FlowPane fpCategories;
    @FXML FlowPane fpDishes;
    public void initialize() {
        try {
            List<MenuCategory> categories =MenuCategoryDAO.findCategories();
            categoriesDesc = categories.stream()
                    .map(MenuCategory::getDescription)
                    .toList();
            for(String category : categoriesDesc) {
                Button b = new Button(category);
                b.getStyleClass().add("category-button");
                fpCategories.getChildren().add(b);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Thin.ttf"), 12);

        btnAllDishes.getStyleClass().add("active-category");
        loadAllDishCards();
    }
    @FXML
    protected void loadAllDishCards() {
        try{
            List <MenuItem> dishes = MenuItemDAO.findAll();
            for (MenuItem dish : dishes) {
                VBox card = createDishCard(dish);
                fpDishes.getChildren().add(card);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private VBox createDishCard(MenuItem dish) {
        //VBox es el contenedor de la tarjeta
        VBox card = new VBox();
        card.getStyleClass().add("card");
        //Obtenemos los datos del platillo y los mostramos en etiquetas
        Label lblDishName = new Label(dish.getName());
        lblDishName.getStyleClass().add("dishName-label");
        Label lblDishDesc = new Label(dish.getDescription());
        lblDishDesc.getStyleClass().add("dishDesc-label");
        lblDishDesc.setWrapText(true);

        //Contenedor horizontal (costo, switch)
        HBox hboxInf = new HBox();
        hboxInf.getStyleClass().add("dishInfo-box");
        VBox costSection = new VBox();
        costSection.getStyleClass().add("costSection");
        VBox switchSection = new VBox();
        switchSection.getStyleClass().add("switchSection");
        VBox editSection = new VBox();
        editSection.getStyleClass().add("editSection");
        costSection.setAlignment(Pos.CENTER_LEFT);
        switchSection.setAlignment(Pos.CENTER_RIGHT);
        editSection.setAlignment(Pos.CENTER);

        hboxInf.getStyleClass().add("hbox-info");
        //Agregamos la información del costo
        Label lblCost = new Label("$"+dish.getCost());
        lblCost.getStyleClass().add("cost-label");
        costSection.getChildren().add(lblCost);

        //Switch para activar/desactivar el platillo
        ToggleSwitch switchActive = new ToggleSwitch(dish.getIsActive() == 1 ? "Activo" : "Inactivo");
        switchActive.setSelected(dish.getIsActive() == 1);
        switchSection.getChildren().add(switchActive);

        //--Boton de editar--
        Button btnEdit = new Button();
        btnEdit.getStyleClass().add("action-button");
        //SVGPath traza el icono de editar
        SVGPath editIcon = new SVGPath();
        editIcon.setContent("M28.8799 11.56L8.39591 32.04C7.78687 32.6488 7.39056 33.4379 7.26591 34.29L6.15991 41.838L13.7099 40.732C14.5622 40.6068 15.3514 40.2098 15.9599 39.6L36.4399 19.12M28.8799 11.56L33.3379 7.09999C33.6351 6.80277 33.9879 6.56699 34.3762 6.40613C34.7644 6.24527 35.1806 6.16248 35.6009 6.16248C36.0212 6.16248 36.4374 6.24527 36.8257 6.40613C37.214 6.56699 37.5668 6.80277 37.8639 7.09999L40.8999 10.136C41.1971 10.4331 41.4329 10.7859 41.5938 11.1742C41.7546 11.5625 41.8374 11.9787 41.8374 12.399C41.8374 12.8193 41.7546 13.2355 41.5938 13.6237C41.4329 14.012 41.1971 14.3648 40.8999 14.662L36.4399 19.12M28.8799 11.56L36.4399 19.12");
        editIcon.getStyleClass().add("action-icon");
        //Agregamos el icono dentro del boton
        btnEdit.setGraphic(editIcon);
        //Agregamos el boton al HBox junto con el switch
        editSection.getChildren().add(btnEdit);
        //Se le agrega funcionalidad al boton de editar
        btnEdit.setOnAction(event -> {
            selectedDish = dish;
            View view = new View(selectedDish);
            view.loadModal(event, "/com/topglobal/dailyorder/views/admin/menu/editMenuItem_form.fxml", "Editar Platillo");
        });
        //Agregamos los HBox hijos al HBox padre antes de añadirlo a card
        hboxInf.getChildren().addAll(costSection, switchSection);

        //Finalmente agregamos las etiquetas de texto, los botones y el HBox al VBox
        card.getChildren().addAll(lblDishName, lblDishDesc, hboxInf, editSection);
        card.setSpacing(8);
        card.setPadding(new Insets(10));
        return card;
    }
    @FXML
    private void onAddDish(ActionEvent event) {
        View view = new View();
        view.loadModal(event, "/com/topglobal/dailyorder/views/admin/menu/menuItem_form.fxml", "Agregar Platillo");
    }
}
