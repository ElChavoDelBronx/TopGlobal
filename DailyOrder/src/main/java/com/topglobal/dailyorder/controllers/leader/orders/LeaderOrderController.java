package com.topglobal.dailyorder.controllers.leader.orders;

import com.topglobal.dailyorder.dao.DiningTableDAO;
import com.topglobal.dailyorder.dao.FoodOrderDAO;
import com.topglobal.dailyorder.models.objects.DiningTable;
import com.topglobal.dailyorder.models.objects.FoodOrder;
import com.topglobal.dailyorder.utils.CustomAlert;
import com.topglobal.dailyorder.utils.SessionData;
import com.topglobal.dailyorder.utils.View;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LeaderOrderController implements Initializable {
    private FoodOrder selectedOrder;
    private SessionData sessionData;
    @FXML
    private FlowPane flowPaneOrders;  // debe coincidir con fx:id en FXML

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
        loadOrders();
    }

    protected void loadOrders() {
        System.out.println("Cargando órdenes...");
        try {
            List<FoodOrder> orders = FoodOrderDAO.getAllOrders();
            System.out.println("Pedidos encontrados: " + orders.size()); // Para debug
            for (FoodOrder order : orders) {
                VBox card = createOrderCard(order);
                flowPaneOrders.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CustomAlert.showErrorAlert("ERROR", "No se pudieron cargar los pedidos.");
        }
    }

    private VBox createOrderCard(FoodOrder order) {
        VBox card = new VBox();
        card.getStyleClass().add("card");

        // Etiqueta con ID de la orden
        Label lblOrderId = new Label("Folio #" + order.getDailyFolio());
        lblOrderId.setFont(Font.font(16));

        // Etiqueta con nombre del mesero
        Label lblWaiterName = new Label("Mesero: " + order.getWaiterName());

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

        btnEdit.setOnAction(event -> {
            selectedOrder = order;
            View view = new View(selectedOrder);
            view.loadModal(event, "/com/topglobal/dailyorder/views/admin/admin_edit_order.fxml", "Editar Orden", sessionData);
        });

        buttonsSection.getChildren().addAll(btnEdit, btnDelete);

        // Agregar todos los elementos a la card
        card.getChildren().addAll(lblOrderId, lblWaiterName, lblDate, lblStatus, dishesBox, buttonsSection);
        card.setSpacing(8);
        card.setPadding(new Insets(10));

        return card;
    }

    @FXML FlowPane fpOrders;
    public void initialize() {

    }
    @FXML
    public void onAll(){

    }
    @FXML
    public void onPending(){

    }
    @FXML
    public void onPreparation(){

    }
    @FXML
    public void onReady(){

    }
    @FXML
    public void onApproved(){

    }
}
