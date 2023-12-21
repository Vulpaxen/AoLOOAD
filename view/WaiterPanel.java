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

public class WaiterPanel extends Stage {
	private Order selectedOrder;

	Label totalUpdateLabel;
	Label orderDetailLabel;
	Label chooseItemLabel;

	private BorderPane borderPane = new BorderPane();
	private VBox root1 = new VBox(20);
	private VBox root2 = new VBox(20);
	private VBox root3 = new VBox(20);
	private Scene scene;

	// textfield yg nantinya diisi valuenya dari database
	private TextField updateItemQuantity = new TextField();
	private TextField updateItemName = new TextField();
	private TextField updateItemDesc = new TextField();
	private TextField updateItemPrice = new TextField();

	private TextField itemId = new TextField();
	private TextField itemName = new TextField();
	private TextField itemDesc = new TextField();
	private TextField itemPrice = new TextField();
	private TextField itemQuantity = new TextField();

	// inisiasi scene ui
	public WaiterPanel(User user) {

		super(StageStyle.DECORATED);
		this.setTitle("Waiter Dashboard");

		scene = new Scene(borderPane, 1450, 800);
		this.setScene(scene);

		// set ui default ke viewordered
		viewOrdered(user);

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

	// untuk show alert
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// untuk clear formnya
	public void formClear() {

		itemName.clear();
		itemPrice.clear();
		itemQuantity.clear();
		itemDesc.clear();

		updateItemName.clear();
		updateItemPrice.clear();
		updateItemQuantity.clear();
		updateItemDesc.clear();
	}

	// untuk menambahkan sisi kiri stage dgn order table berisi prepared orders
	private void viewOrdered(User user) {
		// TODO Auto-generated method stub
		root1.getChildren().clear();
		root2.getChildren().clear();
		TableView<Order> preparedOrders = createOrderedTable(user);

		root1.getChildren().add(preparedOrders);

		// kalau tidak ada yg diselect, tidak bisa show details
		if (preparedOrders.getSelectionModel().isEmpty()) {
			Label selectOrderLabel = new Label("Select an order from the table to see order details.");
			root2.getChildren().add(selectOrderLabel);
		} else {
			selectedOrder = preparedOrders.getSelectionModel().getSelectedItem();
			showOrderDetails(selectedOrder, preparedOrders, user);
		}
	}

	// Untuk membuat tabel yang muncul saat order diklik berdasarkan order id mana
	// yang diselect
	private TableView<OrderItem> createOrdersByOrderIdTable(Order OrderId) {
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

		// tabel diisi kolom2nya dgn yg sudah dibuat diatas
		table.getColumns().addAll(List.of(itemName, itemQuantity, itemTotalPrice));

		return table;

	}

	// membuat tabel list of order yang ada di sebelah kiri stage
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

		TableColumn<Order, Double> orderTotal = new TableColumn<>("Total Price");
		orderTotal.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));

		// mengisi kolom2 di tabel dengan yg sudah dibuat diatas
		table.getColumns().add(orderId);
		table.getColumns().add(orderStatus);
		table.getColumns().add(orderDate);
		table.getColumns().add(orderTotal);

		table.setPrefHeight(1200);

		table.setMinHeight(700);
		table.setMinWidth(400);

		table.setItems(FXCollections.observableArrayList(OrderController.getAllPreparedOrders()));

		// untuk get selected order dari table
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectedOrder = newSelection;
				showOrderDetails(selectedOrder, table, user);
			}
		});

		return table;
	}

	// untuk membuat table yang ada di sebelah kanan kalau mau add order
	private TableView<MenuItem> createMenuItemTable() {
		TableView<MenuItem> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<MenuItem, String> menuItemName = new TableColumn<>("Name");
		menuItemName.setCellValueFactory(new PropertyValueFactory<>("menuItemName"));

		TableColumn<MenuItem, String> menuItemDesc = new TableColumn<>("Description");
		menuItemDesc.setCellValueFactory(new PropertyValueFactory<>("menuItemDescription"));

		TableColumn<MenuItem, String> menuItemPrice = new TableColumn<>("Price");
		menuItemPrice.setCellValueFactory(new PropertyValueFactory<>("menuItemPrice"));

		table.getColumns().add(menuItemName);
		table.getColumns().add(menuItemDesc);
		table.getColumns().add(menuItemPrice);

		menuItemName.setPrefWidth(150);
		menuItemDesc.setPrefWidth(300);
		menuItemPrice.setPrefWidth(100);

		table.setPrefHeight(1200);

		table.setMinHeight(700);
		table.setMinWidth(400);

		table.setItems(FXCollections.observableArrayList(MenuItemController.getAllMenuItems()));

		// biar bisa select data-data
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				itemId.setText(newSelection.getMenuItemId() + "");
				itemName.setText(newSelection.getMenuItemName());
				itemDesc.setText(newSelection.getMenuItemDescription());
				itemPrice.setText(String.valueOf(newSelection.getMenuItemPrice()));
				itemQuantity.setText("1");
			}
		});

		return table;
	}

	// untuk membuat form dibawah table ordersbyorderid yang bisa diinteract dgn
	// button dan diisi new quantity
	private GridPane createUpdateOrderForm(Order selectedOrder, TableView<OrderItem> orderItemTable,
			TableView<MenuItem> tableMenuItem, TableView<Order> tableOrdered, User user) {
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);

		Button serveOrderButton = new Button("Serve Order");
		Button updateOrderButton = new Button("Add / Update Order");
		Button removeOrderButton = new Button("Remove Order");

		// untuk placement dari masing-masing komponen
		form.add(new Label("Quantity:"), 0, 0);
		form.add(updateItemQuantity, 1, 0);

		form.add(serveOrderButton, 0, 1);
		form.add(updateOrderButton, 0, 2);
		form.add(removeOrderButton, 0, 3);

		// untuk get selected orderitem itu orderitem yang mana
		orderItemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				updateItemName.setText(newSelection.getMenuItemName());
				updateItemPrice.setText(String.valueOf(newSelection.getMenuItem().getMenuItemPrice()));
				updateItemDesc.setText(newSelection.getMenuItemDesc());
				updateItemQuantity.setText(String.valueOf(newSelection.getQuantity()));

			}

		});

		// kalau ini untuk selected menuitem
		tableMenuItem.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				updateItemName.setText(newSelection.getMenuItemId() + "");
				updateItemName.setText(newSelection.getMenuItemName());
				updateItemDesc.setText(newSelection.getMenuItemDescription());
				updateItemPrice.setText(String.valueOf(newSelection.getMenuItemPrice()));
				updateItemQuantity.setText("1");
			}
		});

		// button serve order yg mengubah status order ke "Served"
		serveOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (selectedOrder != null) {
					ArrayList<OrderItem> orderItems = OrderItemController
							.getAllOrderItemsByOrderId(selectedOrder.getOrderId());
					OrderController.updateOrder(selectedOrder.getOrderId(), orderItems, "Served");
					tableOrdered.setItems(FXCollections.observableArrayList(OrderController.getAllPreparedOrders()));

					showAlert("Order Served", "Selected order has been served.");
				} else {
					showAlert("No Order Selected", "Please select a prepared order to serve.");
				}
				viewOrdered(user);
				
				root3.getChildren().clear();
			}
		});

		// button update untuk mengubah quantity dari orderitem
		updateOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				OrderItem selectedOrderItem = orderItemTable.getSelectionModel().getSelectedItem();
				MenuItem selectedMenuItem = tableMenuItem.getSelectionModel().getSelectedItem();

				if (selectedOrderItem != null) {
					int newQuantity = Integer.parseInt(updateItemQuantity.getText());

					if (newQuantity == 0) {
						// ini kita anggap kalau mau set ke 0, artinya didelete order itemnya
						OrderItemController.deleteOrderItem(selectedOrderItem.getOrderId(),
								selectedOrderItem.getMenuItemId());

						orderItemTable.getItems().remove(selectedOrderItem);

						totalUpdateLabel.setText(
								"Total Price: " + OrderController.getTotalByOrderId(selectedOrderItem.getOrderId()));

						// table direfresh kembali dengan prepared order yang masih ada
						tableOrdered.setItems(FXCollections.observableArrayList(OrderController.getAllPreparedOrders()));

						showAlert("Remove Order Item", "Selected Order Item Has Been Removed");

					} else if (newQuantity > 0) {
						selectedOrderItem.setQuantity(newQuantity);
						OrderItemController.updateOrderItem(selectedOrderItem.getOrderId(), selectedOrderItem.getMenuItem(),
								newQuantity);

						// refresh orderItemTable
						orderItemTable.refresh();

						totalUpdateLabel.setText(
								"Total Price: " + OrderController.getTotalByOrderId(selectedOrderItem.getOrderId()));

						tableOrdered.setItems(FXCollections.observableArrayList(OrderController.getAllPreparedOrders()));
						showAlert("Update Order Item", "Successfully updated the quantity!");
					} else {
						// ini kalau order item yang diinput kurang dari 0
						showAlert("Update Order Item", "Quantity Must Be Positive");
					}

					orderItemTable.refresh();

					// update label total price dengan order yang diselect
					totalUpdateLabel.setText(
							"Total Price: " + OrderController.getTotalByOrderId(selectedOrderItem.getOrderId()));
					formClear();

					orderItemTable.getSelectionModel().clearSelection();
					tableMenuItem.getSelectionModel().clearSelection();
				} else if (selectedMenuItem != null) {
					// flag untuk cek apakah item sudah ada dalam database
					boolean itemExists = false;
					int newQuantity = Integer.parseInt(updateItemQuantity.getText());
					for (OrderItem existingOrderItem : orderItemTable.getItems()) {
						if (existingOrderItem.getMenuItemId() == selectedMenuItem.getMenuItemId()) {

							// ini untuk cek quantitynya yg harus lebih dari 0
							if (OrderItemController.updateOrderItem(existingOrderItem.getOrderId(),
									existingOrderItem.getMenuItem(), newQuantity)) {
								existingOrderItem.setQuantity(newQuantity);
								showAlert("Update Order Item", "Successfully updated the quantity");

							} else {
								showAlert("Update Order Item Error", "Quantity must be more than 0");
							}

							orderItemTable.refresh();

							// refresh kembali label total price
							totalUpdateLabel.setText("Total Price: "
									+ OrderController.getTotalByOrderId(existingOrderItem.getOrderId()));

							// flag direturn true
							itemExists = true;
							break;
						}
					}

					// Jika item belum ada dalam orderItemList, tambahkan item baru ke dalam order
					if (!itemExists && selectedMenuItem != null) {

						OrderItem newOrderItem = new OrderItem(selectedOrder.getOrderId(), selectedMenuItem,
								newQuantity);

						if (OrderItemController.createOrderItem(selectedOrder.getOrderId(), selectedMenuItem,
								newQuantity)) {

							showAlert("Update Order Item", "Success Add New Order Item");
							// Refresh orderItemTable
							orderItemTable.getItems().add(newOrderItem);

						} else {
							showAlert("Update Order Item Failed", "Failed: Quantity must be more than 0");
						}

						// Refresh total label
						totalUpdateLabel.setText(
								"Total Price: " + OrderController.getTotalByOrderId(selectedOrder.getOrderId()));

						// Refresh ordered table
						tableOrdered.setItems(FXCollections.observableArrayList(OrderController.getAllPreparedOrders()));

					}

					// Clear form fields dan selection
					tableOrdered.setItems(FXCollections.observableArrayList(OrderController.getAllPreparedOrders()));
					formClear();
					orderItemTable.refresh();
					orderItemTable.getSelectionModel().clearSelection();
					tableMenuItem.getSelectionModel().clearSelection();

				} else {
					showAlert("Update Order Item", "Please Select An Order Item or Menu Item To Update Order");
				}
			}
		});
		// remove order dan semua order item yang ada didalamnya via on cascade
		removeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (selectedOrder != null) {
					int selectedOrderId = selectedOrder.getOrderId();

					// langsung delete order dan semua order di dalamnya
					OrderController.deleteOrder(selectedOrderId);

					tableOrdered.setItems(FXCollections.observableArrayList(OrderController.getAllPreparedOrders()));

				} else {
					showAlert("No Order Selected", "Please select an order to remove.");
				}

				viewOrdered(user);
			}
		});

		return form;
	}

	// untuk mengisi bagian tengah stage dengan table order details yang telah
	// dibuat
	private void showOrderDetails(Order selectedOrder, TableView<Order> tableOrdered, User user) {
		TableView<OrderItem> orderItemTable = null;
		orderDetailLabel = new Label("Order Details");
		chooseItemLabel = new Label("Choose Menu Item To Add Item");

		root2.getChildren().clear();
		root3.getChildren().clear();
		root2.getChildren().add(orderDetailLabel);

		if (selectedOrder != null) {
			orderItemTable = createOrdersByOrderIdTable(selectedOrder);
			TableView<MenuItem> tableMenuItem = createMenuItemTable();
			root2.getChildren().add(orderItemTable);

			double totalOrderPrice = selectedOrder.getOrderTotal();
			totalUpdateLabel = new Label("Total Price: " + totalOrderPrice);
			root2.getChildren().add(totalUpdateLabel);

			root2.getChildren()
					.add(createUpdateOrderForm(selectedOrder, orderItemTable, tableMenuItem, tableOrdered, user));

			tableMenuItem.setStyle("-fx-background-color: lightblue;");
			root3.getChildren().add(chooseItemLabel);
			root3.getChildren().add(tableMenuItem);
		}

	}
}
