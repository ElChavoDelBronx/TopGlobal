package com.topglobal.dailyorder.models.login;

import com.topglobal.dailyorder.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeeDAO {
    public static Employee fetchEmployeeByCredentials(String email, String password) {
        String query = "SELECT ID_EMPLOYEE, NAME, FATHER_LASTNAME, MOTHER_LASTNAME, ROLE, SHIFT, STATUS " +
                "FROM EMPLOYEE " + "WHERE EMAIL = ? AND PASSWORD = ?";
        Employee employee = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("ID_EMPLOYEE");
                String name = rs.getString("NAME");
                String fatherLastname = rs.getString("FATHER_LASTNAME");
                String motherLastname = rs.getString("MOTHER_LASTNAME");
                String role = rs.getString("ROLE");
                String shift = rs.getString("SHIFT");
                String status = rs.getString("STATUS");

                if("ADMIN".equalsIgnoreCase(role)) {
                    employee = new Admin(id, name, fatherLastname, motherLastname, shift, status);
                }else if("WAITERLEADER".equalsIgnoreCase(role)){
                    employee = new WaiterLeader(id, name, fatherLastname, motherLastname, shift, status);
                }else if("WAITER".equalsIgnoreCase(role)){
                    employee = new Waiter(id, name, fatherLastname, motherLastname, shift, status);
                }
            } else {
                System.out.println("Acceso denegado");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }
}
