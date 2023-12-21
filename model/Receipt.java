package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Receipt {
	private int receiptId;
	private Order receiptOrder;
	private int receiptOrderId;
	private int receiptPaymentAmount;
	private Date receiptPaymentDate;
	private String receiptPaymentType;

	public Receipt(int receiptId, int receiptOrderId, int receiptPaymentAmount, Date receiptPaymentDate,
			String receiptPaymentType) {
		super();
		this.receiptId = receiptId;
		this.receiptOrderId = receiptOrderId;
		this.receiptPaymentAmount = receiptPaymentAmount;
		this.receiptPaymentDate = receiptPaymentDate;
		this.receiptPaymentType = receiptPaymentType;
	}

	public static void createReceipt(int orderId, String receiptPaymentType, Date receiptPaymentDate,
			double receiptPaymentAmount) {
		String query = "INSERT INTO receipt (orderId, receiptPaymentType, receiptPaymentDate, receiptPaymentAmount) VALUES (?, ?, ?, ?)";
		try (Connection connection = Connect.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, orderId);
			ps.setString(2, receiptPaymentType);
			ps.setDate(3, receiptPaymentDate);
			ps.setDouble(4, receiptPaymentAmount);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteReceipt(Order orderId) {
		String query = "DELETE FROM receipt WHERE orderId = ?";
		try (Connection connection = Connect.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, orderId.getOrderId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Receipt getReceiptByid(int receiptId) {
		Receipt receipt = null;

		try (Connection connection = Connect.getInstance().getConnection()) {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM receipt WHERE receiptId = ?;");
			ps.setInt(1, receiptId);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				int id = resultSet.getInt("receiptId");
				int orderId = resultSet.getInt("orderId");
				int paymentAmount = resultSet.getInt("receiptPaymentAmount");
				Date paymentDate = resultSet.getDate("receiptPaymentDate");
				String paymentType = resultSet.getString("receiptPaymentType");

				receipt = new Receipt(id, orderId, paymentAmount, paymentDate, paymentType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return receipt;
	}

	public static ArrayList<Receipt> getAllReceipts() {
		ArrayList<Receipt> receiptList = new ArrayList<>();
		String query = "SELECT * FROM receipt JOIN orders ON receipt.orderId = orders.orderId";
		try (Connection connection = Connect.getInstance().getConnection()) {
			PreparedStatement prep = connection.prepareStatement(query);
//            ResultSet resultSet = prep.executeQuery();

			try (ResultSet resultSet = prep.executeQuery()) {
				while (resultSet.next()) {
					int id = resultSet.getInt("receiptId");
					int orderId = resultSet.getInt("orderId");
					int paymentAmount = resultSet.getInt("receiptPaymentAmount");
					Date paymentDate = resultSet.getDate("receiptPaymentDate");
					String paymentType = resultSet.getString("receiptPaymentType");

					Receipt receipt = new Receipt(id, orderId, paymentAmount, paymentDate, paymentType);
					receiptList.add(receipt);
				}
			}

//            resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return receiptList;

	}

	public int getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}

	public Order getReceiptOrder() {
		return receiptOrder;
	}

	public void setReceiptOrder(Order receiptOrder) {
		this.receiptOrder = receiptOrder;
	}

	public int getReceiptPaymentAmount() {
		return receiptPaymentAmount;
	}

	public void setReceiptPaymentAmount(int receiptPaymentAmount) {
		this.receiptPaymentAmount = receiptPaymentAmount;
	}

	public Date getReceiptPaymentDate() {
		return receiptPaymentDate;
	}

	public void setReceiptPaymentDate(Date receiptPaymentDate) {
		this.receiptPaymentDate = receiptPaymentDate;
	}

	public String getReceiptPaymentType() {
		return receiptPaymentType;
	}

	public void setReceiptPaymentType(String receiptPaymentType) {
		this.receiptPaymentType = receiptPaymentType;
	}

	public int getReceiptOrderId() {
		return receiptOrderId;
	}

	public void setReceiptOrderId(int receiptOrderId) {
		this.receiptOrderId = receiptOrderId;
	}
}
