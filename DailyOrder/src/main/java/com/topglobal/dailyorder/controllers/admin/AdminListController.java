package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.dao.EmployeeDAO;
import com.topglobal.dailyorder.models.users.Admin;
import com.topglobal.dailyorder.models.users.Employee;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminListController implements Initializable {

    @FXML
    private TableView<Employee> tablePersonal;
    @FXML private TableColumn<Employee, Void> colNumero;
    @FXML private TableColumn<Employee, String> colNombre;
    @FXML private TableColumn<Employee, String> colApellidoP;
    @FXML private TableColumn<Employee, String> colApellidoM;
    @FXML private TableColumn<Employee, String> colUsuario;
    @FXML private TableColumn<Employee, String> colTelefono;
    @FXML private TableColumn<Employee, String> colCorreo;
    @FXML private TableColumn<Employee, String> colCargo;
    @FXML private TableColumn<Employee, String> colEstatus;
    @FXML private TableColumn<Employee, Void> colAcciones;

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    private AnchorPane contentPane;

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNumero.setCellFactory(col -> new TableCell<Employee, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getIndex() >= getTableView().getItems().size()) {
                    setText(null);
                } else {
                    setText(String.valueOf(getTableRow().getIndex() + 1));
                }
            }
        });
        colNombre.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getName())
        );
        colApellidoP.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getFatherLastname())
        );
        colApellidoM.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getMotherLastname())
        );
        colUsuario.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getUser())
        );
        colTelefono.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getPhoneNumber())
        );
        colCorreo.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getEmail())
        );
        colCargo.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getRole())
        );
        colEstatus.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(String.valueOf(data.getValue().getStatus()))
        );
        // Column “Acciones”
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnVer = new Button("Ver");
            private final Button btnEditar = new Button("Editar");
            private final Button btnCambiarEstatus = new Button("Cambiar Estatus");
            private final HBox pane = new HBox(5, btnVer, btnEditar, btnCambiarEstatus);

            {
                pane.setAlignment(Pos.CENTER);
                pane.getChildren().forEach(b -> b.getStyleClass().add("action-btn"));

                // Asignar acciones a cada botón
                btnVer.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    onWatchEmployee(emp.getId()); // ✅ OK
                });
                btnEditar.setOnAction(e -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    onEditEmployee(emp);
                });

                btnCambiarEstatus.setOnAction(e -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    onDeleteEmployee(emp);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });



        // Carga Datos
        loadPersonal();

    }

    private void loadPersonal() {
        try {
            List<Employee> personal = employeeDAO.findAllEmployees();
            tablePersonal.setItems(FXCollections.observableArrayList(personal));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public class EmpleadoContexto {
        public static int idEmpleadoSeleccionado = -1;
    }

    @FXML
    private void onCreateEmployee(ActionEvent event) {
        System.out.println("Click");
        AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_form.fxml", contentPane);

    }

    @FXML
    private void onWatchEmployee( int id) {
        System.out.println("Click");
        EmpleadoContexto.idEmpleadoSeleccionado = id;
        AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_watch_employee.fxml", contentPane);
    }

    @FXML
    private void onEditEmployee(Employee emp) {}

    @FXML
    private void onDeleteEmployee(Employee emp) {}
}
