package com.topglobal.dailyorder.controllers.login;

import com.topglobal.dailyorder.Main;
import com.topglobal.dailyorder.models.login.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML
    private Button btnLogin;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField pwfLogin;

    @FXML
    protected void onLoginButtonClick() {
        String email = tfEmail.getText();
        String password = pwfLogin.getText();
        if (!email.isEmpty() && !password.isEmpty()) {
            Employee employee = EmployeeDAO.fetchEmployeeByCredentials(email, password);
            if(employee != null) {
                String newFxmlPath;
                if(employee instanceof Admin){
                    newFxmlPath = "/com/topglobal/dailyorder/views/admin_view.fxml";
                }else if(employee instanceof WaiterLeader){
                    newFxmlPath = "/com/topglobal/dailyorder/views/waiterLeader_view.fxml";
                }else{
                    newFxmlPath = "/com/topglobal/dailyorder/views/waiter_view.fxml";
                }
                Main.changeScene(newFxmlPath);
            }
        }

    }
}