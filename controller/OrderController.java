package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Connect;
import model.Order;
import model.OrderItem;
import model.User;

public class OrderController {
	
	public static int createOrder(User orderUser, ArrayList<OrderItem> orderItem, Date orderDate, double orderTotal){
		int orderId = Order.createOrder(orderUser, orderItem, orderDate, orderTotal);
		return orderId;
	}
	
	public static String updateOrder(int orderId, ArrayList<OrderItem> orderItems, String orderStatus){
	    if (!orderExists(orderId) || orderId == 0) {
	        return "Order ID does not exist in the database";
	    } 
	    else {
	        Order.updateOrder(orderId, orderItems, orderStatus);
	        return "Order Items Updated Successfully!";
	    }
	}
	
	private static boolean orderExists(int orderId) {
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
	
	public static ArrayList<Order> getOrderByCustomerId(int customerId){
		return Order.getOrdersByCustomerId(customerId);
	}
	
	public static void deleteOrder(int orderId){
		Order.deleteOrder(orderId);
	}
	
	public static ArrayList<Order> getAllOrders(){
		return Order.getAllOrders();
	}
	
	public static Order getOrderByOrderId(int orderId){
		return Order.getOrderByOrderId(orderId);
	}


}
