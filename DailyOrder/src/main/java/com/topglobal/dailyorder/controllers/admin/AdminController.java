package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.Main;
import com.topglobal.dailyorder.controllers.UserController;
import com.topglobal.dailyorder.utils.SessionData;
import com.topglobal.dailyorder.utils.View;
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

public class AdminController {
    private SessionData sessionData = new SessionData();
    @FXML private AnchorPane contentPane;

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    @FXML
    private void onTables(ActionEvent event) {
        View.loadView("/com/topglobal/dailyorder/views/admin/admin_table_management.fxml", contentPane, sessionData);
    }
    @FXML
    private void onPersonal(ActionEvent event) {
        View.loadView("/com/topglobal/dailyorder/views/admin/admin_list.fxml", contentPane, sessionData);

    }
    @FXML
    private void onFoodDish(ActionEvent event) {
        View.loadView("/com/topglobal/dailyorder/views/admin/menu/menu_management.fxml", contentPane, sessionData);
    }
    @FXML
    private void onOrders(ActionEvent event) {
        View.loadView("/com/topglobal/dailyorder/views/admin/orders/adminOrder_management.fxml", contentPane, sessionData);
    }
    @FXML
    public void onLogout() {
        Main.changeScene("/com/topglobal/dailyorder/views/login_view.fxml", "Login", sessionData);
    }

}
