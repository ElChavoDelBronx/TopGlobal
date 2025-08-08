package com.topglobal.dailyorder.controllers.waiter;

import com.topglobal.dailyorder.controllers.UserController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class WaiterHomeController extends UserController {
    @FXML
    private Label lblFullName;
    @FXML private Label lblUsername;
    @FXML private Label lblPhoneNumber;
    @FXML private Label lblEmail;
    @FXML private Label lblShift;

    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
    }
    @Override
    public void setInfo() {
        if (this.user != null) {
            lblFullName.setText(user.getName() + " " + user.getFatherLastname() + " " + user.getMotherLastname());
            lblUsername.setText(user.getUser());
            lblPhoneNumber.setText(user.getPhoneNumber());
            lblEmail.setText(user.getEmail());
            lblShift.setText(user.getShift());
        }
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
