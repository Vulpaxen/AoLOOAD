package controller;

import java.util.ArrayList;

import model.MenuItem;
import model.OrderItem;

public class MenuItemController {
    private ArrayList<MenuItem> menuItems;
    
    public void createMenuItem(String menuItemName, String menuItemDescription, int menuItemPrice) {
        MenuItem newMenuItem = new MenuItem(menuItemName, menuItemDescription, menuItemPrice);
        menuItems.add(newMenuItem);
        System.out.println("MenuItem created: " + newMenuItem);
    }
    
    public void updateMenuItem(String menuItemId, String menuItemName, String menuItemDescription, int menuItemPrice) {
        for (MenuItem menuItem : menuItems) {
            if (menuItem.menuItemId.equals(menuItemId)) {
                menuItem.menuItemName = menuItemName;
                menuItem.menuItemDescription = menuItemDescription;
                menuItem.menuItemPrice = menuItemPrice;
                System.out.println("MenuItem updated: " + menuItem);
                return;
            }
        }
        System.out.println("MenuItem not found with ID: " + menuItemId);
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
                System.out.println("MenuItem found by ID: " + menuItem);
                return menuItem;
            }
        }
        System.out.println("MenuItem not found with ID: " + menuItemId);
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
