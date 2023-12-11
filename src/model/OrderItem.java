package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import controller.MenuItemController;

public class OrderItem {
	private int orderId;
	private MenuItem menuItem;
	private int quantity;
	
	public OrderItem(int orderId, MenuItem menuItem, int quantity) {
		this.orderId = orderId;
		this.menuItem = menuItem;
		this.quantity = quantity;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
    public static ArrayList<OrderItem> getAllOrderItemsByOrderId(int orderId) {
        ArrayList<OrderItem> orderItemsList = new ArrayList<>();
        
//        for (OrderItem orderItem : orderItems) {
//            if (orderItem.getOrderId() == (orderId)) {
//                orderItemsList.add(orderItem);
//            }
//        }
//        
//        for (OrderItem orderItem : orderItemsList) {
//            System.out.println("OrderItem: " + orderItem.getMenuItemId() + ", Quantity: " + orderItem.getQuantity());
//        }
        
        String query = "SELECT * FROM orderitem";
        try (Connection connection = Connect.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
				int id = resultSet.getInt("orderItemId");
				int menuItemId = resultSet.getInt("menuItemId");
				
				MenuItemController menuItemController = new MenuItemController();
				MenuItem menuItem = menuItemController.getMenuItemById(menuItemId);
				
				int quantity = resultSet.getInt("quantity");
				
                OrderItem orderItem = new OrderItem(id,menuItem,quantity);
                orderItemsList.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItemsList;
    }
	
	
}
