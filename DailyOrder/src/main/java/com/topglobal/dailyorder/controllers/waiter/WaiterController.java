package com.topglobal.dailyorder.controllers.waiter;

import com.topglobal.dailyorder.controllers.UserController;
import com.topglobal.dailyorder.utils.View;
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
        UserController controller = View.loadView("/com/topglobal/dailyorder/views/waiter/waiter_home.fxml", contentPane);
        controller.setUser(this.user);
        controller.setInfo();
    }

    @FXML
    public void showTasks() {
        View.loadView("/com/topglobal/dailyorder/views/waiter/waiter_tasks.fxml", contentPane);
    }

}
