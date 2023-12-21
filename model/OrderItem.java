package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderItem {

	private int orderId;
	private int menuItemId;
	private int quantity;

	private MenuItem menuItem;

	public OrderItem(int orderId, MenuItem menuItem, int quantity) {
		this.orderId = orderId;
		this.menuItem = menuItem;
		this.quantity = quantity;
	}

	public static void createOrderItem(int orderId, MenuItem menuItem, int quantity) {
		String query = "INSERT INTO orderitem (orderId, menuItemId, quantity) VALUES (?, ?, ?)";
		try (Connection connection = Connect.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, orderId);
			ps.setInt(2, menuItem.getMenuItemId());
			ps.setInt(3, quantity);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateOrderItem(int orderId, MenuItem menuItem, int quantity) {
		String query = "UPDATE orderitem SET quantity = ? WHERE orderId = ? AND menuItemId = ?";
		try (Connection connection = Connect.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, quantity);
			ps.setInt(2, orderId);
			ps.setInt(3, menuItem.getMenuItemId());
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
		ArrayList<OrderItem> orderItemList = new ArrayList<>();

		String query = "SELECT * FROM orderitem "
				+ "JOIN menuItem ON menuItem.menuItemid = orderItem.menuItemId WHERE orderId = ?";

		try (Connection connection = Connect.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {

			ps.setInt(1, orderId);

			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					int id = resultSet.getInt("orderId");
					int menuItemId = resultSet.getInt("menuItemId");
					int quantity = resultSet.getInt("quantity");
					String menuItemName = resultSet.getString("menuItemName");
					String menuItemDescription = resultSet.getString("menuItemDescription");
					double menuItemPrice = resultSet.getDouble("menuItemPrice");

					MenuItem menuItem = new MenuItem(menuItemId, menuItemName, menuItemDescription, menuItemPrice);
					orderItemList.add(new OrderItem(id, menuItem, quantity));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return orderItemList;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getMenuItemId() {
		return menuItem.getMenuItemId();
	}

	public void setMenuItemId(int menuItemId) {
		this.menuItemId = menuItemId;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = MenuItem.getMenuItemById(menuItemId);
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getMenuItemName() {
		return menuItem.getMenuItemName();
	}

	public String getMenuItemDesc() {
		return menuItem.getMenuItemDescription();
	}

}
