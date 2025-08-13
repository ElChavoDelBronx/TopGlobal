package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.controllers.UserController;
import com.topglobal.dailyorder.utils.SessionData;
import com.topglobal.dailyorder.utils.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;


public class AdminHomeController {
    private SessionData sessionData;
    @FXML private Label lblFullName;
    @FXML private Label lblUsername;
    @FXML private Label lblPhoneNumber;
    @FXML private Label lblEmail;
    @FXML private Label lblShift;

    private AnchorPane contentPane;
    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    //Inicializa tipografia
    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);

    }

    //Metodo para cambiar de vista al formulario de registro de empleados
    @FXML
    private void onCreateEmployee(ActionEvent event) {
        System.out.println("Click");
        View.loadView("/com/topglobal/dailyorder/views/admin/admin_form.fxml", contentPane, sessionData);

    }
}
