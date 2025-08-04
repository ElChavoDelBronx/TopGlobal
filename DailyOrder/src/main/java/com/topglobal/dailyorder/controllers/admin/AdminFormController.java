package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.dao.EmployeeDAO;
import com.topglobal.dailyorder.models.users.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class AdminFormController {

    @FXML private TextField tfNombre;
    @FXML private TextField tfApellidoP;
    @FXML private TextField tfApellidoM;
    @FXML private ComboBox<String> cbGenero;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField tfCurp;
    @FXML private ComboBox<String> cbEstadoNacimiento;
    @FXML private TextField tfEmail;
    @FXML private TextField tfNameUsuario;
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelar;
    @FXML private TextField tfNumeroTelefono;
    @FXML private ComboBox<String> cbPuesto;
    @FXML private ComboBox<String> cbHorario;
    @FXML int status = 1;
    @FXML private TextField tfEdad;

    EmployeeDAO dao = new EmployeeDAO();

    private AnchorPane contentPane;

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    @FXML
    private void initialize() {
        try {
            dpFechaNacimiento.valueProperty().addListener((obs, oldDate, newDate) -> {
                if (newDate != null) {
                    int edad = calcularEdad(newDate);
                    tfEdad.setText(String.valueOf(edad));
                } else {
                    tfEdad.setText("");
                }
            });
            /*
            ObservableList<String> estados = FXCollections.observableArrayList(
                    "Aguascalientes", "Baja California", "Baja California Sur", "Campeche",
                    "Chiapas", "Chihuahua", "Ciudad de México", "Coahuila", "Colima", "Durango",
                    "Estado de México", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco",
                    "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla",
                    "Querétaro", "Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora",
                    "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas"
            );
            */
            ObservableList<String> genero = FXCollections.observableArrayList(
                    "Masculino", "Femenino", "Otro"
            );
            ObservableList<String> puesto = FXCollections.observableArrayList(
                    "Administrador", "Lider de meseros", "Mesero"
            );
            ObservableList<String> horario = FXCollections.observableArrayList(
                    "Matutino", "Vespertino"
            );

            //cbEstadoNacimiento.setItems(estados);
            cbGenero.setItems(genero);
            cbPuesto.setItems(puesto);
            cbHorario.setItems(horario);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void onRegistrar() {
        String nombre = tfNombre.getText().trim();
        String apellidoP = tfApellidoP.getText().trim();
        String apellidoM = tfApellidoM.getText().trim();
        String telefono = tfNumeroTelefono.getText().trim();
        String puesto = cbPuesto.getValue();
        String horario = cbHorario.getValue();
        String genero = cbGenero.getValue();
        LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
        String curp = tfCurp.getText().trim();
        //String estados = cbEstadoNacimiento.getValue();
        String email = tfEmail.getText().trim();
        String usuario = tfNameUsuario.getText().trim();
        int status = 1;



        if (nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty() || telefono.isEmpty() || puesto.isEmpty() || horario == null || genero.isEmpty()
                || fechaNacimiento == null || curp.isEmpty() || email.isEmpty() || usuario.isEmpty()) {

            showAlert("ERROR", "Todos los campos son obligatorios");
            return;
        }

        Employee employee = new Employee();
        employee.setName(nombre);
        employee.setFatherLastname(apellidoP);
        employee.setMotherLastname(apellidoM);
        employee.setPhoneNumber(telefono);
        employee.setRole(puesto);
        employee.setShift(horario);
        employee.setGender(genero);
        employee.setBirthday(fechaNacimiento);
        employee.setCurp(curp);
        employee.setEmail(email);
        employee.setUser(usuario);         // 👈 este dato ya es obligatorio
        employee.setStatus(1);             // 👈 por defecto puedes poner 1 (activo) o el que uses

        try {
            dao.createEmployee(employee);
            showAlert("¡ÉXITO!", "Registro Exitoso");
            AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_list.fxml", contentPane);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("ERROR", "Ocurrió un error al registrar al empleado.");
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

    // Método para calcular edad en años desde una fecha
    private int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}


