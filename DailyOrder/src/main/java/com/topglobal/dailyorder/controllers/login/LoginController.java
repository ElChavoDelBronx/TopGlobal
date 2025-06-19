package com.topglobal.dailyorder.controllers.login;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML
    private Button btnLogin;

    @FXML
    protected void onLoginButtonClick() {
        btnLogin.setText("Iniciando sesión...");
    }
}