package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Connect;
import model.Order;
import model.OrderItem;
import model.Receipt;

public class ReceiptController {
	
	public static void createReceipt(Order order, String receiptPaymentType, int receiptPaymentAmount, Date receiptPaymentDate) {
		Receipt.createReceipt(order, receiptPaymentType, receiptPaymentAmount, receiptPaymentDate);
	}
	
	public static void deleteReceipt(Order orderId) {
        Receipt.deleteReceipt(orderId);
    }
	
	public static Receipt getReceiptByid(int receiptId) {
		return Receipt.getReceiptByid(receiptId);
	}
	
	public static ArrayList<Receipt> getAllReceipts() {
        return Receipt.getAllReceipts();
    }
}
