package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.controllers.UserController;
import com.topglobal.dailyorder.dao.EmployeeDAO;
import com.topglobal.dailyorder.models.users.Employee;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    @FXML private TableColumn<Employee, String> colAcciones;

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

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

        colCorreo.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getEmail())
        );
        colTelefono.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getPhoneNumber())
        );
        colCargo.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getRole())
        );


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
}
