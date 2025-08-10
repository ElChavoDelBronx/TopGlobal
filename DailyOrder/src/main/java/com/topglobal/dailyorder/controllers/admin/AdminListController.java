package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.dao.EmployeeDAO;
import com.topglobal.dailyorder.models.users.Admin;
import com.topglobal.dailyorder.models.users.Employee;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.text.Font;

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
    //@FXML private ComboBox<String> cbFiltroTipo;
    //@FXML private ComboBox<String> cbValorFiltro;
    @FXML private Button btnFiltrar;
    @FXML private TableView<Employee> tablaEmpleados;
    private Employee empleadoActual;
    EmployeeDAO dao = new EmployeeDAO();
    private final ObservableList<Employee> listaEmpleados = FXCollections.observableArrayList();

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    private AnchorPane contentPane;

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    //Inicializa tipografia, obtine ID por cada empleado y carga información en la tabla
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        cbFiltroTipo.getItems().addAll("Puesto", "Horario", "Nombre (A-Z)", "Apellido paterno (A-Z)");

        // Escucha el tipo de filtro seleccionado
        cbFiltroTipo.valueProperty().addListener((obs, oldVal, newVal) -> {
            cbValorFiltro.getItems().clear();

            switch (newVal) {
                case "Puesto":
                    cbValorFiltro.getItems().addAll("Administrador", "Lider de meseros", "Mesero"); // según tus roles
                    break;
                case "Horario":
                    cbValorFiltro.getItems().addAll("Vespertino", "Matutino"); // ajusta según tu DB
                    break;
                default:
                    cbValorFiltro.setDisable(true);
            }
        });
        */
        tablePersonal.setItems(listaEmpleados);
        int id = AdminListController.EmpleadoContexto.idEmpleadoSeleccionado;
        if (id != -1) {
            try {
                empleadoActual = dao.findEmployeeById(id);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);

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
        colEstatus.setCellValueFactory(data -> {
            String estadoTexto = data.getValue().getStatus() == 1 ? "Activo" : "Inactivo";
            return new ReadOnlyStringWrapper(estadoTexto);
        });
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
                    System.out.println("ID del empleado: " + emp.getId());
                    onWatchEmployee(emp.getId());
                });
                btnEditar.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    System.out.println("ID del empleado: " + emp.getId());
                    onEditEmployee(emp.getId());
                });

                btnCambiarEstatus.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    onChangeStatus(emp.getId());
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

    //Realiza consulta a la base de datos
    private void loadPersonal() {
        try {
            List<Employee> personal = employeeDAO.findAllEmployees();
            listaEmpleados.setAll(personal); // Esto actualiza la tabla sin necesidad de recargar vista
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //Reconoce ID del empleado según su registro
    public class EmpleadoContexto {
        public static int idEmpleadoSeleccionado = -1;
    }

    //Metodo para cambiar de vista al formulario de registro de empleados
    @FXML
    private void onCreateEmployee(ActionEvent event) {
        System.out.println("Click");
        AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_form.fxml", contentPane);

    }

    //Metodo para visualizar información completa de un solo empleado visible desde la tabla de empleados
    @FXML
    private void onWatchEmployee( int id) {
        System.out.println("Click");
        EmpleadoContexto.idEmpleadoSeleccionado = id;
        AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_watch_employee.fxml", contentPane);
    }

    //Metodo para cambiar a vista de editar información completa de un solo empleado
    @FXML
    private void onEditEmployee(int id) {
        System.out.println("Click");
        EmpleadoContexto.idEmpleadoSeleccionado = id;
        AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_edit_employee.fxml", contentPane);

    }

    //Metodo para cambiar estatus de un solo empleado
    @FXML
    private void onChangeStatus(int id) {
        try {
            Employee empleado = employeeDAO.findEmployeeById(id);
            if (empleado != null) {

                int nuevoStatus = empleado.getStatus() == 1 ? 0 : 1;
                empleado.setStatus(nuevoStatus);

                employeeDAO.changeStatus(empleado);
                System.out.println("Status actualizado para empleado con ID: " + id);

                loadPersonal(); // Actualiza la tabla después de cambiar estado
            } else {
                System.out.println("No se encontró el empleado con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @FXML
    private void onFiltrar() {
        String tipo = cbFiltroTipo.getValue();
        String valor = cbValorFiltro.getValue();

        List<Employee> empleadosFiltrados;

        switch (tipo) {
            case "Puesto":
                empleadosFiltrados = employeeDAO.findByRole(valor);
                break;
            case "Horario":
                empleadosFiltrados = employeeDAO.findByShift(valor);
                break;
            case "Nombre (A-Z)":
                empleadosFiltrados = employeeDAO.orderByNameAsc();
                break;
            case "Apellido paterno (A-Z)":
                empleadosFiltrados = employeeDAO.orderByFatherLastnameAsc();
                break;
            default:
                empleadosFiltrados = employeeDAO.findAllEmployees();
        }

        tablaEmpleados.setItems(FXCollections.observableArrayList(empleadosFiltrados));
    }
*/


}
