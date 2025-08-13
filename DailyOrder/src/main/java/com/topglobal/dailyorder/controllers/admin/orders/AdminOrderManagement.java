package com.topglobal.dailyorder.controllers.admin.orders;

import com.topglobal.dailyorder.dao.FoodOrderDAO;
import com.topglobal.dailyorder.dao.MenuItemDAO;
import com.topglobal.dailyorder.models.objects.FoodOrder;
import com.topglobal.dailyorder.models.objects.MenuItem;
import com.topglobal.dailyorder.utils.SessionData;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminOrderManagement {
    private SessionData sessionData;
    @FXML private FlowPane cardContainer;
    @FXML private Button btnAll;
    @FXML private Button btnPending;
    @FXML private Button btnPrep;
    @FXML private Button btnReady;
    @FXML private Button btnApproved;

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    /*private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() {
        loadAllOrders(null);
    }

    @FXML
    private void loadAllOrders(ActionEvent event) {
        try {
            render(FoodOrderDAO.findAll());
            markActive(btnAll);
        } catch (SQLException e) { e.printStackTrace(); }
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
            cardContainer.getChildren().add(buildCard(o));
        }
    }

    private VBox buildCard(FoodOrder o) {
        VBox card = new VBox(10);
        card.getStyleClass().add("order-card");
        card.setPrefWidth(520);
        card.setPadding(new Insets(12));

        // Encabezado
        HBox header = new HBox(10);
        Label folio = new Label("Orden #" + o.getOrderId()); folio.getStyleClass().add("card-title");
        String fechaTxt = o.getOrderDate() != null ? o.getOrderDate().format(fmt) : "";
        Label fecha = new Label(fechaTxt); fecha.getStyleClass().add("muted");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Label status = new Label(o.getOrderStatus());
        status.getStyleClass().add(cssForStatus(o.getOrderStatus()));
        header.getChildren().addAll(folio, fecha, spacer, status);

        // Meta
        Label meta = new Label("Mesa: " + o.getDiningTableId() + " — Mesero ID: " + o.getWaiterId());
        meta.getStyleClass().add("muted");

        // Lista de platillos
        VBox itemsBox = new VBox(4);
        try {
            List<MenuItem> items = FoodOrderDAO.findItemsByOrderId(o.getOrderId());
            for (MenuItem it : items) {
                String name = it.getName() != null ? it.getName() : ("Item " + it.getId());
                Label li = new Label("• " + it.getQuantity() + "x " + name);
                li.getStyleClass().add("card-text");
                itemsBox.getChildren().add(li);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Acciones
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        Button cobrar = new Button("Aprobar"); cobrar.getStyleClass().add("btn-light");
        Button rechazar = new Button("Rechazar"); rechazar.getStyleClass().add("btn-outline");
        actions.getChildren().addAll(cobrar, rechazar);

        card.getChildren().addAll(header, meta, itemsBox, actions);
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
    }*/
}
