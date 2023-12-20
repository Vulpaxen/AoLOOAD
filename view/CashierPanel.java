package view;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import controller.OrderController;
import controller.OrderItemController;
import controller.ReceiptController;
import controller.UserController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Order;
import model.OrderItem;
import model.Receipt;
import model.User;

public class CashierPanel extends Stage{
	
	private BorderPane borderPane = new BorderPane();
	private VBox root1 = new VBox(20);
	private VBox root2 = new VBox(20);
	private MenuBar menuBar = new MenuBar();
	private Scene scene;
	Label totalPrice;
	
	public CashierPanel() {
		super(StageStyle.DECORATED);
		this.setTitle("Cashier Dashboard");
		
		scene = new Scene(borderPane, 1000, 800);
		this.setScene(scene);
		
		viewOrderCashier();
		Menu orderMenu = new Menu("View Order");
		MenuItem orderMenuItem = new MenuItem("View Order");
		Menu receiptMenu = new Menu("View Receipt");
		MenuItem receiptMenuItem = new MenuItem("View Receipt");
		
		orderMenu.getItems().add(orderMenuItem);
		receiptMenu.getItems().add(receiptMenuItem);
		
		menuBar.getMenus().addAll(orderMenu, receiptMenu);
		
		// Menu ada 2
		// Order Item yang akan di process
		orderMenuItem.setOnAction(e -> {
			viewOrderCashier();
		});
		
		// Receipt View dan Receipt Detail View
		receiptMenuItem.setOnAction(e -> {
			viewReceiptCashier();
		});
		
		borderPane.setTop(menuBar);
		borderPane.setLeft(root1);
		root1.setAlignment(Pos.CENTER);
		root1.setPadding(new Insets(20));
		root1.setPrefHeight(getMaxHeight());
		root1.setStyle("-fx-background-color: lightgray;");

		borderPane.setCenter(root2);
		root2.setAlignment(Pos.CENTER);
		root2.setPadding(new Insets(20));
	}
	
	//Table untuk served Orders
	TableView<Order> servedOrders = createServedTable();
	
	// View Order untuk cashier
	private void viewOrderCashier() {
		root1.getChildren().clear();
		root2.getChildren().clear();
		
		root1.getChildren().add(servedOrders);
		if (servedOrders.getSelectionModel().isEmpty()) {
			Label selectOrderLabel = new Label("Select an order from the table to see order details.");
			root2.getChildren().add(selectOrderLabel);
		} else {
			selectedOrder = servedOrders.getSelectionModel().getSelectedItem();
			showOrderDetails(selectedOrder);
		}
	}

