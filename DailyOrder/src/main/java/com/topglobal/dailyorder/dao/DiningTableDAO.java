package com.topglobal.dailyorder.dao;

import com.topglobal.dailyorder.config.DatabaseConfig;
import com.topglobal.dailyorder.models.objects.DiningTable;
import com.topglobal.dailyorder.models.users.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiningTableDAO {
    public static void create(DiningTable diningTable) throws SQLException {
        String query = "INSERT INTO DINING_TABLE (ID_TABLE, AREA, CHAIR_NUMBER, STATUS) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DatabaseConfig.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, diningTable.getTableNumber());
            ps.setString(2, diningTable.getArea());
            ps.setInt(3, diningTable.getCapacity());
            ps.setInt(4, diningTable.getStatus());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Mesa creada con exito");
            }
        } catch (Exception e) {
            if(conn != null) { conn.rollback(); }
            throw new RuntimeException(e);
        } finally {
            if(conn != null && ps != null) {
                ps.close();
                conn.close();
            }
        }
    }
    public static List<DiningTable> findAll() throws SQLException {
        String sql = "SELECT ID_TABLE, AREA, CHAIR_NUMBER, STATUS FROM DINING_TABLE ORDER BY ID_TABLE ASC";
        List<DiningTable> tables = new ArrayList<>();
        try{
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DiningTable table = new DiningTable();
                table.setTableNumber(rs.getInt("ID_TABLE"));
                table.setArea(rs.getString("AREA"));
                table.setCapacity(rs.getInt("CHAIR_NUMBER"));
                table.setStatus(rs.getInt("STATUS"));

                tables.add(table);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tables;
    }
    public static void update(DiningTable diningTable) throws SQLException {
        String query = "UPDATE DINING_TABLE SET AREA=?, CHAIR_NUMBER=?, STATUS=? WHERE ID_TABLE=?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConfig.getConnection();
            //Desactivar cambios automaticos en la base de datos
            conn.setAutoCommit(false);

            // Actualizar datos de la mesa en la consulta
            ps = conn.prepareStatement(query);
            ps.setString(1, diningTable.getArea());
            ps.setInt(2, diningTable.getCapacity());
            ps.setInt(3, diningTable.getStatus());
            ps.setInt(4, diningTable.getTableNumber());

            ps.executeUpdate();

            conn.commit();
            System.out.println("Datos de la mesa actualizados con éxito.");

        } catch (Exception e) {
            if (conn != null){ conn.rollback(); } //Si ocurre un error, se abortan los cambios
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar empleado: " + e.getMessage());
        } finally { //Este bloque siempre se ejecuta, independientemente de si hubo una excepción o no
            if (ps != null) { ps.close(); }
            if (conn != null) {
                conn.setAutoCommit(true); //Reactivar cambios automaticos en la base de datos
                conn.close();
            }
        }
    }
    public static void delete(DiningTable diningTable) throws SQLException {
        String query = "DELETE FROM DINING_TABLE WHERE ID_TABLE=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DatabaseConfig.getConnection();
            //Desactivar cambios automaticos en la base de datos
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(query);
            ps.setInt(1, diningTable.getTableNumber());
            ps.executeUpdate();
            System.out.println("Mesa eliminada con éxito.");
        } catch (Exception e) {
            if(conn != null) { conn.rollback(); } //Si ocurre un error, se abortan los cambios
            throw new RuntimeException(e);
        } finally { //Este bloque siempre se ejecuta, independientemente de si hubo una excepción o no
            if(conn != null && ps != null) {
                ps.close();
                conn.setAutoCommit(true); //Reactivar cambios automaticos en la base de datos
                conn.close();
            }
        }
    }
}
