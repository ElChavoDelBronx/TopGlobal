package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.controllers.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;


public class AdminHomeController extends UserController {
    @FXML private Label lblFullName;
    @FXML private Label lblUsername;
    @FXML private Label lblPhoneNumber;
    @FXML private Label lblEmail;
    @FXML private Label lblShift;

    private AnchorPane contentPane;
    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }


    //Inicializa tipografia
    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);

    }

    //Carga información de usuario
    @Override
    public void setInfo() {
        if (this.user != null) {
            lblFullName.setText(user.getName() + " " + user.getFatherLastname() + " " + user.getMotherLastname());
            lblPhoneNumber.setText(user.getPhoneNumber());
            lblEmail.setText(user.getEmail());
            lblShift.setText(user.getShift());
        }
    }

    //Metodo para cambiar de vista al formulario de registro de empleados
    @FXML
    private void onCreateEmployee(ActionEvent event) {
        System.out.println("Click");
        AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_form.fxml", contentPane);

    }
}