	// View Order Detail
	// Bisa Process
	private Order selectedOrder;
	private TableView<Order> createServedTable() {
		// TODO Auto-generated method stunt
		TableView<Order> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<Order, Integer> orderId = new TableColumn<>("Order ID");
		orderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));

		TableColumn<Order, String> orderStatus = new TableColumn<>("Status");
		orderStatus.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

		TableColumn<Order, Date> orderDate = new TableColumn<>("Date");
		orderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

		TableColumn<Order, Double> orderTotal = new TableColumn<>("Total Order Price");
		orderTotal.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));

		table.getColumns().add(orderId);
		table.getColumns().add(orderStatus);
		table.getColumns().add(orderDate);
		table.getColumns().add(orderTotal);

		table.setPrefHeight(1200);

		table.setMinHeight(700);
		table.setMinWidth(400);

		table.setItems(FXCollections.observableArrayList(Order.getServedOrders()));

		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectedOrder = newSelection;
				showOrderDetails(selectedOrder);
			}
		});

		return table;
	}

	// Untuk Show details Order
	private void showOrderDetails(Order order) {
		TableView<OrderItem> orderItemTable = null;
		root2.getChildren().clear(); //
		if (order != null) {
			orderItemTable = createOrdersByOrderIdTable(order);
			root2.getChildren().add(orderItemTable);

			double totalOrderPrice = order.getOrderTotal();
			totalPrice = new Label("Total Price: " + totalOrderPrice);
			root2.getChildren().add(totalPrice);

			root2.getChildren().add(createProcessOrderForm(orderItemTable));
		}
	}

	// Table order Detail
	private TableView<OrderItem> createOrdersByOrderIdTable(Order order) {
		TableView<OrderItem> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<OrderItem, String> itemName = new TableColumn<>("Item Name");
		itemName.setCellValueFactory(orderItem -> new SimpleStringProperty(orderItem.getValue().getMenuItemName()));
		itemName.setPrefWidth(150);

		TableColumn<OrderItem, Integer> itemQuantity = new TableColumn<>("Quantity");
		itemQuantity.setCellValueFactory(
				orderItem -> new SimpleIntegerProperty(orderItem.getValue().getQuantity()).asObject());
		itemQuantity.setPrefWidth(50);

		TableColumn<OrderItem, Double> itemTotalPrice = new TableColumn<>("Total Price");
		itemTotalPrice.setCellValueFactory(orderItem -> new SimpleDoubleProperty(
				orderItem.getValue().getQuantity() * orderItem.getValue().getMenuItem().getMenuItemPrice()).asObject());
		itemTotalPrice.setPrefWidth(100);

		ArrayList<OrderItem> orderItem = OrderItemController.getAllOrderItemsByOrderId(selectedOrder.getOrderId());

		table.setItems(FXCollections.observableArrayList(orderItem));

		table.getColumns().addAll(List.of(itemName, itemQuantity, itemTotalPrice));

		return table;
	}

	// Process Form untuk process order yang sudah di served
	private GridPane createProcessOrderForm(TableView<OrderItem> orderItemTable) {
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);

		Button prepareOrderButton = new Button("Process Order");
		TextField paymentType = new TextField();
		TextField paymentAmount = new TextField();
		
		paymentType.setPromptText("Cash / Debit / Credit");
		paymentAmount.setPromptText("Payment Amount");
		
		form.add(new Label("Payment Type:"), 0, 0);
		form.add(paymentType, 1, 0);
		form.add(new Label("Payment amount:"), 0, 1);
		form.add(paymentAmount, 1, 1);
		form.add(prepareOrderButton, 0, 2);
		
		prepareOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				if (selectedOrder != null) {
					//Check paymenttype
					try {
						String payment = paymentType.getText();
						int amount = Integer.parseInt(paymentAmount.getText());
						
						int totalPrice = 0;
						if(selectedOrder.getOrderItem()!= null) {
							for(OrderItem oi:selectedOrder.getOrderItem()) {
								totalPrice += (oi.getMenuItem().getMenuItemPrice() * oi.getQuantity());

							}
						}
						
						ArrayList<OrderItem> orderItems = OrderItemController
								.getAllOrderItemsByOrderId(selectedOrder.getOrderId());
						if(amount < totalPrice) {
							showAlert("insufficient payment","Please input more than payment");
						}else if(payment.equals("Cash") || payment.equals("Debit") || payment.equals("Credit")) {
							
							OrderController.updateOrder(selectedOrder.getOrderId(), orderItems, "Paid");
							refreshOrderedTable();
							showAlert("Order Paid", "Selected order has been paid.");
							
							//TODO: masukin ke receipt
							
							Date date = new Date(System.currentTimeMillis());
							ReceiptController.createReceipt(selectedOrder.getOrderId(), payment, date, totalPrice);
						}else {
							showAlert("Payment Type Invalid", "Please select either Cash/Credit/Debit");
						}
						
						
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
				} else {
					showAlert("No Order Selected", "Please select a pending order to prepare.");
				}
				viewOrderCashier();
			}
			
			
		});
		return form;
	}
	
	// Refresh Table
	private void refreshOrderedTable() {
		servedOrders.setItems(FXCollections.observableArrayList(Order.getServedOrders()));
	}
	
	
	//Receipt
	private TableView<Receipt> tableReceipt = createReceiptTable();
	private TableView<OrderItem> tableDetail;
	Receipt selectedReceipt;
	
	// View receipt semua untuk cashier
	private void viewReceiptCashier() {
		root1.getChildren().clear();
		root2.getChildren().clear();
		
		root1.getChildren().add(tableReceipt);
		if (tableReceipt.getSelectionModel().isEmpty()) {
			Label selectOrderLabel = new Label("Select an receipt from the table to see receipt details.");
			root2.getChildren().add(selectOrderLabel);
		} else {
			selectedReceipt = tableReceipt.getSelectionModel().getSelectedItem();
			showReceiptDetails(selectedReceipt);
		}
	}
	
	// Table untuk ambil receipt
	private TableView<Receipt> createReceiptTable() {
		tableReceipt = new TableView<>();
		tableReceipt.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		ObservableList<Receipt> receiptData = FXCollections.observableArrayList(Receipt.getAllReceipts());
		
		TableColumn<Receipt, Integer> receiptId = new TableColumn<>("Order ID");
		receiptId.setCellValueFactory(new PropertyValueFactory<>("receiptId"));

		TableColumn<Receipt, Integer> orderId = new TableColumn<>("Order ID");
		orderId.setCellValueFactory(new PropertyValueFactory<>("receiptOrderId"));

		TableColumn<Receipt, String> paymentType = new TableColumn<>("Payment Type");
		paymentType.setCellValueFactory(new PropertyValueFactory<>("receiptPaymentType"));

		TableColumn<Receipt, Integer> paymentAmount = new TableColumn<>("Payment Amount");
		paymentAmount.setCellValueFactory(new PropertyValueFactory<>("receiptPaymentAmount"));
		
		TableColumn<Receipt, Date> receiptDate = new TableColumn<>("Payment Date");
		receiptDate.setCellValueFactory(new PropertyValueFactory<>("receiptPaymentDate"));
		
		tableReceipt.getColumns().addAll(List.of(receiptId,orderId,paymentType,paymentAmount,receiptDate));
		
		tableReceipt.setPrefHeight(1200);

		tableReceipt.setMinHeight(800);
		tableReceipt.setMinWidth(400);
		
		tableReceipt.setItems(receiptData);
		
		tableReceipt.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectedReceipt = newSelection;
				showReceiptDetails(selectedReceipt);
			}
		});
		
		
		return tableReceipt;
	}
	
	//Receipt Detail
	Label orderItemLbl;
	Label userNameLbl;
	
	// Memiliki detail untuk setiap receipt yang dipilih
	// Nama Customer
	private void showReceiptDetails(Receipt receipt) {
		root2.getChildren().clear();

		orderItemLbl = new Label("Receipt Detail");
		root2.getChildren().add(orderItemLbl);

		if(selectedReceipt != null) {
			tableDetail = createDetailTable();
			
			root2.getChildren().add(tableDetail);
			
			Order order = OrderController.getOrderByOrderId(selectedReceipt.getReceiptOrderId());
			System.out.println(order.getOrderId());
            totalPrice = new Label("Total Paid: " + selectedReceipt.getReceiptPaymentAmount());
            
            root2.getChildren().add(totalPrice);
        	tableDetail.setStyle("-fx-background-color: lightblue;");

        	User user = UserController.getUserById(order.getOrderUserId());
        	
        	userNameLbl = new Label("Customer Name : "+user.getUserName());
        	root2.getChildren().add(userNameLbl);
		}
	}
	
	// Mengambil Receipt Detail yaitu OrderItem dari OrderId.
	private TableView<OrderItem> createDetailTable() {
		TableView<OrderItem> table = new TableView<>();
     	table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<OrderItem, String> itemName = new TableColumn<>("Item Name");
 	    itemName.setCellValueFactory(orderItem -> new SimpleStringProperty(orderItem.getValue().getMenuItemName()));
 	    
 	    TableColumn<OrderItem, String> itemDesc = new TableColumn<>("Item Desc");
	    itemDesc.setCellValueFactory(orderItem -> new SimpleStringProperty(orderItem.getValue().getMenuItemDesc()));

 	    TableColumn<OrderItem, Integer> itemQuantity = new TableColumn<>("Quantity");
 	    itemQuantity.setCellValueFactory(orderItem -> new SimpleIntegerProperty(orderItem.getValue().getQuantity()).asObject());

 	    // New TableColumn for itemTotalPrice
 	    TableColumn<OrderItem, Double> itemTotalPrice = new TableColumn<>("Total Price");
 	    itemTotalPrice.setCellValueFactory(orderItem -> new SimpleDoubleProperty(orderItem.getValue().getQuantity() * orderItem.getValue().getMenuItem().getMenuItemPrice()).asObject());
 	    
 	   ArrayList<OrderItem> receiptData = OrderItemController.getAllOrderItemsByOrderId(selectedReceipt.getReceiptOrderId());
 	   table.setItems(FXCollections.observableArrayList(receiptData));
 	   
 	   table.getColumns().addAll(List.of(itemName, itemDesc, itemQuantity,  itemTotalPrice));
 	   
		return table;
	}
	
	//Alert
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
