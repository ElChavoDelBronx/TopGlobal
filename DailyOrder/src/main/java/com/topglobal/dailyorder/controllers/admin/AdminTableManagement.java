package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.dao.DiningTableDAO;
import com.topglobal.dailyorder.models.objects.DiningTable;
import com.topglobal.dailyorder.utils.CustomAlert;
import com.topglobal.dailyorder.utils.SessionData;
import com.topglobal.dailyorder.utils.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminTableManagement {
    private SessionData sessionData;
    @FXML Button btnAddTable;
    @FXML FlowPane flowPaneTables;

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
        if(sessionData.getDiningTables().isEmpty()){
            sessionData.setDiningTables(loadTables());
        }else{
            for(DiningTable diningTable : sessionData.getDiningTables()){
                flowPaneTables.getChildren().add(createTableCard(diningTable));
            }
        }
    }

    @FXML
    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
    }
    @FXML
    public void onAddTable(ActionEvent event) {
        View view = new View();
        view.loadModal(event, "/com/topglobal/dailyorder/views/admin/admin_add_table.fxml", "Añadir Nueva Mesa", sessionData);
    }
    protected List<DiningTable> loadTables() {
        List <DiningTable> tables;
        try{
            tables = DiningTableDAO.findAll();
            for (DiningTable table : tables) {
                VBox card = createTableCard(table);
                flowPaneTables.getChildren().add(card);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tables;
    }
    private VBox createTableCard(DiningTable table) {
        //VBox es el contenedor de la tarjeta
        VBox card = new VBox();
        card.getStyleClass().add("card");
        //Obtenemos los datos de la mesa y los mostramos en etiquetas
        Label lblTableNumber = new Label("Mesa "+table.getTableNumber());
        lblTableNumber.setFont(Font.font(16));
        Label lblCapacity = new Label("Capacidad: "+table.getCapacity()+" personas");
        //La disponibilidad de la mesa se muestra con un botón que cambia de estado
        Button btnTableStatus = new Button(table.getStatus() == 1 ? "Disponible" : "Ocupada");
        btnTableStatus.getStyleClass().add(table.getStatus() == 1 ? "tableAvailable" : "tableOccupied");

        //Contenedor de botones (editar, eliminar)
        HBox buttonsSection = new HBox();
        buttonsSection.setAlignment(Pos.CENTER);
        buttonsSection.getStyleClass().add("hbox-CardButtons");
        Button btnEdit = new Button();
        Button btnDelete = new Button();
        btnEdit.getStyleClass().add("action-button");
        btnDelete.getStyleClass().add("action-button");
        //Se le agrega funcionalidad a los botones
        btnEdit.setOnAction(event -> {
            sessionData.setSelectedTable(table);
            View.loadModal(event, "/com/topglobal/dailyorder/views/admin/admin_edit_table.fxml", "Editar Mesa", sessionData);
        });
        btnDelete.setOnAction(event -> {
            sessionData.setSelectedTable(table);
            //Se muestra una alerta de confirmación antes de eliminar la mesa
            Optional<ButtonType> result = CustomAlert.showConfirmationAlert("Confirmar Eliminación","¿Estás seguro?", "Esta acción no se puede deshacer.");
            // Si el usuario confirma, se procede a eliminar la mesa
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    DiningTableDAO.delete(sessionData.getSelectedTable());
                    CustomAlert.showInfoAlert("ÉXITO", "Mesa eliminada correctamente.");
                    flowPaneTables.getChildren().removeIf(node -> ((VBox) node).getChildren().contains(lblTableNumber));
                } catch (Exception e) {
                    CustomAlert.showErrorAlert("ERROR", "No se pudo eliminar la mesa. Inténtalo de nuevo.");
                }
            }
        });
        //SVGPath traza los iconos
        SVGPath editIcon = new SVGPath();
        SVGPath deleteIcon = new SVGPath();
        editIcon.setContent("M28.8799 11.56L8.39591 32.04C7.78687 32.6488 7.39056 33.4379 7.26591 34.29L6.15991 41.838L13.7099 40.732C14.5622 40.6068 15.3514 40.2098 15.9599 39.6L36.4399 19.12M28.8799 11.56L33.3379 7.09999C33.6351 6.80277 33.9879 6.56699 34.3762 6.40613C34.7644 6.24527 35.1806 6.16248 35.6009 6.16248C36.0212 6.16248 36.4374 6.24527 36.8257 6.40613C37.214 6.56699 37.5668 6.80277 37.8639 7.09999L40.8999 10.136C41.1971 10.4331 41.4329 10.7859 41.5938 11.1742C41.7546 11.5625 41.8374 11.9787 41.8374 12.399C41.8374 12.8193 41.7546 13.2355 41.5938 13.6237C41.4329 14.012 41.1971 14.3648 40.8999 14.662L36.4399 19.12M28.8799 11.56L36.4399 19.12");
        deleteIcon.setContent("M13.548 12.8L15.172 40.096C15.1965 40.5028 15.3752 40.8849 15.6718 41.1644C15.9684 41.4439 16.3605 41.5997 16.768 41.6H31.232C31.6395 41.5997 32.0316 41.4439 32.3282 41.1644C32.6248 40.8849 32.8035 40.5028 32.828 40.096L34.452 12.8H13.548ZM36.858 12.8L35.224 40.238C35.1633 41.2557 34.7164 42.2118 33.9744 42.911C33.2325 43.6103 32.2515 43.9998 31.232 44H16.768C15.7485 43.9998 14.7675 43.6103 14.0256 42.911C13.2836 42.2118 12.8367 41.2557 12.776 40.238L11.142 12.8H7V11.4C7 11.1348 7.10536 10.8804 7.29289 10.6929C7.48043 10.5054 7.73478 10.4 8 10.4H40C40.2652 10.4 40.5196 10.5054 40.7071 10.6929C40.8946 10.8804 41 11.1348 41 11.4V12.8H36.858ZM28 6C28.2652 6 28.5196 6.10536 28.7071 6.29289C28.8946 6.48043 29 6.73478 29 7V8.4H19V7C19 6.73478 19.1054 6.48043 19.2929 6.29289C19.4804 6.10536 19.7348 6 20 6H28ZM19 18H21.4L22.4 36H20L19 18ZM26.6 18H29L28 36H25.6L26.6 18Z");
        editIcon.getStyleClass().add("action-icon");
        deleteIcon.getStyleClass().add("action-icon");
        //Agregamos los iconos dentro de los botones
        btnEdit.setGraphic(editIcon);
        btnDelete.setGraphic(deleteIcon);
        //Agregamos los iconos al HBox antes de añadirlo a card
        buttonsSection.getChildren().addAll(btnEdit, btnDelete);
        //Finalmente agregamos las etiquetas de texto, los botones y el HBox al VBox
        card.getChildren().addAll(lblTableNumber, lblCapacity, btnTableStatus, buttonsSection);
        card.setSpacing(8);
        card.setPadding(new Insets(10));
        return card;
    }
}
