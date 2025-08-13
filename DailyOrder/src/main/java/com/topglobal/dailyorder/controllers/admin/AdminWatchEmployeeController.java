package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.dao.EmployeeDAO;
import com.topglobal.dailyorder.models.users.Employee;
import com.topglobal.dailyorder.utils.SessionData;
import com.topglobal.dailyorder.utils.View;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;

public class AdminWatchEmployeeController {
    private SessionData sessionData;
    @FXML private TextField tfNombre;
    @FXML private TextField tfApellidoP;
    @FXML private TextField tfApellidoM;
    @FXML private TextField tfGenero;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField tfCurp;
    @FXML private TextField tfEstadoNacimiento;
    @FXML private TextField tfEmail;
    @FXML private TextField tfNameUsuario;
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelar;
    @FXML private TextField tfNumeroTelefono;
    @FXML private TextField tfPuesto;
    @FXML private TextField tfHorario;
    @FXML int status = 1;
    @FXML private TextField tfEdad;

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    private AnchorPane contentPane;

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
        loadEmployee(sessionData.getSelectedEmployee());
    }

    //Obtine ID de empleado seleccionado
    /*@Override
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
    }*/

    //Carga información completa del empleado
    private void loadEmployee(Employee empleado) {
        tfNombre.setText(empleado.getName());
        tfApellidoP.setText(empleado.getFatherLastname());
        tfApellidoM.setText(empleado.getMotherLastname());
        tfGenero.setText(empleado.getGender());
        tfCurp.setText(empleado.getCurp());
        tfHorario.setText(empleado.getShift());
        tfEmail.setText(empleado.getEmail());
        tfNameUsuario.setText(empleado.getUser());
        tfNumeroTelefono.setText(empleado.getPhoneNumber());
        tfPuesto.setText(empleado.getRole());

        if (empleado.getBirthday() != null) {
            dpFechaNacimiento.setValue(empleado.getBirthday());
            tfEdad.setText(String.valueOf(Period.between(empleado.getBirthday(), LocalDate.now()).getYears()));
        } else {
            dpFechaNacimiento.setValue(null);
            tfEdad.setText("");
        }
    }

    //Regresa a tabla de empleados
    @FXML
    private void onReturnList(ActionEvent event) {
        View.loadView("/com/topglobal/dailyorder/views/admin/admin_list.fxml", contentPane, sessionData);
    }

    //Cambia a vista para editar información del empleado sleecionado
    @FXML
    private void onEditEmployee(ActionEvent event) {
        View.loadView( "/com/topglobal/dailyorder/views/admin/admin_edit_employee.fxml", contentPane, sessionData);

    }






}
