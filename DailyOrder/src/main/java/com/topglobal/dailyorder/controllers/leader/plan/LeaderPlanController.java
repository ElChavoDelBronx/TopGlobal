package com.topglobal.dailyorder.controllers.leader.plan;

import com.topglobal.dailyorder.dao.AssignmentDAO;
import com.topglobal.dailyorder.models.objects.Assignment;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;

public class LeaderPlanController {

    @FXML
    private FlowPane flowPaneTables;

    @FXML
    private Button btnAddPlan;

    private AssignmentDAO assignmentDAO;

    @FXML
    public void initialize() {
        // Conectar con la base de datos Oracle
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "ADMIN", "password");
            assignmentDAO = new AssignmentDAO(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadAssignments();
    }

    private void loadAssignments() {
        flowPaneTables.getChildren().clear();
        List<Assignment> assignments = assignmentDAO.getAllAssignments();

        for (Assignment a : assignments) {
            VBox box = new VBox();
            box.setSpacing(5);
            box.getStyleClass().add("assignment-card"); // definir estilo en tu CSS
            box.getChildren().addAll(
                    new Text("Mesa: " + a.getIdTable()),
                    new Text("Empleado: " + a.getIdEmployee()),
                    new Text("Turno: " + a.getShift()),
                    new Text("Fecha: " + a.getAssignDate())
            );
            flowPaneTables.getChildren().add(box);
        }
    }

    @FXML
    private void onAddPlan() {
        // Para prueba rápida agregamos una asignación ficticia
        assignmentDAO.addAssignment(101, 5, "Mañana", LocalDate.now());
        loadAssignments();
    }
}
