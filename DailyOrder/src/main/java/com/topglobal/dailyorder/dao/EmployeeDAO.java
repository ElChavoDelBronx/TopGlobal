package com.topglobal.dailyorder.dao;

import com.topglobal.dailyorder.config.DatabaseConfig;
import com.topglobal.dailyorder.models.users.Admin;
import com.topglobal.dailyorder.models.users.Employee;
import com.topglobal.dailyorder.models.users.Waiter;
import com.topglobal.dailyorder.models.users.WaiterLeader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    //Metodo para obtener informacion de un empleado por sus credenciales (email y password)
    public static Employee fetchEmployeeByCredentials(String user, String password) {
        //Consulta SQL para obtener datos de la tabla EMPLOYEE si se encuentra un registro que coincida en la tabla CREDENTIAL_DATA
        String query = "SELECT e.ID_EMPLOYEE, e.NAME, e.FATHER_LASTNAME, e.MOTHER_LASTNAME, e.PHONE_NUMBER, e.ROLE, e.SHIFT, e.GENDER, e.BIRTHDAY, e.CURP, e.EMAIL, c.NAME_USER, c.STATUS " +
                "FROM EMPLOYEE e JOIN CREDENTIAL_DATA c ON e.ID_EMPLOYEE = c.FK_ID_EMPLOYEE " + "WHERE c.NAME_USER = ? AND c.PASS = ?";
        Employee employee = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            //Set a los parametros de la consulta preparada
            stmt.setString(1, user);
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

    public List<Employee> findAllEmployees() throws SQLException {
        String sql = "SELECT e.ID_EMPLOYEE, e.NAME, e.FATHER_LASTNAME, e.MOTHER_LASTNAME, c.NAME_USER, e.PHONE_NUMBER, e.EMAIL, e.ROLE, c.STATUS, e.SHIFT\n" +
                "FROM EMPLOYEE e JOIN CREDENTIAL_DATA c ON e.ID_EMPLOYEE = c.FK_ID_EMPLOYEE";
        List<Employee> personal = new ArrayList<Employee>();
        try{
            Connection conexion = DatabaseConfig.getConnection();
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee personalI = new Employee();
                personalI.setId(rs.getInt("ID_EMPLOYEE"));
                personalI.setName(rs.getString("NAME"));
                personalI.setFatherLastname(rs.getString("FATHER_LASTNAME"));
                personalI.setMotherLastname(rs.getString("MOTHER_LASTNAME"));
                personalI.setUser(rs.getString("NAME_USER"));
                personalI.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                personalI.setEmail(rs.getString("EMAIL"));
                personalI.setRole(rs.getString("ROLE"));
                personalI.setStatus(rs.getInt("STATUS"));
                personalI.setShift(rs.getString("SHIFT"));



                personal.add(personalI);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return personal;
    }

    public void createEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO EMPLOYEE (NAME, FATHER_LASTNAME, MOTHER_LASTNAME, PHONE_NUMBER, ROLE, SHIFT) VALUES (?, ?, ?, ?, ?, ?)";
        try{
            Connection conexion = DatabaseConfig.getConnection();
            PreparedStatement stmt = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getFatherLastname());
            stmt.setString(3, employee.getMotherLastname());
            stmt.setString(4, employee.getPhoneNumber());
            stmt.setString(5, employee.getRole());
            stmt.setString(6, employee.getShift());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Creado con exito");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
