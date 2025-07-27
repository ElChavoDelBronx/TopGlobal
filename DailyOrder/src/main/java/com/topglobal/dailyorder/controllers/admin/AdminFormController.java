package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.dao.EmployeeDAO;
import com.topglobal.dailyorder.models.users.Employee;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class AdminFormController {

    @FXML private TextField tfNombre;
    @FXML private TextField tfApellidoP;
    @FXML private TextField tfApellidoM;
    @FXML private TextField tfGenero;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField tfCurp;
    @FXML private ComboBox<String> cbEstadoNacimiento;
    @FXML private TextField tfEmail;
    @FXML private TextField tfNameUsuario;
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelar;
    @FXML private TextField tfNumeroTelefono;
    @FXML private ComboBox<String> cbPuesto;

    EmployeeDAO dao = new EmployeeDAO();

    private AnchorPane contentPane;

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    @FXML
    private void initialize() {
        /*
        try {
            List<Employee> personal = dao.findAllEmployees();
            cbEstadoNacimiento.setItems(FXCollections.observableArrayList(personal));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

         */
    }

    @FXML
    private void onRegistrar() {
        String nombre = tfNombre.getText().trim();
        String apellidoP = tfApellidoP.getText().trim();
        String apellidoM = tfApellidoM.getText().trim();
        String genero = tfGenero.getText().trim();
        LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
        String curp = tfCurp.getText().trim();
        //Employee estado = cbEstadoNacimiento.getSelectionModel().getSelectedItem();
        String email = tfEmail.getText().trim();
        String usuario = tfNameUsuario.getText().trim();
        String telefono = tfNumeroTelefono.getText().trim();
        //Employee puesto = cbPuesto.getSelectionModel().getSelectedItem();


        if(nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty() || genero.isEmpty()
                || fechaNacimiento == null || curp.isEmpty() || /*estado == null ||*/ email.isEmpty()
                || usuario.isEmpty() || telefono.isEmpty() /*|| puesto == null*/ ) {

            showAlert("ERROR", "Todos los campos son obligatorios");
            return;
        }
        Employee employee = new Employee();
        employee.setName(nombre);
        employee.setFatherLastname(apellidoP);
        employee.setMotherLastname(apellidoM);
        employee.setGender(genero);
        employee.setEmail(email);
        employee.setBirthday(fechaNacimiento);
        employee.setCurp(curp);


        try {
            dao.createEmployee(employee);
            showAlert("¡EXITO!", "Registro Exitoso");
            AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_form.fxml", contentPane);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelar() {
        AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_list.fxml", contentPane);
    }

    public void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
