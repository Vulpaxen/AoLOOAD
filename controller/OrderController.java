package controller;

import java.sql.Date;
import java.util.ArrayList;

import model.Order;
import model.OrderItem;
import model.User;

public class OrderController {
	
	public static int createOrder(User orderUser, ArrayList<OrderItem> orderItem, Date orderDate, double orderTotal){
		int orderId = Order.createOrder(orderUser, orderItem, orderDate, orderTotal);
		return orderId;
	}
	
	public static String updateOrder(int orderId, ArrayList<OrderItem> orderItems, String orderStatus){
	    if (!Order.orderExists(orderId) || orderId == 0) {
	        return "Order ID does not exist in the database";
	    } 
	    else {
	        Order.updateOrder(orderId, orderItems, orderStatus);
	        return "Order Items Updated Successfully!";
	    }
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
