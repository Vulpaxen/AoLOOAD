package controller;

import java.sql.Date;
import java.util.ArrayList;

import model.Order;
import model.OrderItem;
import model.User;

public class OrderController {
	public OrderController(){
		// TODO Auto-generated constructor stub
	}
	
	public static String createOrder(User orderUser, ArrayList<OrderItem> orderItems, Date orderDate){
		if(orderItems.size() == 0) {
			return "Order Items Empty";
		}
		else {
			return "Success Create An Order";
		}
		
	}
	
	public static void updateOrder(int orderId, ArrayList<OrderItem> orderItems, String orderStatus){
		Order.updateOrder(orderId, orderItems, orderStatus);
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
