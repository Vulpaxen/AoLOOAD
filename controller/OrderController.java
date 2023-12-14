package controller;

import java.sql.Date;
import java.util.ArrayList;

import model.Order;
import model.OrderItem;
import model.User;

public class OrderController {
	
	public static void createOrder(User orderUser, ArrayList<OrderItem> orderItems, Date orderDate){
		Order.createOrder(orderUser, orderItems, orderDate);
	}
	
	public static String updateOrder(int orderId, ArrayList<OrderItem> orderItems, String orderStatus){
		if(orderItems.size() == 0) {
			return "Order Items Empty";
		}
		else {
			Order.updateOrder(orderId, orderItems, orderStatus);
		}
		
		return "Order Items Updated Sucessfully!";
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
		return Order.getOrderById(orderId);
	}


}
