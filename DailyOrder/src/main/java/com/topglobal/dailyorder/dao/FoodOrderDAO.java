package com.topglobal.dailyorder.dao;

import com.topglobal.dailyorder.config.DatabaseConfig;
import com.topglobal.dailyorder.models.objects.FoodOrder;
import com.topglobal.dailyorder.models.objects.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

        Connection conn = null;

        public static List<FoodOrder> getAllOrders() throws SQLException {
            List<FoodOrder> orders = new ArrayList<>();

            String sql = """
            SELECT
                o.DAILY_FOLIO,
                o.ORDER_TIME,
                o.ORDER_STATUS,
                o.ORDER_COST,
                e.NAME || ' ' || e.FATHER_LASTNAME AS waiter_name,
                dt.ID_TABLE,
                dt.AREA || ' - Mesa ' || dt.ID_TABLE AS table_name,
                LISTAGG(ofd.FOOD_QUANTITY || 'x ' || f.FOOD_NAME, ', ')
                    WITHIN GROUP (ORDER BY f.FOOD_NAME) AS dishes
            FROM ORDERS o
            JOIN EMPLOYEE e ON o.FK_ID_WAITER = e.ID_EMPLOYEE
            JOIN DINING_TABLE dt ON o.FK_ID_TABLE = dt.ID_TABLE
            JOIN ORDER_FOOD ofd ON o.ID_ORDER = ofd.FK_ID_ORDER
            JOIN FOOD f ON ofd.FK_ID_FOOD = f.ID_FOOD
            GROUP BY
                o.DAILY_FOLIO, o.ORDER_TIME, o.ORDER_STATUS, o.ORDER_COST,
                e.NAME, e.FATHER_LASTNAME, dt.ID_TABLE, dt.AREA
            ORDER BY o.ORDER_TIME DESC
            
        """;

            try (Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    FoodOrder order = new FoodOrder();
                    order.setDailyFolio(rs.getInt("DAILY_FOLIO"));
                    order.setOrderDate(rs.getDate("ORDER_TIME").toLocalDate());
                    order.setOrderStatus(rs.getString("ORDER_STATUS"));
                    order.setTotalCost(rs.getDouble("ORDER_COST"));
                    order.setWaiterName(rs.getString("waiter_name"));
                    order.setDiningTableId(rs.getInt("ID_TABLE"));
                    order.setDiningTableName(rs.getString("table_name"));

                    String dishes = rs.getString("dishes");
                    if (dishes != null) {
                        order.setDishes(Arrays.asList(dishes.split(", ")));
                    }

                    orders.add(order);
                }
            }

            return orders;
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
