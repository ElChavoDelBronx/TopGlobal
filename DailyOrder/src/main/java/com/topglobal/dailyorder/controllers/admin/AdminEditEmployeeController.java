package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.dao.EmployeeDAO;
import com.topglobal.dailyorder.models.users.Employee;
import com.topglobal.dailyorder.utils.SessionData;
import com.topglobal.dailyorder.utils.View;
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
    private SessionData sessionData;
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

    private Employee empleadoActual;
    EmployeeDAO dao = new EmployeeDAO();

    //Setter utilizado para cargar vista central
    private AnchorPane contentPane;
    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
        loadEmployee(sessionData.getSelectedEmployee());
    }

    //Inicializa menus desplegables, calculo de fecha actual, obtine id
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*int id = AdminListController.EmpleadoContexto.idEmpleadoSeleccionado;
        if (id != -1) {
            try {
                empleadoActual = dao.findEmployeeById(id);
                if (empleadoActual != null) {
                    loadEmployee(empleadoActual);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

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

    //Metodo para cargar información de un solo empleado
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

    //Carga vista para visualizar información del empleado
    @FXML
    private void onCancelar(ActionEvent event) {
        View.loadView("/com/topglobal/dailyorder/views/admin/admin_watch_employee.fxml", contentPane, sessionData);
    }

    //Guarda modificaciones en la información del usuario y cambia a vista de tabla de empleados
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
        String email = tfEmail.getText().trim();
        String usuario = tfNameUsuario.getText().trim();

        if (empleadoActual == null) {
            showAlert("ERROR", "Empleado no válido");
            return;
        }

        empleadoActual.setName(nombre);
        empleadoActual.setFatherLastname(apellidoP);
        empleadoActual.setMotherLastname(apellidoM);
        empleadoActual.setPhoneNumber(telefono);
        empleadoActual.setRole(puesto);
        empleadoActual.setShift(horario);
        empleadoActual.setGender(genero);
        empleadoActual.setBirthday(fechaNacimiento);
        empleadoActual.setCurp(curp);
        empleadoActual.setEmail(email);
        empleadoActual.setUser(usuario);

        try {
            System.out.println("ID del empleado a actualizar: " + empleadoActual.getId());
            dao.updateEmployee(empleadoActual);
            showAlert("¡ÉXITO!", "Guardado con éxito");
            View.loadView("/com/topglobal/dailyorder/views/admin/admin_list.fxml", contentPane, sessionData);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("ERROR", "Ocurrió un error al registrar al empleado.");
        }


    }

    //Metodo utilizado para mostrar POP-POPS
    public void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    //Metodo utilizado para calcular edad apartir de la fecha actual y la fecha ingresada en formulario
    private int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

}
