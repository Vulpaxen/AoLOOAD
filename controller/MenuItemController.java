package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Connect;
import model.MenuItem;
import model.OrderItem;
import model.User;

public class MenuItemController {
    private static ArrayList<MenuItem> menuItems;
    /*CREATE TABLE `menuitem` (
  `menuItemId` int(11) NOT NULL,
  `menuItemName` varchar(30) NOT NULL,
  `menuItemDescription` varchar(100) NOT NULL,
  `menuItemPrice` double NOT NULL
)*/
    public static String createMenuItem(String menuItemName, String menuItemDescription, double menuItemPrice) {
    	
        if (menuItemName == null || menuItemName.isEmpty()) {
            return "Error: Name cannot be empty.";
        }
        
        if (!isNameUnique(menuItemName)) {
            return "Error: Name must be unique.";
        }
        
        if (menuItemDescription.length() <= 10) {
            return "Error: Description must be more than 10 characters.";
        }
        
        if (menuItemPrice < 2.5) {
            return "Error: Price must be greater than or equal to 2.5";
        }
        
        MenuItem.createMenuItem(menuItemName, menuItemDescription, menuItemPrice);
    	
        return "Menu Item created: " + menuItemName;
    }
    
    private static boolean isNameUnique(String menuItemName) {
        for (MenuItem item : menuItems) {
            if (item.getMenuItemName().equals(menuItemName)) {
                return false;
            }
        }
        return true;
    }
    
    public static void updateMenuItem(int menuItemId, String menuItemName, String menuItemDescription, double menuItemPrice) {
    	MenuItem.updateMenuItem(menuItemId, menuItemName, menuItemDescription, menuItemPrice);
    }
    
    public static void deleteMenuItem(int menuItemId) {
    	MenuItem.deleteMenuItem(menuItemId);
    }
    
    public static MenuItem getMenuItemById(int menuItemId) {
    	return MenuItem.getMenuItemById(menuItemId);
    }

    public static ArrayList<MenuItem> getAllMenuItems() {
        return MenuItem.getAllMenuItems();
    }
    
    
}
