package com.topglobal.dailyorder.dao;

import com.topglobal.dailyorder.config.DatabaseConfig;
import com.topglobal.dailyorder.models.users.Admin;
import com.topglobal.dailyorder.models.users.Employee;
import com.topglobal.dailyorder.models.users.Waiter;
import com.topglobal.dailyorder.models.users.WaiterLeader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeeDAO {
    //Metodo para obtener informacion de un empleado por sus credenciales (email y password)
    public static Employee fetchEmployeeByCredentials(String email, String password) {
        //Consulta SQL para obtener datos de la tabla EMPLOYEE si se encuentra un registro que coincida en la tabla CREDENTIAL_DATA
        String query = "SELECT e.ID_EMPLOYEE, e.NAME, e.FATHER_LASTNAME, e.MOTHER_LASTNAME, e.PHONE_NUMBER, e.ROLE, e.SHIFT, c.EMAIL, c.STATUS " +
                "FROM EMPLOYEE e JOIN CREDENTIAL_DATA c ON e.ID_EMPLOYEE = c.FK_ID_EMPLOYEE " + "WHERE c.EMAIL = ? AND c.PASS = ?";
        Employee employee = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            //Set a los parametros de la consulta preparada
            stmt.setString(1, email);
            stmt.setString(2, password);
            //Ejecuta la consulta y obtiene el resultado
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                //Se obtiene el rol del empleado
                String role = rs.getString("ROLE");
                //Instanciar el tipo de empleado basado en el rol
                if("ADMIN".equalsIgnoreCase(role)) {
                    employee = new Admin();
                }else if("WAITERLEADER".equalsIgnoreCase(role)){
                    employee = new WaiterLeader();
                }else if("WAITER".equalsIgnoreCase(role)){
                    employee = new Waiter();
                }
                //Set a los atributos relevantes del empleado
                employee.setId(rs.getInt("ID_EMPLOYEE"));
                employee.setName(rs.getString("NAME"));
                employee.setFatherLastname(rs.getString("FATHER_LASTNAME"));
                employee.setMotherLastname(rs.getString("MOTHER_LASTNAME"));
                employee.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                employee.setEmail(rs.getString("EMAIL"));
                employee.setShift(rs.getString("SHIFT"));
                employee.setStatus(rs.getInt("STATUS"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }
}
