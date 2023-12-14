package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	public static void createOrderItem(int orderId, int menuItemId, int quantity) {
		String query = "INSERT INTO orderitem (orderId, menuItemId, quantity) VALUES (?, ?, ?)";
		try (Connection connection = Connect.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, orderId);
			ps.setInt(2, menuItemId);
			ps.setInt(3, quantity);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateOrderItem(int orderId, int menuItemId, int quantity) {
		String query = "UPDATE orderitem SET quantity = ? WHERE orderId = ? AND menuItemId = ?";
		try (Connection connection = Connect.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, quantity);
			ps.setInt(2, orderId);
			ps.setInt(3, menuItemId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteOrderItem(int orderId, int menuItemId) {
		String query = "DELETE FROM orderitem WHERE orderId = ? AND menuItemId = ?";
		try (Connection connection = Connect.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, orderId);
			ps.setInt(2, menuItemId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<OrderItem> getAllOrderItemsByOrderId(int orderId) {
	    ArrayList<OrderItem> orderItemsList = new ArrayList<>();

	    String query = "SELECT * FROM orderitem WHERE orderId = ?";

	    try (Connection connection = Connect.getInstance().getConnection();
	         PreparedStatement ps = connection.prepareStatement(query)) {

	        ps.setInt(1, orderId);

	        try (ResultSet resultSet = ps.executeQuery()) {
	            while (resultSet.next()) {
	                int menuItemId = resultSet.getInt("menuItemId");
	                int quantity = resultSet.getInt("quantity");

	                MenuItem menuItem = MenuItemController.getMenuItemById(menuItemId);

	                OrderItem orderItem = new OrderItem(orderId, menuItem, quantity);
	                orderItemsList.add(orderItem);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return orderItemsList;
	}

}
