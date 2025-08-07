package com.topglobal.dailyorder.utils;

import com.topglobal.dailyorder.controllers.admin.AdminController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class View {
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
}
