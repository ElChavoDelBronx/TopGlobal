package com.topglobal.dailyorder.dao;

import com.topglobal.dailyorder.config.DatabaseConfig;
import com.topglobal.dailyorder.models.objects.DiningTable;
import com.topglobal.dailyorder.models.objects.MenuCategory;
import com.topglobal.dailyorder.models.objects.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {
    public static void create(MenuItem menuItem) throws SQLException {
        String foodQuery = "INSERT INTO FOOD (FOOD_NAME, FOOD_DESCRIPTION, FOOD_COST, IS_ACTIVE) VALUES (?, ?, ?, ?)";
        String categoryQuery = "INSERT INTO FOOD_CATEGORY (FK_ID_FOOD, FK_ID_CATEGORY) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement stmtFood = null;
        PreparedStatement stmtCategory = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); //Desactivamos los cambios automáticos
            //Agregamos valores a la consulta preparada y ejecutamos la inserción
            stmtFood = conn.prepareStatement(foodQuery, new String[]{"ID_FOOD"});
            stmtFood.setString(1, menuItem.getName());
            stmtFood.setString(2, menuItem.getDescription());
            stmtFood.setDouble(3, menuItem.getCost());
            stmtFood.setString(4, menuItem.getImage());
            stmtFood.setInt(4, menuItem.getIsActive());
            stmtFood.executeUpdate();
            // Obtenemos el ID generado para la comida insertada
            ResultSet rs = stmtFood.getGeneratedKeys();
            int generatedId = -1;
            if (rs.next()) {
                generatedId = rs.getInt(1);
            } else {
                throw new SQLException("No se pudo obtener el ID de la comida insertada.");
            }
            //Agregamos valores a la consulta preparada y ejecutamos la inserción en la tabla de relación
            stmtCategory = conn.prepareStatement(categoryQuery);
            stmtCategory.setInt(1, generatedId);
            stmtCategory.setInt(2, menuItem.getCategory().getId());
            stmtCategory.executeUpdate();
            conn.commit(); // Confirmamos los cambios
            System.out.println("Comida creada con éxito");
        } catch (Exception e) {
            if(conn != null) { conn.rollback(); }
            throw new RuntimeException("Error al agregar un nuevo elemento del menú: " + e.getMessage());
        } finally {
            if(stmtFood != null) { stmtFood.close(); }
            if(stmtCategory != null) { stmtCategory.close(); }
            if(conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    public static List<MenuItem> findAll() throws SQLException {
        String sql = "SELECT\n" +
                "    f.id_food,\n" +
                "    f.food_name,\n" +
                "    f.food_description,\n" +
                "    f.food_cost,\n" +
                "    f.is_active,\n" +
                "    mc.id_category,\n" +
                "    mc.category_description\n" +
                "FROM\n" +
                "    food          f\n" +
                "    LEFT JOIN food_category fc ON f.id_food = fc.fk_id_food\n" +
                "    LEFT JOIN menu_category mc ON mc.id_category = fc.fk_id_category\n" +
                "ORDER BY\n" +
                "    mc.category_description ASC, f.food_name ASC";
        List<MenuItem> items = new ArrayList<>();
        try{
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MenuItem item = new MenuItem();
                MenuCategory category = new MenuCategory();

                item.setId(rs.getInt("ID_FOOD"));
                item.setName(rs.getString("FOOD_NAME"));
                item.setDescription(rs.getString("FOOD_DESCRIPTION"));
                item.setCost(rs.getDouble("FOOD_COST"));
                item.setIsActive(rs.getInt("IS_ACTIVE"));
                category.setId(rs.getInt("ID_CATEGORY"));
                category.setDescription(rs.getString("CATEGORY_DESCRIPTION"));
                item.setCategory(category);
                items.add(item);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return items;
    }
    public static void update(MenuItem menuItem, MenuCategory oldCategory) throws SQLException {
        String updateFood = "UPDATE FOOD SET FOOD_NAME=?, FOOD_DESCRIPTION=?, FOOD_COST=?, IS_ACTIVE=? WHERE ID_FOOD=?";
        String updateCategoryRelation = "UPDATE FOOD_CATEGORY SET FK_ID_CATEGORY=? WHERE FK_ID_FOOD=? AND FK_ID_CATEGORY=?";

        Connection conn = null;
        PreparedStatement stmtFood = null;
        PreparedStatement stmtCategory = null;

        try {
            conn = DatabaseConfig.getConnection();
            //Desactivar cambios automaticos en la base de datos
            conn.setAutoCommit(false);

            // Actualizar datos del platillo en la consulta
            stmtFood = conn.prepareStatement(updateFood);
            stmtFood.setString(1, menuItem.getName());
            stmtFood.setString(2, menuItem.getDescription());
            stmtFood.setDouble(3, menuItem.getCost());
            stmtFood.setInt(4, menuItem.getIsActive());
            stmtFood.setInt(5, menuItem.getId());
            stmtFood.executeUpdate();
            // Actualizar la relación con la categoría
            stmtCategory = conn.prepareStatement(updateCategoryRelation);
            stmtCategory.setInt(1, menuItem.getCategory().getId());
            stmtCategory.setInt(2, menuItem.getId());
            stmtCategory.setInt(3, oldCategory.getId());
            stmtCategory.executeUpdate();

            conn.commit();
            System.out.println("Datos del platillo actualizados con éxito.");

        } catch (Exception e) {
            if (conn != null){ conn.rollback(); } //Si ocurre un error, se abortan los cambios
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar empleado: " + e.getMessage());
        } finally { //Este bloque siempre se ejecuta, independientemente de si hubo una excepción o no
            if (stmtFood != null) { stmtFood.close(); }
            if (stmtCategory != null) { stmtCategory.close(); }
            if (conn != null) {
                conn.setAutoCommit(true); //Reactivar cambios automaticos en la base de datos
                conn.close();
            }
        }
    }
}
