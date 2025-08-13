package com.topglobal.dailyorder.dao;

import com.topglobal.dailyorder.config.DatabaseConfig;
import com.topglobal.dailyorder.models.objects.FoodOrder;
import com.topglobal.dailyorder.models.objects.MenuItem;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public static void createOrderSimple(int tableId, int waiterId, String status, double cost, ObservableList<MenuItem> platillos) {
        String sqlOrder = "INSERT INTO ORDERS (FK_ID_TABLE, FK_ID_WAITER, ORDER_STATUS, ORDER_COST) VALUES (?, ?, ?, ?)";
        String sqlDetails = "INSERT INTO ORDER_DETAILS (FK_ID_ORDER, FK_ID_MENU_ITEM, QUANTITY, SUBTOTAL) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtDetail = conn.prepareStatement(sqlDetails)) {

            conn.setAutoCommit(false);

            // Insert principal
            stmtOrder.setInt(1, tableId);
            stmtOrder.setInt(2, waiterId);
            stmtOrder.setString(3, status);
            stmtOrder.setDouble(4, cost);
            stmtOrder.executeUpdate();

            // Obtener folio generado
            ResultSet rs = stmtOrder.getGeneratedKeys();
            int newOrderId = 0;
            if (rs.next()) {
                newOrderId = rs.getInt(1);
            }

            // Insert detalles
            for (MenuItem item : platillos) {
                stmtDetail.setInt(1, newOrderId);
                stmtDetail.setInt(2, item.getId());
                stmtDetail.setInt(3, item.getQuantity());
                stmtDetail.setDouble(4, item.getCost() * item.getQuantity());
                stmtDetail.addBatch();
            }
            stmtDetail.executeBatch();

            conn.commit();
            System.out.println("✅ Orden creada con folio: " + newOrderId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // =======================
// Métodos nuevos para historial y filtros
// =======================

    public static List<FoodOrder> findAll() throws SQLException {
        String sql = """
        SELECT ID_ORDER, FK_ID_TABLE, FK_ID_WAITER, ORDER_TIME, ORDER_STATUS, ORDER_COST
        FROM ORDERS
        ORDER BY ORDER_TIME DESC
    """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<FoodOrder> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapOrderRow(rs));
            }
            return list;
        }
    }

    public static List<FoodOrder> findByStatus(String status) throws SQLException {
        String sql = """
        SELECT ID_ORDER, FK_ID_TABLE, FK_ID_WAITER, ORDER_TIME, ORDER_STATUS, ORDER_COST
        FROM ORDERS
        WHERE LOWER(ORDER_STATUS) = LOWER(?)
        ORDER BY ORDER_TIME DESC
    """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                List<FoodOrder> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapOrderRow(rs));
                }
                return list;
            }
        }
    }

    // (Opcional) Para mostrar los platillos de una orden
    public static List<MenuItem> findItemsByOrderId(int orderId) throws SQLException {
        String sql = """
        SELECT od.FK_ID_MENU_ITEM AS ID_ITEM,
               mi.NAME            AS NAME,   -- ajusta si tu columna es distinta
               od.QUANTITY        AS QTY,
               mi.PRICE           AS PRICE   -- o usa SUBTOTAL si no guardas precio unitario
        FROM ORDER_DETAILS od
        JOIN MENU_ITEM mi ON mi.ID_MENU_ITEM = od.FK_ID_MENU_ITEM
        WHERE od.FK_ID_ORDER = ?
        """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                List<MenuItem> items = new ArrayList<>();
                while (rs.next()) {
                    MenuItem item = new MenuItem(
                            rs.getInt("ID_ITEM"),
                            rs.getString("NAME"),
                            rs.getDouble("PRICE")
                    );
                    item.setQuantity(rs.getInt("QTY"));
                    items.add(item);
                }
                return items;
            }
        }
    }


    // -----------------------
// Mapeo interno reutilizable
// -----------------------
    private static FoodOrder mapOrderRow(ResultSet rs) throws SQLException {
        FoodOrder fo = new FoodOrder();
        fo.setOrderId(rs.getInt("ID_ORDER"));
        fo.setDiningTableId(rs.getInt("FK_ID_TABLE"));
        fo.setWaiterId(rs.getInt("FK_ID_WAITER"));

        Timestamp ts = rs.getTimestamp("ORDER_TIME");
        if (ts != null) {
            fo.setOrderDate(ts.toLocalDateTime().toLocalDate());
        }

        fo.setStatus(rs.getString("ORDER_STATUS"));
        fo.setTotalCost(rs.getDouble("ORDER_COST"));
        return fo;
    }


}
