package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.dao.EmployeeDAO;
import com.topglobal.dailyorder.models.users.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

public class AdminEditEmployeeController implements Initializable {

    @FXML private TextField tfNombre;
    @FXML private TextField tfApellidoP;
    @FXML private TextField tfApellidoM;
    @FXML private ComboBox<String> cbGenero;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField tfCurp;
    @FXML private ComboBox<String> cbEstadoNacimiento;
    @FXML private TextField tfEmail;
    @FXML private TextField tfNameUsuario;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private TextField tfNumeroTelefono;
    @FXML private ComboBox<String> cbPuesto;
    @FXML private ComboBox<String> cbHorario;
    @FXML int status = 1;
    @FXML private TextField tfEdad;

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    EmployeeDAO dao = new EmployeeDAO();

    private AnchorPane contentPane;

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int id = AdminListController.EmpleadoContexto.idEmpleadoSeleccionado;
        if (id != -1) {
            try {
                Employee empleado = employeeDAO.findEmployeeById(id);
                if (empleado != null) {
                    loadEmployee(empleado);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        dpFechaNacimiento.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                int edad = calcularEdad(newDate);
                tfEdad.setText(String.valueOf(edad));
            } else {
                tfEdad.setText("");
            }
        });

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
    }

    private void loadEmployee(Employee empleado) {
        tfNombre.setText(empleado.getName());
        tfApellidoP.setText(empleado.getFatherLastname());
        tfApellidoM.setText(empleado.getMotherLastname());
        cbGenero.setValue(empleado.getGender());
        tfCurp.setText(empleado.getCurp());
        cbHorario.setValue(empleado.getShift());
        tfEmail.setText(empleado.getEmail());
        tfNameUsuario.setText(empleado.getUser());
        tfNumeroTelefono.setText(empleado.getPhoneNumber());
        cbPuesto.setValue(empleado.getRole());

        if (empleado.getBirthday() != null) {
            dpFechaNacimiento.setValue(empleado.getBirthday());
            tfEdad.setText(String.valueOf(Period.between(empleado.getBirthday(), LocalDate.now()).getYears()));
        } else {
            dpFechaNacimiento.setValue(null);
            tfEdad.setText("");
        }
    }

    @FXML
    private void onCancelar(ActionEvent event) {
        AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_watch_employee.fxml", contentPane);
    }

    @FXML
    private void onGuardar() {
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

        try {
            dao.updateEmployee(employee);
            showAlert("¡ÉXITO!", "Guardado con exito");
            AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_list.fxml", contentPane);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("ERROR", "Ocurrió un error al registrar al empleado.");
        }

    }

    public void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }






}
