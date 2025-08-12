package com.topglobal.dailyorder.dao;

import com.topglobal.dailyorder.config.DatabaseConfig;
import com.topglobal.dailyorder.models.objects.FoodOrder;
import com.topglobal.dailyorder.models.objects.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class FoodOrderDAO {
    //Metodo usado para obtener el numero de orden actual
    public static int selectAllOrders() throws SQLException {
        String sql = "SELECT ID_ORDER FROM ORDERS";
        try {
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery().getFetchSize();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error fetching all orders", e);
        }
    }
    //Metodo usado para crear una nueva orden
    public static void createOrder(
            FoodOrder order, int tableID, int waiterID, List<MenuItem> dishes
    ) throws SQLException {
        String orderQuery = "INSERT INTO ORDERS (ID_ORDER, FK_ID_TABLE, FK_ID_WAITER, ORDER_TIME, ORDER_STATUS, ORDER_COST) VALUES (?, ?, ?, ?, ?, ?)";
        String orderFoodQuery = "INSERT INTO ORDER_FOOD (FK_ID_ORDER, FK_ID_FOOD, FOOD_QUANTITY, TOTAL_COST) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmtOrder = null;
        PreparedStatement stmtOrderFood = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); //Desactivamos los cambios automáticos
            //Agregamos valores a la consulta preparada y ejecutamos la inserción
            stmtOrder = conn.prepareStatement(orderQuery);
            stmtOrder.setInt(1, order.getOrderId());
            stmtOrder.setInt(2, tableID);
            stmtOrder.setInt(3, waiterID);
            stmtOrder.setDate(4, java.sql.Date.valueOf(order.getOrderDate()));
            stmtOrder.setString(4, order.getOrderStatus().toLowerCase());
            stmtOrder.executeUpdate();

            //Agregamos valores a la consulta preparada y ejecutamos la inserción en la tabla de relación por cada uno de los platillos pedidos
            for( MenuItem menuItem : dishes) {
                stmtOrderFood = conn.prepareStatement(orderFoodQuery);
                stmtOrderFood.setInt(1, order.getOrderId());
                stmtOrderFood.setInt(2, menuItem.getId());
                stmtOrderFood.setInt(3, menuItem.getQuantity());
                stmtOrderFood.setDouble(4, menuItem.getCost() * menuItem.getQuantity());
                stmtOrderFood.executeUpdate();
            }
            conn.commit(); // Confirmamos los cambios
            System.out.println("Orden guardada con éxito");
        } catch (Exception e) {
            if(conn != null) { conn.rollback(); }
            throw new RuntimeException("Error al crear nueva orden: " + e.getMessage());
        } finally {
            if(stmtOrder != null) { stmtOrder.close(); }
            if(stmtOrderFood != null) { stmtOrderFood.close(); }
            if(conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
