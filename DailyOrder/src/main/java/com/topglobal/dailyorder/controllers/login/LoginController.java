package com.topglobal.dailyorder.controllers.login;

import com.topglobal.dailyorder.Main;
import com.topglobal.dailyorder.controllers.UserController;
import com.topglobal.dailyorder.dao.EmployeeDAO;
import com.topglobal.dailyorder.models.users.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML
    private TextField tfUser;
    @FXML
    private PasswordField pwfLogin;

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
                //Cambia la escena a la vista correspondiente
                UserController controller = Main.changeScene(newFxmlPath, title);
                if(controller != null) {
                    controller.setUser(employee);
                    controller.setInfo();
                }
            }else{
                showAlert("Error", "Acceso denegado. Verifique su información de acceso o estado de cuenta con el administrador.");
            }
        }else{
            showAlert("Error", "Por favor, complete todos los campos");
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