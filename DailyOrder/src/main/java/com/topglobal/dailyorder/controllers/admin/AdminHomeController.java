package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.controllers.UserController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminHomeController extends UserController {
    @FXML private Label lblFullName;
    @FXML private Label lblUsername;
    @FXML private Label lblPhoneNumber;
    @FXML private Label lblEmail;
    @FXML private Label lblShift;

    @Override
    public void setInfo() {
        if (this.user != null) {
            lblFullName.setText(user.getName() + " " + user.getFatherLastname() + " " + user.getMotherLastname());
            lblPhoneNumber.setText(user.getPhoneNumber());
            lblEmail.setText(user.getEmail());
            lblShift.setText(user.getShift());
        }
    }
}
