package com.topglobal.dailyorder.utils;

import com.topglobal.dailyorder.controllers.admin.AdminController;
import com.topglobal.dailyorder.controllers.admin.AdminEditTableController;
import com.topglobal.dailyorder.controllers.admin.menu.EditMenuItemFormController;
import com.topglobal.dailyorder.models.objects.DiningTable;
import com.topglobal.dailyorder.models.objects.MenuItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class View {
    //Aqui se definiran todos los atributos que se necesiten antes de cargar una vista
    private DiningTable diningTable;
    private MenuItem menuItem;
    //Agregar constructores según sea necesario
    public View() {}
    public View(DiningTable diningTable) {
        this.diningTable = diningTable;
    }
    public View(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public static <T> T loadView(String fxmlPath, AnchorPane contentPane) {
        try {
            FXMLLoader loader = new FXMLLoader(View.class.getResource(fxmlPath));
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
    //Carga un modal
    public  <T> T loadModal(ActionEvent event, String fxmlPath, String title) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    View.class.getResource(fxmlPath)
            );
            Parent root = loader.load();
            // Se obtiene el controlador del FXML cargado
            T controller = loader.getController();
            //Aca se verificará las instancias de los controladores que se necesiten
            if(controller instanceof AdminEditTableController){
                ((AdminEditTableController) controller).setDiningTable(diningTable);
            }else if(controller instanceof EditMenuItemFormController){
                ((EditMenuItemFormController) controller).setFormData(menuItem);
            }

            Stage dialog = new Stage();
            dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(title);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //Cierra la escena de un nodo
    public static void closeWindow(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
