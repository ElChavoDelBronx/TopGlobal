package com.topglobal.dailyorder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/topglobal/dailyorder/views/login_view.fxml"));
        Image image = new Image(getClass().getResourceAsStream("/com/topglobal/dailyorder/icons/Logo.png"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}