package com.topglobal.dailyorder.controllers.waiter;

import com.topglobal.dailyorder.Main;
import com.topglobal.dailyorder.controllers.UserController;
import com.topglobal.dailyorder.utils.SessionData;
import com.topglobal.dailyorder.utils.View;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class WaiterController {
    private SessionData sessionData;
    @FXML private AnchorPane contentPane;

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    @FXML public void showHistory(){
        View.loadView("/com/topglobal/dailyorder/views/waiter/waiter_history.fxml", contentPane, sessionData);
    }
    @FXML
    public void showTasks() {
        View.loadView("/com/topglobal/dailyorder/views/waiter/waiter_home.fxml", contentPane, sessionData);
    }
    @FXML
    public void onLogout() {
        Main.changeScene("/com/topglobal/dailyorder/views/login_view.fxml", "Login", sessionData);
    }
    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
    }
}
