package com.topglobal.dailyorder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Cambiar vista fxml segun conveniencia.
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/topglobal/dailyorder/views/login_view.fxml"));
        
        Image image = new Image(getClass().getResourceAsStream("/com/topglobal/dailyorder/icons/Logo.png"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(image);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        //primaryStage.setFullScreen(true);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static <T> T changeScene(String fxmlPath, String title) {
        try {
            // Guardar estado actual
            boolean wasMaximized = primaryStage.isMaximized();
            boolean wasFullScreen = primaryStage.isFullScreen();
            double prevX = primaryStage.getX();
            double prevY = primaryStage.getY();
            double prevW = primaryStage.getWidth();
            double prevH = primaryStage.getHeight();

            // Cargar nueva scene
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene newScene = new Scene(root);

            // Aplicar title y scene (no llamar a show() aquí)
            primaryStage.setTitle(title);
            primaryStage.setScene(newScene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(700);

            // Restaurar estado **después** de que JavaFX haya aplicado la Scene
            Platform.runLater(() -> {
                primaryStage.setFullScreen(wasFullScreen);

                if (wasMaximized) {
                    // Si estaba maximizada, volver a maximizar
                    primaryStage.setMaximized(true);
                } else {
                    // Si no estaba maximizada, restaurar tamaño y posición
                    // Primero limitar la posición al área visible (por si cambió de monitor)
                    java.util.List<Screen> screens = Screen.getScreensForRectangle(prevX, prevY, prevW, prevH);
                    Rectangle2D bounds = screens.isEmpty() ? Screen.getPrimary().getVisualBounds() : screens.get(0).getVisualBounds();

                    double x = Math.max(bounds.getMinX(), Math.min(prevX, bounds.getMaxX() - prevW));
                    double y = Math.max(bounds.getMinY(), Math.min(prevY, bounds.getMaxY() - prevH));

                    primaryStage.setX(x);
                    primaryStage.setY(y);
                    primaryStage.setWidth(prevW);
                    primaryStage.setHeight(prevH);
                    primaryStage.setMaximized(false);
                }
            });

            return loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}