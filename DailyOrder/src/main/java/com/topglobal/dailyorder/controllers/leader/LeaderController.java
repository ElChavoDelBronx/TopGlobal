package com.topglobal.dailyorder.controllers.leader;

import com.topglobal.dailyorder.Main;
import com.topglobal.dailyorder.controllers.UserController;
import com.topglobal.dailyorder.controllers.admin.AdminController;
import com.topglobal.dailyorder.utils.SessionData;
import com.topglobal.dailyorder.utils.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LeaderController {
    private SessionData sessionData = new SessionData();
    @FXML private AnchorPane contentPane;

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    @FXML
    private void onOrder(ActionEvent event) {
        View.loadView("/com/topglobal/dailyorder/views/leader/orders/leaderOrder_management.fxml", contentPane, sessionData);
    }

    //Metodo para cargar vista de tabla de empleados

    @FXML
    private void onPlan(ActionEvent event) {
        View.loadView("/com/topglobal/dailyorder/views/admin/admin_list.fxml", contentPane, sessionData);

    }
    @FXML
    public void onLogout() {
        Main.changeScene("/com/topglobal/dailyorder/views/login_view.fxml", "Login", sessionData);
    }
}
