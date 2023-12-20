package view;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import controller.MenuItemController;
import controller.OrderController;
import controller.OrderItemController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.User;

public class CustomerPanel extends Stage {

	private BorderPane borderPane = new BorderPane();
	private VBox root1 = new VBox(20);
	private VBox root2 = new VBox(20);
	private VBox root3 = new VBox(20);
	private Scene scene;
	private MenuBar menuBar = new MenuBar();

	public CustomerPanel(User user) {

		super(StageStyle.DECORATED);
		this.setTitle("Customer Dashboard");

		scene = new Scene(borderPane, 1400, 800);
		this.setScene(scene);

		Menu addOrderMenu = new Menu("Add Order");
		javafx.scene.control.MenuItem AddOrderMenuItem = new javafx.scene.control.MenuItem("Go To Add Order");
		addOrderMenu.getItems().addAll(AddOrderMenuItem);

		Menu viewOrderedMenu = new Menu("View Ordered (History)");
		javafx.scene.control.MenuItem viewOrderedMenuItem = new javafx.scene.control.MenuItem("Go to View Ordered");
		viewOrderedMenu.getItems().addAll(viewOrderedMenuItem);

		menuBar.getMenus().addAll(addOrderMenu);
		menuBar.getMenus().addAll(viewOrderedMenu);

		borderPane.setTop(menuBar);

		addOrder(user);

		AddOrderMenuItem.setOnAction(e -> {
			formClear();
			addOrder(user);

		});

		viewOrderedMenuItem.setOnAction(e -> {
			formClear();
			viewOrdered(user);
		});

		borderPane.setLeft(root1);
		root1.setAlignment(Pos.CENTER);
		root1.setPadding(new Insets(20));
		root1.setPrefHeight(getMaxHeight());
		root1.setStyle("-fx-background-color: lightgray;");

		borderPane.setCenter(root2);
		root2.setAlignment(Pos.CENTER);
		root2.setPadding(new Insets(20));

		borderPane.setRight(root3);
		root3.setAlignment(Pos.CENTER);
		root3.setPadding(new Insets(20));
		
		this.setOnCloseRequest(event -> {
			if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
				Authentication authenticationPanel = new Authentication();
				authenticationPanel.show();
			}
		});

	}

	// variabel nyimpen select dan form
	private TextField ItemId = new TextField();
	private TextField ItemName = new TextField();
	private TextField ItemDesc = new TextField();
	private TextField ItemPrice = new TextField();
	private TextField ItemQuantity = new TextField();

	// Tampilan Customer Untuk Add Order
	// Tabel Menu Order Item, Form buat Nambah Item, Tabel Cart Item yang udah
	// ditambahin
	TableView<OrderItem> createdCartTable = createCartTable();

	private void addOrder(User user) {
		// buat hilangin tampilan isi sebelumnya
		root1.getChildren().clear();
		root2.getChildren().clear();
		root3.getChildren().clear();

		// buat tampilan baru
		TableView<MenuItem> tableMenuItem = createMenuItemTable();
		tableMenuItem.setStyle("-fx-background-color: lightblue;");
		root1.getChildren().addAll(tableMenuItem);

		GridPane form = createOrderForm(tableMenuItem, user);
		Label totalLabel = new Label("Total Price: 0");
		root2.getChildren().addAll(form, createdCartTable, totalLabel);
		updateTotalLabel();
	}

	// Buat Table Menu Item
	private TableView<MenuItem> createMenuItemTable() {
		TableView<MenuItem> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<MenuItem, String> menuItemName = new TableColumn<>("Name");
		menuItemName.setCellValueFactory(new PropertyValueFactory<>("menuItemName"));

		TableColumn<MenuItem, String> menuItemDesc = new TableColumn<>("Desc");
		menuItemDesc.setCellValueFactory(new PropertyValueFactory<>("menuItemDescription"));

		TableColumn<MenuItem, String> menuItemPrice = new TableColumn<>("Price");
		menuItemPrice.setCellValueFactory(new PropertyValueFactory<>("menuItemPrice"));

		table.getColumns().add(menuItemName);
		table.getColumns().add(menuItemDesc);
		table.getColumns().add(menuItemPrice);

		menuItemName.setPrefWidth(100);
		menuItemName.setPrefWidth(100);
		menuItemName.setPrefWidth(100);

		table.setPrefHeight(1200);

		table.setMinHeight(700);
		table.setMinWidth(400);

		table.setItems(FXCollections.observableArrayList(MenuItemController.getAllMenuItems()));

		// biar bisa select data-data
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				ItemId.setText(newSelection.getMenuItemId() + "");
				ItemName.setText(newSelection.getMenuItemName());
				ItemDesc.setText(newSelection.getMenuItemDescription());
				ItemPrice.setText(String.valueOf(newSelection.getMenuItemPrice()));
				ItemQuantity.setText("1");
			}
		});

		return table;
	}

	// Buat tabel keranjang
	private TableView<OrderItem> createCartTable() {
		TableView<OrderItem> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Tambahkan kolom-kolom yang sesuai dengan atribut OrderItem
		TableColumn<OrderItem, String> itemName = new TableColumn<>("Item Name");
		itemName.setCellValueFactory(orderItem -> new SimpleStringProperty(orderItem.getValue().getMenuItemName()));
		itemName.setPrefWidth(150);

		TableColumn<OrderItem, Integer> itemQuantity = new TableColumn<>("Quantity");
		itemQuantity.setCellValueFactory(
				orderItem -> new SimpleIntegerProperty(orderItem.getValue().getQuantity()).asObject());
		itemQuantity.setPrefWidth(50);

		// New TableColumn for itemTotalPrice
		TableColumn<OrderItem, Double> itemTotalPrice = new TableColumn<>("Total Price");
		itemTotalPrice.setCellValueFactory(orderItem -> new SimpleDoubleProperty(
				orderItem.getValue().getQuantity() * orderItem.getValue().getMenuItem().getMenuItemPrice()).asObject());
		itemTotalPrice.setPrefWidth(100);

		table.getColumns().addAll(List.of(itemName, itemQuantity, itemTotalPrice));

		return table;
	}

	// Label Total Price di bawah Tabel Keranjang
	private void updateTotalLabel() {
		orderTotal = tempCart.stream()
				.mapToDouble(orderItem -> orderItem.getQuantity() * orderItem.getMenuItem().getMenuItemPrice()).sum();

		Label totalLabel = new Label("Total Price: " + orderTotal);
		root2.getChildren().set(root2.getChildren().size() - 1, totalLabel); // Update the last child (total label)
	}

	ArrayList<OrderItem> tempCart = new ArrayList<>();
	double orderTotal = 0;

	// Form buat nambahin Keranjang
	private GridPane createOrderForm(TableView<MenuItem> tableMenuItem, User user) {
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);

		Button addItemButton = new Button("Add Item");
		Button makeOrderButton = new Button("Make Order");
		Button resetOrderButton = new Button("Reset Order");

		form.add(new Label("Name:"), 0, 0);
		form.add(ItemName, 1, 0);
		ItemName.setDisable(true);
		form.add(new Label("Desc:"), 0, 1);
		form.add(ItemDesc, 1, 1);
		ItemDesc.setDisable(true);
		form.add(new Label("Price:"), 0, 2);
		form.add(ItemPrice, 1, 2);
		ItemPrice.setDisable(true);
		form.add(new Label("Quantity:"), 0, 3);
		form.add(ItemQuantity, 1, 3);
		form.add(addItemButton, 0, 4);
		form.add(makeOrderButton, 0, 6);
		form.add(resetOrderButton, 45, 6);

		// button tambahin item ke keranjang
		addItemButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MenuItem selectedMenuItem = tableMenuItem.getSelectionModel().getSelectedItem();
				
				int newQuantity = Integer.parseInt(ItemQuantity.getText());

				if (selectedMenuItem != null && !ItemQuantity.getText().equals("0") ) {
					
					if(newQuantity > 0) {
						// Check if the selected item is already in the cart
						OrderItem existingOrderItem = tempCart.stream().filter(
								orderItem -> orderItem.getMenuItem().getMenuItemId() == selectedMenuItem.getMenuItemId())
								.findFirst().orElse(null);

						if (existingOrderItem != null) {
							// If the item is already in the cart, update the quantity
							newQuantity = existingOrderItem.getQuantity() + Integer.parseInt(ItemQuantity.getText());
							existingOrderItem.setQuantity(newQuantity);

							createdCartTable.setItems(FXCollections.observableArrayList(tempCart));
							updateTotalLabel();
							showAlert("Add Order Item", "Add Quantity to Added Item");

							createdCartTable.refresh();
						} else {
							// If the item is not in the cart, add a new OrderItem
							OrderItem orderItem = new OrderItem(0, selectedMenuItem,
									Integer.parseInt(ItemQuantity.getText()));
							tempCart.add(orderItem);

							createdCartTable.setItems(FXCollections.observableArrayList(tempCart));
							updateTotalLabel();
							showAlert("Add Order Item", "Add New Item To Cart");
							createdCartTable.refresh();

						}
					}
					else if(newQuantity < 0) {
						showAlert("Add Order Item", "Item Quantity Must Be Positive");
					}
					
					formClear();
					tableMenuItem.getSelectionModel().clearSelection();

				}
				else if(ItemQuantity.getText().equals("0")) {
					showAlert("Add Menu Item", "Please Add Quantity");
				}

				else {
					showAlert("Add Menu Item", "Please Select Menu Item From The Table");
				}
			}
		});

		// button buat Order dari Tabel Keranjang
		makeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!tempCart.isEmpty()) {
					Date date = new Date(System.currentTimeMillis());
					int orderId = OrderController.createOrder(user, tempCart, date, orderTotal);

					for (OrderItem orderItem : tempCart) {
						OrderItemController.createOrderItem(orderId, orderItem.getMenuItem(), orderItem.getQuantity());

					}

					tempCart.clear();
					createdCartTable.setItems(FXCollections.observableArrayList(tempCart)); // Clear the table

					Label totalLabel = new Label("Total Price: 0");
					root2.getChildren().set(root2.getChildren().size() - 1, totalLabel);

					showAlert("Make Order", "Success Make Order");

					TableView<Order> tableOrdered = createOrderedTable(user);
					tableOrdered
							.setItems(FXCollections.observableArrayList(Order.getOrdersByCustomerId(user.getUserId())));

				} else {
					showAlert("Make Order", "There Is No Item, Please Add Item First");
				}
			}
		});

		// Reset Keranjang jika ingin mengcancel order
		resetOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				tempCart.clear();
				createdCartTable.setItems(FXCollections.observableArrayList(tempCart)); // Clear the table

				Label totalLabel = new Label("Total Price: 0.0");
				root2.getChildren().set(root2.getChildren().size() - 1, totalLabel);

				formClear();

				tableMenuItem.getSelectionModel().clearSelection();

				showAlert("Reset Cart", "Your Cart Is Clear Now");

			}
		});

		return form;
	}

	// =========================================================
	// Customer Panel untuk View Ordered / History Order

	// Berisi Tabel Order List,
	private void viewOrdered(User user) {
		// TODO Auto-generated method stub
		root1.getChildren().clear();
		root2.getChildren().clear();
		root3.getChildren().clear();
		TableView<Order> tableOrdered = createOrderedTable(user);

		// buat tampilan baru
		root1.getChildren().add(tableOrdered);

		// Menampilkan label petunjuk jika belum ada pesanan yang dipilih
		if (tableOrdered.getSelectionModel().isEmpty()) {
			Label selectOrderLabel = new Label("Select an order from the table to see order details.");
			root2.getChildren().add(selectOrderLabel);
		} else {
			// Jika ada pesanan yang dipilih, tampilkan detail pesanan
			selectedOrder = tableOrdered.getSelectionModel().getSelectedItem();
			showOrderDetails(selectedOrder, tableOrdered, user);
		}

	}

	private Order selectedOrder;

	private TableView<Order> createOrderedTable(User user) {

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

		table.setItems(FXCollections.observableArrayList(OrderController.getOrderByCustomerId(user.getUserId())));

		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectedOrder = newSelection;
				showOrderDetails(selectedOrder, table, user);
			}
		});

		return table;
	}

	private TableView<OrderItem> createOrdersByOrderIdTable(Order OrderId) {
		TableView<OrderItem> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Tambahkan kolom-kolom yang sesuai dengan atribut OrderItem
		TableColumn<OrderItem, String> itemName = new TableColumn<>("Item Name");
		itemName.setCellValueFactory(orderItem -> new SimpleStringProperty(orderItem.getValue().getMenuItemName()));
		itemName.setPrefWidth(100);

		TableColumn<OrderItem, String> itemDesc = new TableColumn<>("Item Desc");
		itemDesc.setCellValueFactory(orderItem -> new SimpleStringProperty(orderItem.getValue().getMenuItemDesc()));
		itemDesc.setPrefWidth(100);

		TableColumn<OrderItem, Integer> itemQuantity = new TableColumn<>("Quantity");
		itemQuantity.setCellValueFactory(
				orderItem -> new SimpleIntegerProperty(orderItem.getValue().getQuantity()).asObject());
		itemQuantity.setPrefWidth(50);

		// New TableColumn for itemTotalPrice
		TableColumn<OrderItem, Double> itemTotalPrice = new TableColumn<>("Total Price");
		itemTotalPrice.setCellValueFactory(orderItem -> new SimpleDoubleProperty(
				orderItem.getValue().getQuantity() * orderItem.getValue().getMenuItem().getMenuItemPrice()).asObject());
		itemTotalPrice.setPrefWidth(50);

		ArrayList<OrderItem> orderItem = OrderItemController.getAllOrderItemsByOrderId(selectedOrder.getOrderId());
// 	   System.out.println("Number of order items: " + orderItem.size());
		table.setItems(FXCollections.observableArrayList(orderItem));

		table.getColumns().addAll(List.of(itemName, itemDesc, itemQuantity, itemTotalPrice));

		return table;
	}

	Label totalUpdateLabel;
	Label orderDetailLabel;
	Label chooseItemLabel;

	private void showOrderDetails(Order selectedOrder, TableView<Order> tableOrdered, User user) {
		TableView<OrderItem> orderItemTable = null;
		orderDetailLabel = new Label("Order Details");
		chooseItemLabel = new Label("Choose Menu Item To Add Item");

		root2.getChildren().clear(); //
		root3.getChildren().clear(); //
		root2.getChildren().add(orderDetailLabel);

		if (selectedOrder != null) {

			orderItemTable = createOrdersByOrderIdTable(selectedOrder);
			TableView<MenuItem> tableMenuItem = createMenuItemTable();
			if (selectedOrder.getOrderStatus().equals("Pending")) {

				root2.getChildren().add(orderItemTable);

				double totalOrderPrice = selectedOrder.getOrderTotal();
				totalUpdateLabel = new Label("Total Price: " + totalOrderPrice);
				root2.getChildren().add(totalUpdateLabel);

				root2.getChildren()
						.add(createUpdateOrderForm(selectedOrder, orderItemTable, tableMenuItem, tableOrdered, user));

				tableMenuItem.setStyle("-fx-background-color: lightblue;");
				root3.getChildren().add(chooseItemLabel);
				root3.getChildren().add(tableMenuItem);

			} else if (selectedOrder.getOrderStatus().equals("Served")
					|| selectedOrder.getOrderStatus().equals("Prepared")
					|| selectedOrder.getOrderStatus().equals("Paid")) {
				root2.getChildren().add(createOrdersByOrderIdTable(selectedOrder));

				Label statusLabel;
				double totalOrderPrice = selectedOrder.getOrderTotal();
				totalUpdateLabel = new Label("Total Price: " + totalOrderPrice);

				root2.getChildren().add(totalUpdateLabel);
				if (selectedOrder.getOrderStatus().equals("Served")) {
					statusLabel = new Label("Order Served, Can Only See Order Item Details");
					root2.getChildren().add(statusLabel);
				} else if (selectedOrder.getOrderStatus().equals("Prepared")) {
					statusLabel = new Label("Order Prepared, Can Only See Order Item Details");
					root2.getChildren().add(statusLabel);
				} else if (selectedOrder.getOrderStatus().equals("Paid")) {
					statusLabel = new Label("Order Already Paid, Can Only See Order Item Details");
					root2.getChildren().add(statusLabel);
				}

			}

		}

	}

	// variabel nyimpen select dan form untuk update product
	// variabel nyimpen select dan form untuk update product
	private TextField UpdateItemName = new TextField();
	private TextField UpdateItemDesc = new TextField();
	private TextField UpdateItemPrice = new TextField();
	private TextField UpdateItemQuantity = new TextField();

	private GridPane createUpdateOrderForm(Order selectedOrder, TableView<OrderItem> orderItemTable,
			TableView<MenuItem> tableMenuItem, TableView<Order> tableOrdered, User user) {
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);

		Button updateItemButton = new Button("Update Order");

		form.add(new Label("Name:"), 0, 0);
		form.add(UpdateItemName, 1, 0);
		UpdateItemName.setDisable(true);

		form.add(new Label("Desc:"), 0, 1);
		form.add(UpdateItemDesc, 1, 1);
		UpdateItemDesc.setDisable(true);

		form.add(new Label("Price:"), 0, 2);
		form.add(UpdateItemPrice, 1, 2);
		UpdateItemPrice.setDisable(true);

		form.add(new Label("Quantity:"), 0, 3);
		form.add(UpdateItemQuantity, 1, 3);

		form.add(updateItemButton, 0, 4);

		orderItemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				// Populate the form fields with the data from the selected OrderItem
				UpdateItemName.setText(newSelection.getMenuItemName());
				UpdateItemPrice.setText(String.valueOf(newSelection.getMenuItem().getMenuItemPrice()));
				UpdateItemDesc.setText(newSelection.getMenuItemDesc());
				UpdateItemQuantity.setText(String.valueOf(newSelection.getQuantity()));

			}

		});

		tableMenuItem.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				UpdateItemName.setText(newSelection.getMenuItemId() + "");
				UpdateItemName.setText(newSelection.getMenuItemName());
				UpdateItemDesc.setText(newSelection.getMenuItemDescription());
				UpdateItemPrice.setText(String.valueOf(newSelection.getMenuItemPrice()));
				UpdateItemQuantity.setText("1");
			}
		});

		// button untuk update item di keranjang
		updateItemButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				OrderItem selectedOrderItem = orderItemTable.getSelectionModel().getSelectedItem();
				MenuItem selectedMenuItem = tableMenuItem.getSelectionModel().getSelectedItem();

				if (selectedOrderItem != null) {
					int newQuantity = Integer.parseInt(UpdateItemQuantity.getText());
//					System.out.println(selectedOrderItem.getOrderId());
//					System.out.println(selectedOrderItem.getMenuItemId());

					if (newQuantity == 0) {
						// If the new quantity is 0, delete the order item
						OrderItemController.deleteOrderItem(selectedOrderItem.getOrderId(),
								selectedOrderItem.getMenuItemId());

						// Refresh the orderItemTable
						orderItemTable.getItems().remove(selectedOrderItem);

						totalUpdateLabel.setText(
								"Total Price: " + Order.getTotalByOrderId(selectedOrderItem.getOrderId()));

						tableOrdered.setItems(FXCollections
								.observableArrayList(OrderController.getOrderByCustomerId(user.getUserId())));

						showAlert("Remove Order Item", "Selected Order Item Has Been Removed");

					} else if(newQuantity > 0){
						// Update the selected order item with the new quantity
						selectedOrderItem.setQuantity(newQuantity);
						OrderItem.updateOrderItem(selectedOrderItem.getOrderId(),
								selectedOrderItem.getMenuItem(), newQuantity);

						// Refresh the orderItemTable
						orderItemTable.refresh();

						totalUpdateLabel.setText(
								"Total Price: " + Order.getTotalByOrderId(selectedOrderItem.getOrderId()));

						tableOrdered.setItems(FXCollections
								.observableArrayList(OrderController.getOrderByCustomerId(user.getUserId())));
						showAlert("Update Order Item", "Success Update Selected Order Item's Quantity");
					}
					else {
						showAlert("Update Order Item", "Quantity Must Be Positive");
					}

					// Refresh the orderItemTable
					orderItemTable.refresh();
					
					// Update the total label
					totalUpdateLabel
							.setText("Total Price: " + Order.getTotalByOrderId(selectedOrderItem.getOrderId()));
					formClear();

					orderItemTable.getSelectionModel().clearSelection();
					tableMenuItem.getSelectionModel().clearSelection();
				} else if (selectedMenuItem != null ) {
					// Cek apakah item sudah ada dalam orderItemList
					boolean itemExists = false;
					int newQuantity = Integer.parseInt(UpdateItemQuantity.getText());
					for (OrderItem existingOrderItem : orderItemTable.getItems()) {
						if (existingOrderItem.getMenuItemId() == selectedMenuItem.getMenuItemId()) {
							// Jika sudah ada, update quantity-nya
					
							
							//check quantity 
							if(OrderItemController.updateOrderItem(existingOrderItem.getOrderId(),
									existingOrderItem.getMenuItem(), newQuantity)) {
								existingOrderItem.setQuantity(newQuantity);
								showAlert("Update Order Item", "Success Update Selected Order Item's Quantity");

							}else {
								showAlert("Update Order Item Error","Failed quantity must be more than 0");
							}
//							OrderItem.updateOrderItem(existingOrderItem.getOrderId(),
//									existingOrderItem.getMenuItem(), existingOrderItem.getQuantity());

							// Refresh orderItemTable
							orderItemTable.refresh();

							// Refresh total label
							totalUpdateLabel.setText(
									"Total Price: " + Order.getTotalByOrderId(existingOrderItem.getOrderId()));

							// Refresh ordered table
//                            refreshOrderedTable();
							
							itemExists = true;
							break;
						}
					}

					// Jika item belum ada dalam orderItemList, tambahkan item baru ke dalam order
					if (!itemExists && selectedMenuItem != null) {
			

						OrderItem newOrderItem = new OrderItem(selectedOrder.getOrderId(), selectedMenuItem,
								newQuantity);
						
						if(OrderItemController.createOrderItem(selectedOrder.getOrderId(), selectedMenuItem, newQuantity)) {
							
							showAlert("Update Order Item", "Success Add New Order Item");
							// Refresh orderItemTable
							orderItemTable.getItems().add(newOrderItem);

						}else {
							showAlert("Update Order Item Failed", "Failed: Quantity must be more than 0");
						}
//						OrderItem.createOrderItem(selectedOrder.getOrderId(), selectedMenuItem, newQuantity);

						

						// Refresh total label
						totalUpdateLabel
								.setText("Total Price: " + Order.getTotalByOrderId(selectedOrder.getOrderId()));

						// Refresh ordered table
						tableOrdered.setItems(
								FXCollections.observableArrayList(Order.getOrdersByCustomerId(user.getUserId())));

//						showAlert("Update Order Item", "Success Add New Order Item");
					}

					// Clear form fields dan selection
					tableOrdered
							.setItems(FXCollections.observableArrayList(Order.getOrdersByCustomerId(user.getUserId())));
					formClear();
					orderItemTable.refresh();
					orderItemTable.getSelectionModel().clearSelection();
					tableMenuItem.getSelectionModel().clearSelection();
					
				} else {
					showAlert("Update Order Item", "Please Select An Order Item or Menu Item To Update Order");
				}
			}
		});
		return form;
	}

	public void formClear() {

		ItemName.clear();
		ItemPrice.clear();
		ItemQuantity.clear();
		ItemDesc.clear();

		UpdateItemName.clear();
		UpdateItemPrice.clear();
		UpdateItemQuantity.clear();
		UpdateItemDesc.clear();
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
