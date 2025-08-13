package com.topglobal.dailyorder.controllers.waiter;

import com.topglobal.dailyorder.dao.FoodOrderDAO;
import com.topglobal.dailyorder.dao.MenuItemDAO;
import com.topglobal.dailyorder.models.objects.MenuItem;
import com.topglobal.dailyorder.utils.View;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class WaiterAddOrder {

    @FXML private ComboBox<MenuItem> comboPlatillos;
    @FXML private Label labelCosto;
    @FXML private Spinner<Integer> spinnerCantidad;
    @FXML private TableView<MenuItem> tablaOrden;
    @FXML private TableColumn<MenuItem, String> colNombrePlatillo;
    @FXML private TableColumn<MenuItem, Double> colPrecioUnitario;
    @FXML private TableColumn<MenuItem, Integer> colCantidad;
    @FXML private TableColumn<MenuItem, Double> colSubtotal;
    @FXML private Label labelTotal;
    @FXML private Button btnCancel;
    @FXML private Button btnAddOrder;

    private final ObservableList<MenuItem> platillosOrdenados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        spinnerCantidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1)
        );

        try {
            comboPlatillos.setItems(FXCollections.observableArrayList(MenuItemDAO.findAll()));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar menú").showAndWait();
        }

        comboPlatillos.setOnAction(e -> {
            MenuItem m = comboPlatillos.getValue();
            if (m != null) labelCosto.setText(String.format("$ %.2f", m.getCost()));
        });

        // Mostrar solo el nombre en el ComboBox
        comboPlatillos.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(MenuItem item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item.getName());
            }
        });
        comboPlatillos.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(MenuItem item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item.getName());
            }
        });

        // Columnas de la tabla
        colNombrePlatillo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getName())
        );
        colPrecioUnitario.setCellValueFactory(c ->
                new SimpleDoubleProperty(c.getValue().getCost()).asObject()
        );
        colCantidad.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getQuantity()).asObject()
        );
        colSubtotal.setCellValueFactory(c ->
                new SimpleDoubleProperty(c.getValue().getCost() * c.getValue().getQuantity()).asObject()
        );

        tablaOrden.setItems(platillosOrdenados);

        // Columna "Eliminar"
        TableColumn<MenuItem, Void> colEliminar = new TableColumn<>("Eliminar");
        colEliminar.setPrefWidth(90);
        colEliminar.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("✖");

            {
                btn.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold;");
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #e60000; -fx-text-fill: white; -fx-font-weight: bold;"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold;"));

                btn.setOnAction(e -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    if (item == null) return;

                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "¿Deseas eliminar este platillo de la orden?",
                            ButtonType.YES, ButtonType.NO);
                    confirm.setHeaderText("Confirmar eliminación");
                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            platillosOrdenados.remove(item);
                            tablaOrden.refresh();
                            actualizarTotal();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void value, boolean empty) {
                super.updateItem(value, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tablaOrden.getColumns().add(colEliminar);

        labelCosto.setText("$ 0.00");
        labelTotal.setText("$ 0.00");
    }

    @FXML
    public void onAgregarPlatillo() {
        MenuItem seleccionado = comboPlatillos.getValue();
        if (seleccionado == null) return;

        int cantidad = spinnerCantidad.getValue();

        for (MenuItem item : platillosOrdenados) {
            if (item.getId() == seleccionado.getId()) {
                item.setQuantity(item.getQuantity() + cantidad);
                tablaOrden.refresh();
                actualizarTotal();
                return;
            }
        }

        MenuItem nuevo = new MenuItem();
        nuevo.setId(seleccionado.getId());
        nuevo.setName(seleccionado.getName());
        nuevo.setCost(seleccionado.getCost());
        nuevo.setQuantity(cantidad);
        platillosOrdenados.add(nuevo);
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = platillosOrdenados.stream()
                .mapToDouble(p -> p.getCost() * p.getQuantity())
                .sum();
        labelTotal.setText(String.format("$ %.2f", total));
    }

    @FXML
    public void onAddOrder(ActionEvent event) {
        if (platillosOrdenados.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "No se han agregado platillos.").showAndWait();
            return;
        }

        try {
            double totalCost = platillosOrdenados.stream()
                    .mapToDouble(p -> p.getCost() * p.getQuantity())
                    .sum();

            // IDs de mesa y mesero (aquí puedes obtenerlos dinámicamente)
            int tableId = 1;
            int waiterId = 1;

            FoodOrderDAO.createOrderSimple(tableId, waiterId, "Pendiente", totalCost, platillosOrdenados);

            new Alert(Alert.AlertType.INFORMATION, "Orden guardada exitosamente").showAndWait();
            View.closeWindow(btnAddOrder);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al guardar: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void onCancel() {
        View.closeWindow(btnCancel);
    }
}