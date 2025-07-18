package com.topglobal.dailyorder.controllers.waiter;

import com.topglobal.dailyorder.controllers.UserController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class WaiterController extends UserController {

    @FXML private Label lblUsuario;
    @FXML private Label lblTelefono;
    @FXML private Label lblCorreo;
    @FXML private Label lblTurno;

    @Override
    public void setInfo() {
        // Simulación de datos, puedes cambiarlos desde tu UserController base
        lblUsuario.setText("MeseroChespi88");
        lblTelefono.setText("777-876-5432");
        lblCorreo.setText("gutierritos@empresa.com");
        lblTurno.setText("Vespertino");
    }

    @FXML
    private void marcarEntrada() {
        mostrarAlerta("Entrada marcada correctamente.");
    }

    @FXML
    private void marcarDescanso() {
        mostrarAlerta("Descanso registrado.");
    }

    @FXML
    private void marcarSalida() {
        mostrarAlerta("Salida registrada.");
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registro");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
