package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.controllers.UserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class AdminController extends UserController {
    @FXML private AnchorPane contentPane;

    @Override
    public void setInfo() {
        UserController controller = loadView("/com/topglobal/dailyorder/views/admin/admin_home.fxml");
        controller.setUser(this.user);
        controller.setInfo();
    }

    private <T> T loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
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

    @FXML
    private void onPersonal(ActionEvent event) {
        loadView("/com/topglobal/dailyorder/views/admin/admin_list.fxml");
    }

    @FXML
    private void onCreateEmployee(ActionEvent event) {
        loadView("/com/topglobal/dailyorder/views/admin/admin_form.fxml");
    }
}
