package com.topglobal.dailyorder.controllers.login;

import com.topglobal.dailyorder.Main;
import com.topglobal.dailyorder.controllers.UserController;
import com.topglobal.dailyorder.dao.EmployeeDAO;
import com.topglobal.dailyorder.models.users.*;
import com.topglobal.dailyorder.utils.SessionData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;

public class LoginController {
    private SessionData sessionData = new SessionData();
    @FXML private TextField tfUser;
    @FXML private PasswordField pwfLogin;
    @FXML private TextField tfPassword;
    @FXML private Button eyeClosed;
    @FXML private Button eyeOpened;

    public void initialize() {
        tfPassword.setVisible(false);
        tfPassword.setManaged(false);
        eyeOpened.setVisible(false);
        eyeOpened.setManaged(false);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"),12);
        tfPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            pwfLogin.setText(newValue);
        });
    }
    @FXML
    protected void onLoginButtonClick() {
        //Obtiene los valores de los campos de texto
        String user = tfUser.getText().trim();
        String password = pwfLogin.getText().trim();

        //Valida que los campos no estén vacíos
        if (!user.isEmpty() && !password.isEmpty()) {
            //Obtiene el empleado por sus credenciales
            Employee employee = EmployeeDAO.fetchEmployeeByCredentials(user, password);
            if(employee != null && employee.getStatus() == 1) {
                String newFxmlPath;
                String title;
                if(employee instanceof Admin){
                    newFxmlPath = "/com/topglobal/dailyorder/views/admin/admin_view.fxml";
                    title = "Administrador";
                }else if(employee instanceof WaiterLeader){
                    newFxmlPath = "/com/topglobal/dailyorder/views/leader/waiterLeader_view.fxml";
                    title = "Líder De Meseros";
                }else{
                    newFxmlPath = "/com/topglobal/dailyorder/views/waiter/waiter_view.fxml";
                    title = "Mesero";
                }
                sessionData.setUser(employee);
                //Cambia la escena a la vista correspondiente
                Main.changeScene(newFxmlPath, title, sessionData);
            }else{
                showAlert("Error", "Acceso denegado. Verifique su información de acceso o estado de cuenta con el administrador.");
            }
        }else{
            showAlert("Error", "Por favor, complete todos los campos");
        }

    }
    @FXML
    protected void togglePassVisibility() {
        if (pwfLogin.isVisible()) {
            pwfLogin.setVisible(false);
            pwfLogin.setManaged(false);
            tfPassword.setVisible(true);
            tfPassword.setManaged(true);
            tfPassword.setText(pwfLogin.getText());
            eyeOpened.setVisible(true);
            eyeOpened.setManaged(true);
            eyeClosed.setVisible(false);
            eyeClosed.setManaged(false);
        }else{
            pwfLogin.setVisible(true);
            pwfLogin.setManaged(true);
            tfPassword.setVisible(false);
            tfPassword.setManaged(false);
            pwfLogin.setText(tfPassword.getText());
            eyeOpened.setVisible(false);
            eyeOpened.setManaged(false);
            eyeClosed.setVisible(true);
            eyeClosed.setManaged(true);
        }
    }
    public void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}