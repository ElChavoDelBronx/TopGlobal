package com.topglobal.dailyorder.controllers.waiter;

import com.topglobal.dailyorder.Main;
import com.topglobal.dailyorder.controllers.UserController;
import com.topglobal.dailyorder.utils.View;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class WaiterController extends UserController {

    @FXML private AnchorPane contentPane;

    @Override
    public void setInfo() {
        UserController controller = View.loadView("/com/topglobal/dailyorder/views/waiter/waiter_home.fxml", contentPane);
        controller.setUser(this.user);
        controller.setInfo();
    }
    @FXML public void showHistory(){
        View.loadView("/com/topglobal/dailyorder/views/waiter/waiter_history.fxml", contentPane);
    }
    @FXML
    public void showTasks() {
        View.loadView("/com/topglobal/dailyorder/views/waiter/waiter_home.fxml", contentPane);
    }
    @FXML
    public void onLogout() {
        Main.changeScene("/com/topglobal/dailyorder/views/login_view.fxml", "Login");
    }
    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/com/topglobal/dailyorder/fonts/Lexend-ExtraLight.ttf"), 12);
    }
}
