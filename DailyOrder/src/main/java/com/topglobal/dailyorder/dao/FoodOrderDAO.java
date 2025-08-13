package com.topglobal.dailyorder.dao;

import com.topglobal.dailyorder.config.DatabaseConfig;
import com.topglobal.dailyorder.models.objects.FoodOrder;
import com.topglobal.dailyorder.models.objects.MenuItem;
import javafx.collections.ObservableList;

import java.sql.*;
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


    public static void createOrder(FoodOrder order, int tableID, int waiterID, List<MenuItem> dishes) throws SQLException {
        String orderQuery = """
        INSERT INTO ORDERS (ID_ORDER, FK_ID_TABLE, FK_ID_WAITER, ORDER_TIME, ORDER_STATUS, ORDER_COST)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        String orderFoodQuery = """
        INSERT INTO ORDER_FOOD (FK_ID_ORDER, FK_ID_FOOD, FOOD_QUANTITY, TOTAL_COST)
        VALUES (?, ?, ?, ?)
    """;

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false); // desactivar commit automático

            // Insertar la orden principal
            try (PreparedStatement stmtOrder = conn.prepareStatement(orderQuery)) {
                stmtOrder.setInt(1, order.getOrderId());
                stmtOrder.setInt(2, tableID);
                stmtOrder.setInt(3, waiterID);
                stmtOrder.setTimestamp(4, java.sql.Timestamp.valueOf(order.getOrderDate().atStartOfDay())); // incluir fecha y hora
                stmtOrder.setString(5, order.getOrderStatus().toLowerCase());
                stmtOrder.setDouble(6, order.getTotalCost());
                stmtOrder.executeUpdate();
            }

            // Insertar cada platillo en ORDER_FOOD
            try (PreparedStatement stmtOrderFood = conn.prepareStatement(orderFoodQuery)) {
                for (MenuItem menuItem : dishes) {
                    stmtOrderFood.setInt(1, order.getOrderId());
                    stmtOrderFood.setInt(2, menuItem.getId());
                    stmtOrderFood.setInt(3, menuItem.getQuantity());
                    stmtOrderFood.setDouble(4, menuItem.getCost() * menuItem.getQuantity());
                    stmtOrderFood.addBatch(); // usar batch para eficiencia
                }
                stmtOrderFood.executeBatch();
            }

            conn.commit(); // confirmar cambios
            System.out.println("Orden guardada con éxito");

        } catch (SQLException e) {
            throw new SQLException("Error al crear nueva orden: " + e.getMessage(), e);
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

    public static List<FoodOrder> getOrdersByWaiterId(int waiterId) throws SQLException {
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
        WHERE o.FK_ID_WAITER = ?
        GROUP BY
            o.DAILY_FOLIO, o.ORDER_TIME, o.ORDER_STATUS, o.ORDER_COST,
            e.NAME, e.FATHER_LASTNAME, dt.ID_TABLE, dt.AREA
        ORDER BY o.ORDER_TIME DESC
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, waiterId);

            try (ResultSet rs = stmt.executeQuery()) {
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
        }

        return orders;
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

    // Para mostrar los platillos de una orden según la estructura de getAllOrders
    public static List<MenuItem> findItemsByOrderId(int orderId) throws SQLException {
        // Aquí asumimos que la tabla ORDERS tiene un campo item_ids con los IDs de los platillos separados por coma
        String sqlOrder = "SELECT item_ids FROM orders WHERE id_order = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement psOrder = conn.prepareStatement(sqlOrder)) {

            psOrder.setInt(1, orderId);

            try (ResultSet rsOrder = psOrder.executeQuery()) {
                if (!rsOrder.next()) {
                    return new ArrayList<>(); // No existe la orden
                }

                String itemIdsCsv = rsOrder.getString("item_ids"); // ej: "1,3,5"
                if (itemIdsCsv == null || itemIdsCsv.isEmpty()) {
                    return new ArrayList<>();
                }

                String[] itemIds = itemIdsCsv.split(",");
                StringBuilder placeholders = new StringBuilder();
                for (int i = 0; i < itemIds.length; i++) {
                    placeholders.append("?");
                    if (i < itemIds.length - 1) placeholders.append(",");
                }

                String sqlItems = "SELECT id_menu_item, name, price FROM menu_item WHERE id_menu_item IN (" + placeholders + ")";
                try (PreparedStatement psItems = conn.prepareStatement(sqlItems)) {
                    for (int i = 0; i < itemIds.length; i++) {
                        psItems.setInt(i + 1, Integer.parseInt(itemIds[i].trim()));
                    }

                    try (ResultSet rsItems = psItems.executeQuery()) {
                        List<MenuItem> items = new ArrayList<>();
                        while (rsItems.next()) {
                            MenuItem item = new MenuItem(
                                    rsItems.getInt("id_menu_item"),
                                    rsItems.getString("name"),
                                    rsItems.getDouble("price")
                            );
                            items.add(item);
                        }
                        return items;
                    }
                }
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

        fo.setOrderStatus(rs.getString("ORDER_STATUS"));
        fo.setTotalCost(rs.getDouble("ORDER_COST"));
        return fo;
    }
}
