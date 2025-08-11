package com.topglobal.dailyorder.controllers.admin;

import com.topglobal.dailyorder.models.users.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminWorkingpController implements Initializable {

    @FXML
    private TableView<Employee> tableWorkingP;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
