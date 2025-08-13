package com.topglobal.dailyorder.controllers.leader;

import com.topglobal.dailyorder.controllers.UserController;
import com.topglobal.dailyorder.controllers.admin.AdminController;
import com.topglobal.dailyorder.utils.SessionData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;


public class LeaderHomeController {
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
        lblFullName.setText(sessionData.getUser().getName() + " " + sessionData.getUser().getFatherLastname() + " " + sessionData.getUser().getMotherLastname());
        lblUsername.setText(sessionData.getUser().getUser());
        lblPhoneNumber.setText(sessionData.getUser().getPhoneNumber());
        lblEmail.setText(sessionData.getUser().getEmail());
        lblShift.setText(sessionData.getUser().getShift());
    }

    //Inicializa tipografia
    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);

    }


}
