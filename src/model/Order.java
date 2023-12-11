package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Order {
	private int orderId;
	private int orderUserId;
	private User orderUser;
	private ArrayList<OrderItem> orderItem;
	private String orderStatus;
	private Date orderDate;
	private double orderTotal;
	
	public Order(int orderId, int orderUserId, String orderStatus, Date orderDate) {
		super();
		this.orderId = orderId;
		this.orderUserId = orderUserId;
		this.orderStatus = orderStatus;
		this.orderDate = orderDate;
	}

	
	
	public static void createOrder(User orderUser, ArrayList<OrderItem> orderItems, Date orderDate){
		String query = "INSERT INTO orders (orderId, userId, orderStatus, orderDate) VALUES (? ,? ,? ,? );";
		  
		try (Connection connection = Connect.getInstance().getConnection()){
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt   (1, 0);
			ps.setInt	  (2, orderUser.getUserId());
			ps.setString(3, "Pending");
			ps.setDate  (4, (java.sql.Date) orderDate);
			ps.executeUpdate();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Order> getOrdersByCustomerId(int customerId){
		ArrayList<Order> orders = new ArrayList<Order>();
		String query = "SELECT * FROM orders WHERE userId = ?;";
		  
		try (Connection connection = Connect.getInstance().getConnection()){
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt   (1, customerId);
			ResultSet resultSet = ps.executeQuery();
			while(resultSet.next()){
				int id = resultSet.getInt("orderId");
				int userId = resultSet.getInt("userId");
				String status = resultSet.getString("orderStatus");
				Date date = resultSet.getDate("orderDate");
				orders.add(new Order(id, userId, status, date));
			}
			resultSet.close();
			
			for (Order order : orders){
				order.setOrderUser(User.getUserById(order.getOrderUserId()));
				order.setOrderItems(OrderItem.getAllOrderItemsByOrderId(order.getOrderId()));
				order.setOrderTotal(Order.getTotalByOrderId(order.getOrderId()));
			}
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		return orders;
	}
	
	public static ArrayList<Order> getAllOrders(){
		ArrayList<Order> orders = new ArrayList<>();
		String query = "SELECT * FROM orders;";
		
		try (Connection connection = Connect.getInstance().getConnection()){
			PreparedStatement prep = connection.prepareStatement(query);
			ResultSet resultSet = prep.executeQuery();
			
			while(resultSet.next()) {
				int id = resultSet.getInt("orderId");
				int userId = resultSet.getInt("userId");
				String status = resultSet.getString("orderStatus");
				Date date = resultSet.getDate("orderDate");
				orders.add(new Order(id, userId, status, date));
			}
			resultSet.close();
			
			for (Order order : orders){
				order.setOrderUser(User.getUserById(order.getOrderUserId()));
				order.setOrderItems(OrderItem.getAllOrderItemsByOrderId(order.getOrderId()));
				order.setOrderTotal(Order.getTotalByOrderId(order.getOrderId()));
			}
		} 
		catch (SQLException e){
			e.printStackTrace();
		} 
		return orders;
	}
	
	public static Order getOrderById(int orderId)	{
		Order order = null;
		String query = "SELECT * FROM orders WHERE orderId = ?;";
		
		try (Connection connection = Connect.getInstance().getConnection()){
			PreparedStatement prep = connection.prepareStatement(query);
			prep.setInt(1, orderId);
			ResultSet resultSet = prep.executeQuery();

			if(resultSet.next()){
				int orderUserId = resultSet.getInt("userId");
				String orderStatus = resultSet.getString("orderStatus");
				Date orderDate = resultSet.getDate("orderDate");
				order = new Order(orderId, orderUserId, orderStatus, orderDate);
			}
			resultSet.close();
			order.setOrderUser(User.getUserById(order.getOrderUserId()));
			order.setOrderItems(OrderItem.getAllOrderItemsByOrderId(order.getOrderId()));
			order.setOrderTotal(Order.getTotalByOrderId(order.getOrderId()));
		} 
		catch (SQLException e){
			e.printStackTrace();
		} 
		return order;
	}
	
	public static void updateOrder(int orderId, ArrayList<OrderItem> orderItems, String orderStatus){
		String deleteOrderItemsQuery = "DELETE FROM orderitems WHERE orderId = ?";
		try (Connection connection = Connect.getInstance().getConnection()){
			PreparedStatement ps = connection.prepareStatement(deleteOrderItemsQuery);
			ps.setInt(1, orderId);
			ps.executeUpdate();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		
		String reinsertOrderItemsQuery = "INSERT INTO orderitems (orderId, menuItemId, quantity) VALUES (?, ?, ?);";
		try (Connection connection = Connect.getInstance().getConnection())
		{
			PreparedStatement ps = connection.prepareStatement(reinsertOrderItemsQuery);
			for (OrderItem orderItem : orderItems) {
				ps.setInt(1, orderId);
				ps.setInt(2, orderItem.getMenuItem().getMenuItemId());
				ps.setInt(3, orderItem.getQuantity());
				ps.executeUpdate();
				
			}
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		
		
		String statusQuery = "UPDATE orders SET orderStatus = ? WHERE orderId = ?;";
		try (Connection connection = Connect.getInstance().getConnection()){
			PreparedStatement prep = connection.prepareStatement(statusQuery);
			prep.setString(1, orderStatus);
			prep.setInt(2, orderId);
			prep.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void deleteOrder(int orderId){
		String query = "DELETE FROM orders WHERE orderId = ?";
		  
		try (Connection connection = Connect.getInstance().getConnection()){
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, orderId);
			ps.executeUpdate();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static double getTotalByOrderId(int orderId){
		int orderTotalPrice = 0;
		String query = "SELECT * FROM orderitems JOIN menuitems ON orderitems.menuItemId = menuitems.menuItemId WHERE orderitems.orderId = ?;";
		  
		try (Connection connection = Connect.getInstance().getConnection()){
			PreparedStatement prep = connection.prepareStatement(query);
			prep.setInt(1, orderId);
			ResultSet resultSet = prep.executeQuery();
			
			while(resultSet.next())
			{
				int quantity = resultSet.getInt("quantity");
				double menuItemPrice = resultSet.getDouble("menuItemPrice");
				orderTotalPrice += (double) quantity * menuItemPrice;
			}
			resultSet.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		return orderTotalPrice;
	}
	



	public int getOrderId() {
		return orderId;
	}



	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}



	public int getOrderUserId() {
		return orderUserId;
	}



	public void setOrderUserId(int orderUserId) {
		this.orderUserId = orderUserId;
	}



	public User getOrderUser() {
		return orderUser;
	}



	public void setOrderUser(User orderUser) {
		this.orderUser = orderUser;
	}



	public ArrayList<OrderItem> getOrderItem() {
		return orderItem;
	}



	public void setOrderItem(ArrayList<OrderItem> orderItem) {
		this.orderItem = orderItem;
	}



	public String getOrderStatus() {
		return orderStatus;
	}



	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}



	public Date getOrderDate() {
		return orderDate;
	}



	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}



	public int getOrderTotal() {
		return orderTotal;
	}



	public void setOrderTotal(double d) {
		this.orderTotal = d;
	}
	
}
