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
    private ArrayList<MenuItem> menuItems;
    /*CREATE TABLE `menuitem` (
  `menuItemId` int(11) NOT NULL,
  `menuItemName` varchar(30) NOT NULL,
  `menuItemDescription` varchar(100) NOT NULL,
  `menuItemPrice` double NOT NULL
)*/
    public String createMenuItem(String menuItemName, String menuItemDescription, double menuItemPrice) {
    	
        if (menuItemName == null || menuItemName.isEmpty()) {
            return "Error: Name cannot be empty.";
        }
        
        if (!isNameUnique(menuItemName)) {
            return "Error: Name must be unique.";
        }
        
        if (menuItemDescription.length() < 10) {
            return "Error: Description must be more than 10 characters.";
        }
        
        if (menuItemPrice >= 2.5) {
            return "Error: Price must be greater than or equal to 2.5";
        }
    	
        String query = "INSERT INTO menuitem (menuItemName, menuItemDescription, menuItemPrice) VALUES (?, ?, ?)";
    	try (Connection connection = Connect.getInstance().getConnection();
    	  PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setString(1, menuItemName);
    		ps.setString(2, menuItemDescription);
    		ps.setDouble(3, menuItemPrice);
    		ps.executeUpdate();
    	} catch (SQLException e) {
    	  e.printStackTrace();
    	}
//        MenuItem newMenuItem = new MenuItem(menuItemName, menuItemDescription, menuItemPrice);
//        menuItems.add(newMenuItem);
        return "Menu Item created: " + menuItemName;
    }
    
    private boolean isNameUnique(String menuItemName) {
        for (MenuItem item : menuItems) {
            if (item.getMenuItemName().equals(menuItemName)) {
                return false;
            }
        }
        return true;
    }
    
    public void updateMenuItem(int menuItemId, String menuItemName, String menuItemDescription, double menuItemPrice) {
//        for (MenuItem menuItem : menuItems) {
//            if (menuItem.menuItemId.equals(menuItemId)) {
//                menuItem.menuItemName = menuItemName;
//                menuItem.menuItemDescription = menuItemDescription;
//                menuItem.menuItemPrice = menuItemPrice;
//                System.out.println("Menu Item updated: " + menuItem);
//                return;
//            }
//        }
    	
//        System.out.println("Menu Item not found with ID: " + menuItemId);
    	String query = "UPDATE menuitem SET menuItemName = ?, menuItemDescription = ?, menuItemPrice = ? WHERE menuItemId = ?";
    	try (Connection connection = Connect.getInstance().getConnection();
    		PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setString(1, menuItemName);
    		ps.setString(2, menuItemDescription);
    		ps.setDouble(3, menuItemPrice);
    		ps.setInt(4, menuItemId);
    		ps.executeUpdate();
    	} catch (SQLException e) {
    	  e.printStackTrace();
    	}
    }
    
    public void deleteMenuItem(int menuItemId) {
//        for (int i = 0; i < menuItems.size(); i++) {
//        	MenuItem item = menuItems.get(i);
//            if (item.menuItemId.equals(menuItemId)) {
//            	menuItems.remove(i);
//                System.out.println("Menu Item deleted: " + item);
//                return;
//            }
//        }
//        System.out.println("Menu not found with ID: " + menuItemId);
    	
    	String query = "DELETE FROM menuitem WHERE menuItemId = ?";
        try (Connection connection = Connect.getInstance().getConnection();
        	PreparedStatement ps = connection.prepareStatement(query)) {
        	ps.setInt(1, menuItemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public MenuItem getMenuItemById(int menuItemId) {
//        for (MenuItem menuItem : menuItems) {
//            if (menuItem.menuItemId.equals(menuItemId)) {
//                System.out.println("Menu Item found by ID: " + menuItem);
//                return menuItem;
//            }
//        }
//        System.out.println("Menu Item not found with ID: " + menuItemId);
    	MenuItem menuItem = null;
		
		try(Connection connection = Connect.getInstance().getConnection()){
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM menuitem WHERE menuItemId = ?;");
			ps.setInt(1,  menuItemId);
			ResultSet resultSet = ps.executeQuery();
			
			if(resultSet.next()){
				int id = resultSet.getInt("menuItemId");
				String name = resultSet.getString("menuItemName");
				String description = resultSet.getString("menuItemDescription");
				int price = resultSet.getInt("menuItemPrice");
				menuItem = new MenuItem(id, name, description, price);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return menuItem;
    }

    public ArrayList<MenuItem> getAllMenuItems() {
//        for (MenuItem menuItem : menuItems) {
//            System.out.println("MenuItem: " + menuItem.menuItemName + ", Description: " + menuItem.menuItemDescription
//                    + ", Price: " + menuItem.menuItemPrice);
//        }
//        return new ArrayList<>(menuItems);
        
        ArrayList<MenuItem> menuItemList = new ArrayList<>();
        String query = "SELECT * FROM menuitem";
        try (Connection connection = Connect.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
				int id = resultSet.getInt("menuItemId");
				String name = resultSet.getString("menuItemName");
				String description = resultSet.getString("menuItemDescription");
				int price = resultSet.getInt("menuItemPrice");
				MenuItem menuItem = new MenuItem(id, name, description, price);
                menuItemList.add(menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItemList;
    }
    
    
}
