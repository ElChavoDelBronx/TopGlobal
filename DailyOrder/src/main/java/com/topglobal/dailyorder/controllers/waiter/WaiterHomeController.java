package com.topglobal.dailyorder.controllers.waiter;

import com.topglobal.dailyorder.controllers.UserController;
import com.topglobal.dailyorder.utils.SessionData;
import com.topglobal.dailyorder.utils.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class WaiterHomeController {
    private SessionData sessionData;
    @FXML private Label lblFullName;
    @FXML private Label lblUsername;
    @FXML private Label lblPhoneNumber;
    @FXML private Label lblEmail;
    @FXML private Label lblShift;

    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
        lblFullName.setText(sessionData.getUser().getName() + " " + sessionData.getUser().getFatherLastname() + " " + sessionData.getUser().getMotherLastname());
        lblUsername.setText(sessionData.getUser().getUser());
        lblPhoneNumber.setText(sessionData.getUser().getPhoneNumber());
        lblEmail.setText(sessionData.getUser().getEmail());
        lblShift.setText(sessionData.getUser().getShift());
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
    @FXML
    public void onAddOrder(ActionEvent event) {
        View view = new View();
        view.loadModal(event, "/com/topglobal/dailyorder/views/waiter/waiter_add_order.fxml", "Añadir Nueva Orden", sessionData);
    }
}
