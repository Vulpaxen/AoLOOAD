package controller;

import java.util.ArrayList;

import model.MenuItem;
import model.OrderItem;
import model.User;

public class MenuItemController {
    private ArrayList<MenuItem> menuItems;
    
    public void createMenuItem(String menuItemName, String menuItemDescription, double menuItemPrice) {
    	
        if (menuItemName == null || menuItemName.isEmpty()) {
            System.out.println("Error: Name cannot be empty.");
            return;
        }
        
        if (!isNameUnique(menuItemName)) {
            System.out.println("Error: Name must be unique.");
            return;
        }
        
        if (menuItemDescription.length() < 10) {
            System.out.println("Error: Description must be more than 10 characters.");
            return;
        }
        
        if (menuItemPrice >= 2.5) {
            System.out.println("Error: Price must be greater than or equal to 2.5");
            return;
        }
    	
        MenuItem newMenuItem = new MenuItem(menuItemName, menuItemDescription, menuItemPrice);
        menuItems.add(newMenuItem);
        System.out.println("Menu Item created: " + newMenuItem);
    }
    
    private boolean isNameUnique(String menuItemName) {
        for (MenuItem item : menuItems) {
            if (item.menuItemName.equals(menuItemName)) {
                return false;
            }
        }
        return true;
    }
    
    public void updateMenuItem(String menuItemId, String menuItemName, String menuItemDescription, double menuItemPrice) {
        for (MenuItem menuItem : menuItems) {
            if (menuItem.menuItemId.equals(menuItemId)) {
                menuItem.menuItemName = menuItemName;
                menuItem.menuItemDescription = menuItemDescription;
                menuItem.menuItemPrice = menuItemPrice;
                System.out.println("Menu Item updated: " + menuItem);
                return;
            }
        }
        System.out.println("Menu Item not found with ID: " + menuItemId);
    }
    
    public void deleteMenuItem(String menuItemId) {
        for (int i = 0; i < menuItems.size(); i++) {
        	MenuItem item = menuItems.get(i);
            if (item.menuItemId.equals(menuItemId)) {
            	menuItems.remove(i);
                System.out.println("Menu Item deleted: " + item);
                return;
            }
        }
        System.out.println("Menu not found with ID: " + menuItemId);
    }
    
    public MenuItem getMenuItemById(String menuItemId) {
        for (MenuItem menuItem : menuItems) {
            if (menuItem.menuItemId.equals(menuItemId)) {
                System.out.println("Menu Item found by ID: " + menuItem);
                return menuItem;
            }
        }
        System.out.println("Menu Item not found with ID: " + menuItemId);
        return null;
    }

    public ArrayList<MenuItem> getAllMenuItems() {
        for (MenuItem menuItem : menuItems) {
            System.out.println("MenuItem: " + menuItem.menuItemName + ", Description: " + menuItem.menuItemDescription
                    + ", Price: " + menuItem.menuItemPrice);
        }
        return new ArrayList<>(menuItems);
    }
    
    
}
