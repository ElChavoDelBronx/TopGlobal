package com.topglobal.dailyorder.controllers.waiter;

import com.topglobal.dailyorder.controllers.UserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class WaiterController extends UserController {

    @FXML private AnchorPane contentPane;

    @Override
    public void setInfo() {
        UserController controller = loadView("/com/topglobal/dailyorder/views/waiter/waiter_home.fxml");
        controller.setUser(this.user);
        controller.setInfo();
    }

    private <T> T loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            GridPane view = loader.load();
            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
