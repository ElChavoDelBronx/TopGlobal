package com.topglobal.dailyorder.dao;

import com.topglobal.dailyorder.models.objects.Assignment;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {

    private Connection connection;

    public AssignmentDAO(Connection connection) {
        this.connection = connection;
    }

    // Obtener todas las asignaciones
    public List<Assignment> getAllAssignments() {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT id_assignment, id_employee, id_table, shift, assign_date FROM assignment";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Assignment assignment = new Assignment(
                        rs.getInt("id_assignment"),
                        rs.getInt("id_employee"),
                        rs.getInt("id_table"),
                        rs.getString("shift"),
                        rs.getDate("assign_date").toLocalDate()
                );
                list.add(assignment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Insertar nueva asignación
    public void addAssignment(int idEmployee, int idTable, String shift, LocalDate date) {
        String sql = "INSERT INTO assignment (id_assignment, id_employee, id_table, shift, assign_date) " +
                "VALUES (assignment_seq.NEXTVAL, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idEmployee);
            ps.setInt(2, idTable);
            ps.setString(3, shift);
            ps.setDate(4, Date.valueOf(date));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

