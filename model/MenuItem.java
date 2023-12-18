package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MenuItem {
	private int menuItemId;
	private String menuItemName;
	private String menuItemDescription;
	private double menuItemPrice;
	
	public MenuItem(int menuItemId, String menuItemName, String menuItemDescription, double menuItemPrice) {
		this.menuItemId = menuItemId;
		this.menuItemName = menuItemName;
		this.menuItemDescription = menuItemDescription;
		this.menuItemPrice = menuItemPrice;
	}
	
    public static String createMenuItem(String menuItemName, String menuItemDescription, double menuItemPrice) {
    	
        String query = "INSERT INTO menuitem (menuItemId, menuItemName, menuItemDescription, menuItemPrice) VALUES (?, ?, ?, ?)";
    	try (Connection connection = Connect.getInstance().getConnection();
    	  PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setInt(1, 0);
    		ps.setString(2, menuItemName);
    		ps.setString(3, menuItemDescription);
    		ps.setDouble(4, menuItemPrice);
    		ps.executeUpdate();
    	} catch (SQLException e) {
    	  e.printStackTrace();
    	}
        return "Menu Item created: " + menuItemName;
    }
	
    public static void updateMenuItem(int menuItemId, String menuItemName, String menuItemDescription, double menuItemPrice) {
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
	
    public static void deleteMenuItem(int menuItemId) {
  	String query = "DELETE FROM menuitem WHERE menuItemId = ?";
      try (Connection connection = Connect.getInstance().getConnection();
      	PreparedStatement ps = connection.prepareStatement(query)) {
      	ps.setInt(1, menuItemId);
          ps.executeUpdate();
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }
	
    public static MenuItem getMenuItemById(int menuItemId) {
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
	
    public static ArrayList<MenuItem> getAllMenuItems() {
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

	public int getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(int menuItemId) {
		this.menuItemId = menuItemId;
	}

	public String getMenuItemName() {
		return menuItemName;
	}

	public void setMenuItemName(String menuItemName) {
		this.menuItemName = menuItemName;
	}

	public String getMenuItemDescription() {
		return menuItemDescription;
	}

	public void setMenuItemDescription(String menuItemDescription) {
		this.menuItemDescription = menuItemDescription;
	}

	public double getMenuItemPrice() {
		return menuItemPrice;
	}

	public void setMenuItemPrice(double menuItemPrice) {
		this.menuItemPrice = menuItemPrice;
	}
}
