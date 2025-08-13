package com.topglobal.dailyorder.controllers.waiter;

import com.topglobal.dailyorder.dao.FoodOrderDAO;
import com.topglobal.dailyorder.models.objects.FoodOrder;
import com.topglobal.dailyorder.models.objects.MenuItem;
import com.topglobal.dailyorder.models.users.Employee;
import com.topglobal.dailyorder.utils.Session;
import com.topglobal.dailyorder.utils.SessionData;
import com.topglobal.dailyorder.utils.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WaiterHistoryController {
    private SessionData sessionData;
    @FXML private FlowPane cardContainer;
    @FXML private Button btnAll;
    @FXML private Button btnPending;
    @FXML private Button btnPrep;
    @FXML private Button btnReady;
    @FXML private Button btnApproved;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() { //Se ejecuta antes de setSessionData
    }

    public void setSessionData(SessionData sessionData) { //La logica de inicializacion se movio a este metodo
        this.sessionData = sessionData;
        if(sessionData.getFoodOrders().isEmpty()){ //Si la lista de ordenes esta vacia, consulta la base de datos
            loadAllOrders(null);
        }else{
            render(sessionData.getFoodOrders()); //Si ya no esta vacia, solo renderiza las tarjetas
        }
    }

    @FXML
    private void loadAllOrders(ActionEvent event) {
        try {
            // Obtener el ID del mesero logueado
            int waiterId = sessionData.getUser().getId();
            render(FoodOrderDAO.getOrdersByWaiterId(waiterId));


            // Llamar al DAO para obtener solo las órdenes de ese mesero
            //List<FoodOrder> orders = FoodOrderDAO.getOrdersByWaiterId(waiterId);
            sessionData.setFoodOrders(FoodOrderDAO.getOrdersByWaiterId(waiterId));

            // Renderizar las órdenes en la interfaz
            render(sessionData.getFoodOrders());

            // Marcar el botón como activo
            markActive(btnAll);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void loadPendingOrders(ActionEvent event) {
        loadByStatus("Pendiente", btnPending);
    }

    @FXML
    private void loadPrepOrders(ActionEvent event) {
        loadByStatus("En Preparación", btnPrep);
    }

    @FXML
    private void loadReadyOrders(ActionEvent event) {
        loadByStatus("Lista para Servir", btnReady);
    }

    @FXML
    private void loadApprovedOrders(ActionEvent event) {
        loadByStatus("Aprobada", btnApproved);
    }

    private void loadByStatus(String status, Button activeBtn) {
        try {
            render(FoodOrderDAO.findByStatus(status));
            markActive(activeBtn);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void render(List<FoodOrder> orders) {
        cardContainer.getChildren().clear();
        for (FoodOrder o : orders) {
            cardContainer.getChildren().add(createOrderCard(o));
        }
    }

    private VBox createOrderCard(FoodOrder order) {
        VBox card = new VBox();
        card.getStyleClass().add("card");

        // Etiqueta con ID de la orden
        Label lblOrderId = new Label("Folio #" + order.getDailyFolio());
        lblOrderId.setFont(Font.font(16));

        // Meta
        Label lblTableId = new Label("Mesa: " + order.getDiningTableId());
        lblTableId.getStyleClass().add("muted");

        // Etiqueta con fecha (solo LocalDate)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = order.getOrderDate() != null ? order.getOrderDate().format(formatter) : "N/A";
        Label lblDate = new Label("Fecha: " + formattedDate);

        // Etiqueta para mostrar estado con colores
        Label lblStatus = new Label();
        String statusText = order.getOrderStatus() != null ? order.getOrderStatus() : "Desconocido";
        String statusClass = "";
        switch (statusText.toLowerCase()) {
            case "pendiente":
                statusClass = "status-pending";
                break;
            case "aceptado":
                statusClass = "status-accepted";
                break;
            case "rechazado":
                statusClass = "status-rejected";
                break;
            default:
                statusClass = "status-unknown";
        }
        lblStatus.setText("Estado: " + statusText);
        lblStatus.getStyleClass().add(statusClass);

        // Lista de platos/dishes
        VBox dishesBox = new VBox();
        dishesBox.setSpacing(4);
        Label lblDishesTitle = new Label("Productos:");
        lblDishesTitle.setFont(Font.font(null, FontWeight.BOLD, 14));
        dishesBox.getChildren().add(lblDishesTitle);

        if (order.getDishes() != null && !order.getDishes().isEmpty()) {
            for (String dish : order.getDishes()) {
                Label dishLabel = new Label("- " + dish);
                dishesBox.getChildren().add(dishLabel);
            }
        } else {
            dishesBox.getChildren().add(new Label("No hay productos."));
        }

        // Botones de acción
        HBox buttonsSection = new HBox();
        buttonsSection.setAlignment(Pos.CENTER);
        buttonsSection.getStyleClass().add("hbox-CardButtons");
        buttonsSection.setSpacing(10);

        Button btnEdit = new Button("Editar");
        Button btnDelete = new Button("Eliminar");

        btnEdit.getStyleClass().add("action-button");
        btnDelete.getStyleClass().add("action-button");

        /*
        btnEdit.setOnAction(event -> {
            selectedOrder = order;
            View view = new View(selectedOrder);
            view.loadModal(event, "/com/topglobal/dailyorder/views/admin/admin_edit_order.fxml", "Editar Orden");
        });

         */

        buttonsSection.getChildren().addAll(btnEdit, btnDelete);

        // Agregar todos los elementos a la card
        card.getChildren().addAll(lblOrderId, lblDate, lblTableId, lblStatus, dishesBox, buttonsSection);
        card.setSpacing(8);
        card.setPadding(new Insets(10));

        return card;
    }

    private String cssForStatus(String status) {
        if (status == null) return "status-default";
        String s = status.toLowerCase();
        if (s.contains("pendiente")) return "status-pending";
        if (s.contains("prepar")) return "status-prep";
        if (s.contains("lista")) return "status-ready";
        if (s.contains("aprob") || s.contains("entreg")) return "status-done";
        return "status-default";
    }

    private void markActive(Button active) {
        btnAll.getStyleClass().remove("tabButton-active");
        btnPending.getStyleClass().remove("tabButton-active");
        btnPrep.getStyleClass().remove("tabButton-active");
        btnReady.getStyleClass().remove("tabButton-active");
        btnApproved.getStyleClass().remove("tabButton-active");
        active.getStyleClass().add("tabButton-active");
    }
}