package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.Main;
import com.topglobal.dailyorder.controllers.UserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class AdminController extends UserController {
    @FXML private AnchorPane contentPane;

    @Override
    public void setInfo() {
        UserController controller = AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_home.fxml", contentPane);
        ;
        controller.setUser(this.user);
        controller.setInfo();
    }

    public static <T> T loadView(String fxmlPath, AnchorPane contentPane) {
        try {
            FXMLLoader loader = new FXMLLoader(AdminController.class.getResource(fxmlPath));
            Parent view = loader.load();

            T controller = loader.getController();

            // Verifica si el controlador tiene un método setContentPane
            try {
                Method method = controller.getClass().getMethod("setContentPane", AnchorPane.class);
                method.invoke(controller, contentPane);
            } catch (NoSuchMethodException ignored) {
                // El controlador no tiene setContentPane, lo ignoramos
            }

            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

            return controller;
        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }



    @FXML
    private void onTables(ActionEvent event) {
        AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_table_management.fxml", contentPane);
    }
    @FXML
    private void onPersonal(ActionEvent event) {
        AdminController.loadView("/com/topglobal/dailyorder/views/admin/admin_list.fxml", contentPane);

    }
    @FXML
    public void onLogout() {
        Main.changeScene("/com/topglobal/dailyorder/views/login_view.fxml", "Login");
    }

}
