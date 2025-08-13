package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.dao.EmployeeDAO;
import com.topglobal.dailyorder.dao.MenuItemDAO;
import com.topglobal.dailyorder.models.objects.DiningTable;
import com.topglobal.dailyorder.models.users.Admin;
import com.topglobal.dailyorder.models.users.Employee;
import com.topglobal.dailyorder.utils.SessionData;
import com.topglobal.dailyorder.utils.View;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import org.controlsfx.control.ToggleSwitch;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminListController implements Initializable {
    private SessionData sessionData;
    @FXML private TableView<Employee> tablePersonal;
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

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
        if(sessionData.getEmployees().isEmpty()){
            loadPersonal();
        }else{
            listaEmpleados.setAll(sessionData.getEmployees());
        }
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
            private final Button btnVer = new Button();
            private final Button btnEditar = new Button();
            private final Button btnCambiarEstatus = new Button("X/✔");


            private final HBox pane = new HBox(5, btnVer, btnEditar, btnCambiarEstatus);

            {

                //SVGPath traza los iconos
                SVGPath editIcon = new SVGPath();
                SVGPath eyeIcon1 = new SVGPath();
                SVGPath eyeIcon2 = new SVGPath();
                editIcon.setContent("M28.8799 11.56L8.39591 32.04C7.78687 32.6488 7.39056 33.4379 7.26591 34.29L6.15991 41.838L13.7099 40.732C14.5622 40.6068 15.3514 40.2098 15.9599 39.6L36.4399 19.12M28.8799 11.56L33.3379 7.09999C33.6351 6.80277 33.9879 6.56699 34.3762 6.40613C34.7644 6.24527 35.1806 6.16248 35.6009 6.16248C36.0212 6.16248 36.4374 6.24527 36.8257 6.40613C37.214 6.56699 37.5668 6.80277 37.8639 7.09999L40.8999 10.136C41.1971 10.4331 41.4329 10.7859 41.5938 11.1742C41.7546 11.5625 41.8374 11.9787 41.8374 12.399C41.8374 12.8193 41.7546 13.2355 41.5938 13.6237C41.4329 14.012 41.1971 14.3648 40.8999 14.662L36.4399 19.12M28.8799 11.56L36.4399 19.12");
                eyeIcon1.setContent("M15.125 22C15.125 20.1766 15.8493 18.428 17.1386 17.1386C18.428 15.8493 20.1766 15.125 22 15.125C23.8234 15.125 25.572 15.8493 26.8614 17.1386C28.1507 18.428 28.875 20.1766 28.875 22C28.875 23.8234 28.1507 25.572 26.8614 26.8614C25.572 28.1507 23.8234 28.875 22 28.875C20.1766 28.875 18.428 28.1507 17.1386 26.8614C15.8493 25.572 15.125 23.8234 15.125 22ZM22 17.875C20.906 17.875 19.8568 18.3096 19.0832 19.0832C18.3096 19.8568 17.875 20.906 17.875 22C17.875 23.094 18.3096 24.1432 19.0832 24.9168C19.8568 25.6904 20.906 26.125 22 26.125C23.094 26.125 24.1432 25.6904 24.9168 24.9168C25.6904 24.1432 26.125 23.094 26.125 22C26.125 20.906 25.6904 19.8568 24.9168 19.0832C24.1432 18.3096 23.094 17.875 22 17.875Z");
                eyeIcon2.setContent("M7.9255 19.5177C7.15733 20.625 6.875 21.4922 6.875 22C6.875 22.5078 7.15733 23.375 7.9255 24.4823C8.66983 25.5512 9.7735 26.7117 11.1705 27.7842C13.97 29.9328 17.8072 31.625 22 31.625C26.1928 31.625 30.03 29.9328 32.8295 27.7842C34.2265 26.7117 35.3302 25.5512 36.0745 24.4823C36.8427 23.375 37.125 22.5078 37.125 22C37.125 21.4922 36.8427 20.625 36.0745 19.5177C35.3302 18.4488 34.2265 17.2883 32.8295 16.2158C30.03 14.0672 26.1928 12.375 22 12.375C17.8072 12.375 13.97 14.0672 11.1705 16.2158C9.7735 17.2883 8.66983 18.4488 7.9255 19.5177ZM9.49483 14.0342C12.6683 11.5995 17.0793 9.625 22 9.625C26.9207 9.625 31.3317 11.5995 34.5033 14.0342C36.0928 15.2533 37.4055 16.6137 38.3332 17.9502C39.2352 19.25 39.875 20.6745 39.875 22C39.875 23.3255 39.2333 24.75 38.3332 26.0498C37.4055 27.3863 36.0928 28.7448 34.5052 29.9658C31.3335 32.4005 26.9207 34.375 22 34.375C17.0793 34.375 12.6683 32.4005 9.49667 29.9658C7.90717 28.7467 6.5945 27.3863 5.66683 26.0498C4.76667 24.75 4.125 23.3255 4.125 22C4.125 20.6745 4.76667 19.25 5.66683 17.9502C6.5945 16.6137 7.90717 15.2552 9.49483 14.0342Z");
                Group eyeGroup = new Group(eyeIcon1, eyeIcon2);
                btnEditar.setGraphic(editIcon);
                btnVer.setGraphic(eyeGroup);
                pane.setAlignment(Pos.CENTER);
                pane.getChildren().forEach(b -> b.getStyleClass().add("action-btn"));

                // Asignar acciones a cada botón
                btnVer.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    System.out.println("ID del empleado: " + emp.getId());
                    sessionData.setSelectedEmployee(emp);
                    onWatchEmployee(emp.getId());
                });
                btnEditar.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    System.out.println("ID del empleado: " + emp.getId());
                    sessionData.setSelectedEmployee(emp);
                    onEditEmployee(emp.getId());
                });

                btnCambiarEstatus.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    sessionData.setSelectedEmployee(emp);
                    onChangeStatus(emp.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });




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
        View.loadView("/com/topglobal/dailyorder/views/admin/admin_form.fxml", contentPane, sessionData);

    }

    //Metodo para visualizar información completa de un solo empleado visible desde la tabla de empleados
    @FXML
    private void onWatchEmployee( int id) {
        System.out.println("Click");
        EmpleadoContexto.idEmpleadoSeleccionado = id;
        View.loadView("/com/topglobal/dailyorder/views/admin/admin_watch_employee.fxml", contentPane, sessionData);
    }

    //Metodo para cambiar a vista de editar información completa de un solo empleado
    @FXML
    private void onEditEmployee(int id) {
        System.out.println("Click");
        EmpleadoContexto.idEmpleadoSeleccionado = id;
        View.loadView("/com/topglobal/dailyorder/views/admin/admin_edit_employee.fxml", contentPane, sessionData);

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
