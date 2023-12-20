package controller;

import java.util.ArrayList;
import model.MenuItem;

public class MenuItemController {
    private static ArrayList<MenuItem> menuItems;

    public static String createMenuItem(String menuItemName, String menuItemDescription, double menuItemPrice) {
    	
        if (menuItemName == null || menuItemName.isEmpty()) {
            return "Error: Name cannot be empty.";
        }
        
//        if (!isNameUnique(menuItemName)) {
//            return "Error: Name must be unique.";
//        }
//        
        if (menuItemDescription.length() <= 10) {
            return "Error: Description must be more than 10 characters.";
        }
        
        if (menuItemPrice < 2.5) {
            return "Error: Price must be greater than or equal to 2.5";
        }
        
        MenuItem.createMenuItem(menuItemName, menuItemDescription, menuItemPrice);
    	
        return "Menu Item created: " + menuItemName;
    }
//    
//    private static boolean isNameUnique(String menuItemName) {
//        for (MenuItem item : menuItems) {
//            if (item.getMenuItemName().equals(menuItemName)) {
//                return false;
//            }
//        }
//        return true;
//    }
//    
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
