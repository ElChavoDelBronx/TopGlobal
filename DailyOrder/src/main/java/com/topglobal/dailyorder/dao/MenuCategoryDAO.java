package com.topglobal.dailyorder.dao;

import com.topglobal.dailyorder.config.DatabaseConfig;
import com.topglobal.dailyorder.models.objects.MenuCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuCategoryDAO {
    public static MenuCategory findCategory(String categoryDesc) throws SQLException {
        String sql = "SELECT ID_CATEGORY, CATEGORY_DESCRIPTION FROM MENU_CATEGORY WHERE CATEGORY_DESCRIPTION = ?";
        MenuCategory category = null;
        try{
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, categoryDesc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                category = new MenuCategory();
                category.setId(rs.getInt("ID_CATEGORY"));
                category.setDescription(rs.getString("CATEGORY_DESCRIPTION"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return category;
    }
    public static List<MenuCategory> findCategories() throws SQLException {
        String sql = "SELECT ID_CATEGORY, CATEGORY_DESCRIPTION FROM MENU_CATEGORY ORDER BY CATEGORY_DESCRIPTION ASC";
        List<MenuCategory> categories = new ArrayList<>();
        try {
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MenuCategory category = new MenuCategory();
                category.setId(rs.getInt("ID_CATEGORY"));
                category.setDescription(rs.getString("CATEGORY_DESCRIPTION"));
                categories.add(category);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return categories;
    }
}
