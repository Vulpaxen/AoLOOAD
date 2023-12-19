package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
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

	public static int createOrder(User orderUser, ArrayList<OrderItem> orderItem, Date orderDate, double OrderTotal) {
	    String query = "INSERT INTO orders (userId, orderStatus, orderDate, orderTotal) VALUES (?, ?, ?, ?);";

	    try (Connection connection = Connect.getInstance().getConnection();
	         PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        ps.setInt(1, orderUser.getUserId());
	        ps.setString(2, "Pending");
	        ps.setDate(3, orderDate);
	        ps.setDouble(4, OrderTotal);

	        int affectedRows = ps.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Creating order failed, no rows affected.");
	        }

	        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                int orderId = generatedKeys.getInt(1);
	                return orderId;
	            } else {
	                throw new SQLException("Creating order failed, no ID obtained.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return -1;
	}


	public static ArrayList<Order> getOrdersByCustomerId(int customerId) {
		ArrayList<Order> order = new ArrayList<Order>();
		String query = "SELECT * FROM orders WHERE userId = ?;";

		try (Connection connection = Connect.getInstance().getConnection()) {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, customerId);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("orderId");
				int userId = resultSet.getInt("userId");
				String status = resultSet.getString("orderStatus");
				Date date = resultSet.getDate("orderDate");
				order.add(new Order(id, userId, status, date));
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (Order orders : order)
		{
			orders.setOrderUser(User.getUserById(orders.getOrderUserId()));
			orders.setOrderItem(OrderItem.getAllOrderItemsByOrderId(orders.getOrderId()));
			orders.setOrderTotal(Order.getTotalByOrderId(orders.getOrderId()));
		}
		
		return order;
	}

	public static ArrayList<Order> getAllOrders() {
		ArrayList<Order> orders = new ArrayList<>();
		String query = "SELECT * FROM orders;";

		try (Connection connection = Connect.getInstance().getConnection()) {
			PreparedStatement prep = connection.prepareStatement(query);
			ResultSet resultSet = prep.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("orderId");
				int userId = resultSet.getInt("userId");
				String status = resultSet.getString("orderStatus");
				Date date = resultSet.getDate("orderDate");
				orders.add(new Order(id, userId, status, date));
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	public static Order getOrderByOrderId(int orderId) {
		Order order = null;
		String query = "SELECT * FROM `orders` WHERE orderId = ?;";

		try (Connection connection = Connect.getInstance().getConnection()) {
			PreparedStatement prep = connection.prepareStatement(query);
			prep.setInt(1, orderId);
			ResultSet resultSet = prep.executeQuery();

			if (resultSet.next()) {
				int orderUserId = resultSet.getInt("userId");
				String orderStatus = resultSet.getString("orderStatus");
				Date orderDate = resultSet.getDate("orderDate");
				order = new Order(orderId, orderUserId, orderStatus, orderDate);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}
	
	public static double getTotalByOrderId(int orderId)
	{
		int orderTotalPrice = 0;
		String query = "SELECT * FROM orderitem JOIN menuitem ON orderitem.menuItemId = menuitem.menuItemId WHERE orderitem.orderId = ?;";
		  
		try (Connection connection = Connect.getInstance().getConnection())
		{
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
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return orderTotalPrice;
	}

	
	public static void updateOrder(int orderId, ArrayList<OrderItem> orderItems, String orderStatus) {
		String statusQuery = "UPDATE orders SET orderStatus = ? WHERE orderId = ?;";
		try (Connection connection = Connect.getInstance().getConnection()) {
			PreparedStatement prep = connection.prepareStatement(statusQuery);
			prep.setString(1, orderStatus);
			prep.setInt(2, orderId);
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteOrder(int orderId) {
		String query = "DELETE FROM orders WHERE orderId = ?";

		try (Connection connection = Connect.getInstance().getConnection()) {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, orderId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean orderExists(int orderId) {
	    String query = "SELECT COUNT(*) FROM orders WHERE orderId = ?";
	    try (Connection connection = Connect.getInstance().getConnection();
	         PreparedStatement ps = connection.prepareStatement(query)) {

	        ps.setInt(1, orderId);

	        try (ResultSet resultSet = ps.executeQuery()) {
	            if (resultSet.next()) {
	                int count = resultSet.getInt(1);
	                return count > 0;
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
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

	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(double d) {
		this.orderTotal = d;
	}

	public static ArrayList<Order> getPendingOrdersByCustomerId(int customerId) {
		ArrayList<Order> pendingOrders = new ArrayList<>();
	    
		ArrayList<Order> allOrders = getOrdersByCustomerId(customerId);

	    for (Order order : allOrders) {
	        if ("Pending".equals(order.getOrderStatus())) {
	            pendingOrders.add(order);
	        }
	    }

	    return pendingOrders;
	}
	
	public static ArrayList<Order> getPreparedOrdersByCustomerId(int customerId) {
		ArrayList<Order> preparedOrders = new ArrayList<>();
	    
		ArrayList<Order> allOrders = getOrdersByCustomerId(customerId);

	    for (Order order : allOrders) {
	        if ("Prepared".equals(order.getOrderStatus())) {
	        	preparedOrders.add(order);
	        }
	    }

	    return preparedOrders;
	}


}
