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

    //Metodo para obtener informacion de un empleado por sus credenciales (usuario y password)
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
                if("Administrador".equalsIgnoreCase(role)) {
                    employee = new Admin();
                }else if("Lider de meseros".equalsIgnoreCase(role)){
                    employee = new WaiterLeader();
                }else if("Mesero".equalsIgnoreCase(role)){
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

    //Metodo para encontrar información de todos los empleados registrados
    public List<Employee> findAllEmployees() throws SQLException {
        String sql = "SELECT e.ID_EMPLOYEE, e.NAME, e.FATHER_LASTNAME, e.MOTHER_LASTNAME, c.NAME_USER, e.PHONE_NUMBER, e.EMAIL, e.ROLE, c.STATUS \n" +
                "FROM EMPLOYEE e JOIN CREDENTIAL_DATA c ON e.ID_EMPLOYEE = c.FK_ID_EMPLOYEE";
        List<Employee> personal = new ArrayList<>();
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

                personal.add(personalI);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return personal;
    }

    //Metodo para encontrar información de un solo empleado apartir de su ID
    public Employee findEmployeeById(int id) throws SQLException {
        String sql = "SELECT e.ID_EMPLOYEE, e.NAME, e.FATHER_LASTNAME, e.MOTHER_LASTNAME, " +
                "e.PHONE_NUMBER, e.ROLE, e.SHIFT, e.GENDER, e.BIRTHDAY, e.CURP, " +
                "e.EMAIL, c.NAME_USER, c.STATUS " + // Agregado STATUS
                "FROM EMPLOYEE e " +
                "JOIN CREDENTIAL_DATA c ON e.ID_EMPLOYEE = c.FK_ID_EMPLOYEE " +
                "WHERE e.ID_EMPLOYEE = ?";

        try (Connection conexion = DatabaseConfig.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Employee personalI = new Employee();
                personalI.setId(rs.getInt("ID_EMPLOYEE"));
                personalI.setName(rs.getString("NAME"));
                personalI.setFatherLastname(rs.getString("FATHER_LASTNAME"));
                personalI.setMotherLastname(rs.getString("MOTHER_LASTNAME"));
                personalI.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                personalI.setRole(rs.getString("ROLE"));
                personalI.setShift(rs.getString("SHIFT"));
                personalI.setGender(rs.getString("GENDER"));

                java.sql.Date sqlDate = rs.getDate("BIRTHDAY");
                if (sqlDate != null) {
                    personalI.setBirthday(sqlDate.toLocalDate());
                }

                personalI.setCurp(rs.getString("CURP"));
                personalI.setEmail(rs.getString("EMAIL"));
                personalI.setUser(rs.getString("NAME_USER"));

                // ✅ Asignamos el estatus correctamente
                personalI.setStatus(rs.getInt("STATUS"));

                return personalI;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    //Metodo para crear nuevo registro de empleado
    public void createEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO EMPLOYEE (NAME, FATHER_LASTNAME, MOTHER_LASTNAME, PHONE_NUMBER, ROLE, SHIFT, GENDER, BIRTHDAY, CURP, EMAIL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlCredentials = "INSERT INTO CREDENTIAL_DATA (NAME_USER, PASS,FK_ID_EMPLOYEE, STATUS) VALUES (?, ?, ?, ?)";


        Connection conexion = null;
        PreparedStatement stmtEmployee = null;
        PreparedStatement stmtCredentials = null;
        try{
            conexion = DatabaseConfig.getConnection();
            conexion.setAutoCommit(false);
            stmtEmployee = conexion.prepareStatement(sql, new String[]{"ID_EMPLOYEE"});
            stmtEmployee.setString(1, employee.getName());
            stmtEmployee.setString(2, employee.getFatherLastname());
            stmtEmployee.setString(3, employee.getMotherLastname());
            stmtEmployee.setString(4, employee.getPhoneNumber());
            stmtEmployee.setString(5, employee.getRole());
            stmtEmployee.setString(6, employee.getShift());
            stmtEmployee.setString(7, employee.getGender());
            stmtEmployee.setDate(8, java.sql.Date.valueOf(employee.getBirthday()));
            stmtEmployee.setString(9, employee.getCurp());
            stmtEmployee.setString(10, employee.getEmail());
            stmtEmployee.executeUpdate();

            ResultSet rs = stmtEmployee.getGeneratedKeys();
            int generatedId = -1;
            if (rs.next()) {
                generatedId = rs.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID del empleado insertado.");
            }

            stmtCredentials = conexion.prepareStatement(sqlCredentials);
            stmtCredentials.setString(1, employee.getUser());
            stmtCredentials.setString(2, "temporal123");
            stmtCredentials.setInt(3, generatedId);
            stmtCredentials.setInt(4, employee.getStatus());
            stmtCredentials.executeUpdate();

            conexion.commit();
            System.out.println("Empleado y credenciales creados con éxito.");

        } catch (Exception e) {
            if (conexion != null) conexion.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al crear empleado y credenciales: " + e.getMessage());
        } finally {
            // Cerrar recursos
            if (stmtCredentials != null) stmtCredentials.close();
            if (stmtEmployee != null) stmtEmployee.close();
            if (conexion != null) conexion.setAutoCommit(true);
        }
    }

    //Metodo para actualizar información de empleado
    public void updateEmployee(Employee employee) throws SQLException {
        String sqlUpdateEmployee = "UPDATE EMPLOYEE SET NAME=?, FATHER_LASTNAME=?, MOTHER_LASTNAME=?, PHONE_NUMBER=?, ROLE=?, SHIFT=?, GENDER=?, BIRTHDAY=?, CURP=?, EMAIL=? WHERE ID_EMPLOYEE=?";
        String sqlUpdateCredentials = "UPDATE CREDENTIAL_DATA SET NAME_USER=? WHERE FK_ID_EMPLOYEE=?";

        Connection conexion = null;
        PreparedStatement stmtEmployee = null;
        PreparedStatement stmtCredentials = null;

        try {
            conexion = DatabaseConfig.getConnection();
            conexion.setAutoCommit(false);

            // Actualizar datos del empleado
            stmtEmployee = conexion.prepareStatement(sqlUpdateEmployee);
            stmtEmployee.setString(1, employee.getName());
            stmtEmployee.setString(2, employee.getFatherLastname());
            stmtEmployee.setString(3, employee.getMotherLastname());
            stmtEmployee.setString(4, employee.getPhoneNumber());
            stmtEmployee.setString(5, employee.getRole());
            stmtEmployee.setString(6, employee.getShift());
            stmtEmployee.setString(7, employee.getGender());

            // Validar fecha
            if (employee.getBirthday() != null) {
                stmtEmployee.setDate(8, java.sql.Date.valueOf(employee.getBirthday()));
            } else {
                stmtEmployee.setNull(8, java.sql.Types.DATE);
            }

            stmtEmployee.setString(9, employee.getCurp());
            stmtEmployee.setString(10, employee.getEmail());
            stmtEmployee.setInt(11, employee.getId());

            int filasEmpleado = stmtEmployee.executeUpdate();

            // Actualizar credenciales
            stmtCredentials = conexion.prepareStatement(sqlUpdateCredentials);
            stmtCredentials.setString(1, employee.getUser());
            stmtCredentials.setInt(2, employee.getId());

            int filasCredenciales = stmtCredentials.executeUpdate();

            if (filasEmpleado == 0) {
                throw new RuntimeException("No se encontró el empleado con ID " + employee.getId());
            }

            if (filasCredenciales == 0) {
                throw new RuntimeException("No se encontró credencial para empleado con ID " + employee.getId());
            }

            conexion.commit();
            System.out.println("Empleado y credenciales actualizados con éxito.");

        } catch (Exception e) {
            if (conexion != null) conexion.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar empleado: " + e.getMessage());
        } finally {
            if (stmtCredentials != null) stmtCredentials.close();
            if (stmtEmployee != null) stmtEmployee.close();
            if (conexion != null) conexion.setAutoCommit(true);
        }
    }

    //Metodo para cambiar estatus de empleado (Activo - Inactivo)
    public void changeStatus(Employee employee) throws SQLException {
        String sqlUpdateCredentials = "UPDATE CREDENTIAL_DATA SET STATUS=? WHERE FK_ID_EMPLOYEE=?";
        Connection conexion = null;
        PreparedStatement stmtCredentials = null;

        try {
            conexion = DatabaseConfig.getConnection();
            conexion.setAutoCommit(false);

            stmtCredentials = conexion.prepareStatement(sqlUpdateCredentials);
            stmtCredentials.setInt(1, employee.getStatus());
            stmtCredentials.setInt(2, employee.getId());

            int filasCredenciales = stmtCredentials.executeUpdate();
            if (filasCredenciales == 0) {
                throw new RuntimeException("No se encontró credencial para empleado con ID " + employee.getId());
            }

            conexion.commit();
            System.out.println("Estatus cambiado");
        } catch (Exception e) {
            if (conexion != null) conexion.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar empleado: " + e.getMessage());
        } finally {
            if (stmtCredentials != null) stmtCredentials.close();
            if (conexion != null) conexion.close();
        }
    }
/*
    public List<Employee> findByRole(String role) throws SQLException {
        String sql = "SELECT * FROM EMPLOYEE WHERE ROLE = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            return mapEmployees(rs);
        }
    }

    public List<Employee> findByShift(String shift) throws SQLException {
        String sql = "SELECT * FROM EMPLOYEE WHERE SHIFT = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, shift);
            ResultSet rs = stmt.executeQuery();
            return mapEmployees(rs);
        }
    }

    public List<Employee> orderByNameAsc() throws SQLException {
        String sql = "SELECT * FROM EMPLOYEE ORDER BY NAME ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return mapEmployees(rs);
        }
    }

    public List<Employee> orderByFatherLastnameAsc() throws SQLException {
        String sql = "SELECT * FROM EMPLOYEE ORDER BY FATHER_LASTNAME ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return mapEmployees(rs);
        }
    }



*/


}
