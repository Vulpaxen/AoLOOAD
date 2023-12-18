package view;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.User;

public class CustomerPanel extends Stage {

	private BorderPane borderPane = new BorderPane();
    private VBox root1 = new VBox(20);
    private VBox root2 = new VBox(20);
    private Scene scene;
    private MenuBar menuBar = new MenuBar();
    
    
    private OrderController orderController = new OrderController();
    private OrderItemController orderItemController = new OrderItemController();
    
   
    public CustomerPanel() {
    	
        super(StageStyle.DECORATED);
        this.setTitle("Customer Dashboard");
        
                        
        scene = new Scene(borderPane, 1000, 800);
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
       
        
        addOrder();
        
        AddOrderMenuItem.setOnAction(e ->{
        	addOrder();
        });
        
             
        viewOrderedMenuItem.setOnAction(e -> {
        	viewOrdered();
        });
        
        borderPane.setLeft(root1);
        root1.setAlignment(Pos.CENTER);
        root1.setPadding(new Insets(20));
        root1.setPrefHeight(getMaxHeight());
        root1.setStyle("-fx-background-color: lightgray;");
        
        borderPane.setCenter(root2);
        root2.setAlignment(Pos.CENTER);
        root2.setPadding(new Insets(20));
                                        
    }
    
    //variabel nyimpen select dan form
    private TextField ItemId = new TextField();
    private TextField ItemName = new TextField();
    private TextField ItemDesc = new TextField();
    private TextField ItemPrice = new TextField();
    private TextField ItemQuantity= new TextField();
    
    
    //Tampilan Customer Untuk Add Order
    //Tabel Menu Order Item, Form buat Nambah Item, Tabel Cart Item yang udah ditambahin 
    TableView<OrderItem> createdCartTable = createCartTable();
    private void addOrder() {
    	//buat hilangin tampilan isi sebelumnya
    	root1.getChildren().clear();
    	root2.getChildren().clear();
    	
    	//buat tampilan baru
    	TableView<MenuItem> tableMenuItem = createMenuItemTable();
    	tableMenuItem.setStyle("-fx-background-color: lightblue;");
       	root1.getChildren().addAll(tableMenuItem);
       	
       	GridPane form = createOrderForm(tableMenuItem);
        Label totalLabel = new Label("Total Price: 0");
    	root2.getChildren().addAll(form, createdCartTable, totalLabel );
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

		table.setItems(FXCollections.observableArrayList(MenuItem.getAllMenuItems()));

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
	    itemQuantity.setCellValueFactory(orderItem -> new SimpleIntegerProperty(orderItem.getValue().getQuantity()).asObject());
	    itemQuantity.setPrefWidth(50);

	    // New TableColumn for itemTotalPrice
	    TableColumn<OrderItem, Double> itemTotalPrice = new TableColumn<>("Total Price");
	    itemTotalPrice.setCellValueFactory(orderItem -> new SimpleDoubleProperty(orderItem.getValue().getQuantity() * orderItem.getValue().getMenuItem().getMenuItemPrice()).asObject());
	    itemTotalPrice.setPrefWidth(100);

		table.getColumns().addAll(itemName, itemQuantity, itemTotalPrice);

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
	private GridPane createOrderForm(TableView<MenuItem> tableMenuItem) {
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
		form.add(resetOrderButton, 13, 6);

		// button tambahin item ke keranjang
		addItemButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MenuItem selectedMenuItem = tableMenuItem.getSelectionModel().getSelectedItem();
				if (selectedMenuItem != null && !ItemQuantity.getText().equals("0")) {

					// Check if the selected item is already in the cart
					OrderItem existingOrderItem = tempCart.stream().filter(
							orderItem -> orderItem.getMenuItem().getMenuItemId() == selectedMenuItem.getMenuItemId())
							.findFirst().orElse(null);

					if (existingOrderItem != null) {
						// If the item is already in the cart, update the quantity
						int newQuantity = existingOrderItem.getQuantity() + Integer.parseInt(ItemQuantity.getText());
						existingOrderItem.setQuantity(newQuantity);

						createdCartTable.refresh();
					} else {
						// If the item is not in the cart, add a new OrderItem
						OrderItem orderItem = new OrderItem(0, selectedMenuItem,
								Integer.parseInt(ItemQuantity.getText()));
						tempCart.add(orderItem);
					}

					createdCartTable.setItems(FXCollections.observableArrayList(tempCart));
					updateTotalLabel();

					ItemName.clear();
					ItemDesc.clear();
					ItemPrice.clear();
					ItemQuantity.clear();

					tableMenuItem.getSelectionModel().clearSelection();

				}
			}
		});
        
        //button buat Order dari Tabel Keranjang
        makeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tempCart != null) {
                	Date date = new Date(System.currentTimeMillis());
                	int orderId = orderController.createOrder(User.getUserById(1), tempCart, date, orderTotal);
			                	
                	for (OrderItem orderItem : tempCart) {
                		orderItemController.createOrderItem(orderId, orderItem.getMenuItem(), orderItem.getQuantity());
						
					}
					tempCart.clear();
					createdCartTable.setItems(FXCollections.observableArrayList(tempCart)); // Clear the table

					Label totalLabel = new Label("Total Price: 0");
					root2.getChildren().set(root2.getChildren().size() - 1, totalLabel);
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

				ItemName.clear();
				ItemDesc.clear();
				ItemPrice.clear();
				ItemQuantity.clear();

				tableMenuItem.getSelectionModel().clearSelection();
			}
		});

		return form;
	}

	// =========================================================
	// Customer Panel untuk View Ordered / History Order

	TableView<Order> tableOrdered = createOrderedTable();

	// Berisi Tabel Order List,
	private void viewOrdered() {
		// TODO Auto-generated method stub
		root1.getChildren().clear();
		root2.getChildren().clear();

		// buat tampilan baru
		root1.getChildren().add(tableOrdered);

		// Menampilkan label petunjuk jika belum ada pesanan yang dipilih
		if (tableOrdered.getSelectionModel().isEmpty()) {
			Label selectOrderLabel = new Label("Select an order from the table to see order details.");
			root2.getChildren().add(selectOrderLabel);
		} else {
			// Jika ada pesanan yang dipilih, tampilkan detail pesanan
			selectedOrder = tableOrdered.getSelectionModel().getSelectedItem();
			showOrderDetails(selectedOrder);
		}

//     	GridPane form = createOrderForm(tableMenuItem);

//    	root2.getChildren().addAll(form, createdCartTable, totalLabel );
//    	updateTotalLabel();

	}

	private TableView<OrderItem> createOrdersByOrderIdTable(Order OrderId) {
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

		ArrayList<OrderItem> orderItem = orderItemController.getAllOrderItemsByOrderId(selectedOrder.getOrderId());
// 	   System.out.println("Number of order items: " + orderItem.size());
		table.setItems(FXCollections.observableArrayList(orderItem));

		table.getColumns().addAll(itemName, itemQuantity, itemTotalPrice);

		return table;

	}

	Label totalUpdateLabel;

	private void showOrderDetails(Order selectedOrder) {
		TableView<OrderItem> orderItemTable = null;
		root2.getChildren().clear(); //
		if (selectedOrder != null) {
			orderItemTable = createOrdersByOrderIdTable(selectedOrder);
			root2.getChildren().add(orderItemTable);

			double totalOrderPrice = selectedOrder.getOrderTotal();
			totalUpdateLabel = new Label("Total Price: " + totalOrderPrice);
			root2.getChildren().add(totalUpdateLabel);

			root2.getChildren().add(createUpdateOrderForm(orderItemTable));
		}

	}

	private Order selectedOrder;

	private TableView<Order> createOrderedTable() {
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

		table.setItems(FXCollections.observableArrayList(Order.getOrdersByCustomerId(1)));

		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectedOrder = newSelection;
				showOrderDetails(selectedOrder);
			}
		});

		return table;
	}

	private void refreshOrderedTable() {
		tableOrdered.setItems(FXCollections.observableArrayList(Order.getOrdersByCustomerId(1)));
	}

	// variabel nyimpen select dan form untuk update product
	// variabel nyimpen select dan form untuk update product
	private TextField UpdateItemName = new TextField();
	private TextField UpdateItemDesc = new TextField();
	private TextField UpdateItemPrice = new TextField();
	private TextField UpdateItemQuantity = new TextField();

	private GridPane createUpdateOrderForm(TableView<OrderItem> orderItemTable) {
		GridPane form = new GridPane();
		form.setVgap(20);
		form.setHgap(10);

		Button updateItemButton = new Button("Update Order");

		form.add(new Label("Name:"), 0, 0);
		form.add(UpdateItemName, 1, 0);
		UpdateItemName.setDisable(true);

		form.add(new Label("Price:"), 0, 1);
		form.add(UpdateItemPrice, 1, 1);
		UpdateItemPrice.setDisable(true);

		form.add(new Label("Quantity:"), 0, 2);
		form.add(UpdateItemQuantity, 1, 2);

		form.add(updateItemButton, 0, 3);

		orderItemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				// Populate the form fields with the data from the selected OrderItem
				UpdateItemName.setText(newSelection.getMenuItemName());
				UpdateItemPrice.setText(String.valueOf(newSelection.getMenuItem().getMenuItemPrice()));
				UpdateItemQuantity.setText(String.valueOf(newSelection.getQuantity()));

				// Enable fields for editing
				UpdateItemName.setDisable(true);
				UpdateItemPrice.setDisable(true);
			}
		});
		// button untuk update item di keranjang
		updateItemButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				OrderItem selectedOrderItem = orderItemTable.getSelectionModel().getSelectedItem();
				if (selectedOrderItem != null) {
					int newQuantity = Integer.parseInt(UpdateItemQuantity.getText());
					System.out.println(selectedOrderItem.getOrderId());
					System.out.println(selectedOrderItem.getMenuItemId());

					if (newQuantity < 0) {
						// If the new quantity is 0, delete the order item
						selectedOrderItem.deleteOrderItem(selectedOrderItem.getOrderId(),
								selectedOrderItem.getMenuItemId());

						// Refresh the orderItemTable
						orderItemTable.getItems().remove(selectedOrderItem);

						totalUpdateLabel.setText(
								"Total Price: " + selectedOrder.getTotalByOrderId(selectedOrderItem.getOrderId()));

						refreshOrderedTable();
					} else {
						// Update the selected order item with the new quantity
						selectedOrderItem.setQuantity(newQuantity);
						selectedOrderItem.updateOrderItem(selectedOrderItem.getOrderId(),
								selectedOrderItem.getMenuItem(), newQuantity);

						// Refresh the orderItemTable
						orderItemTable.refresh();

						totalUpdateLabel.setText(
								"Total Price: " + selectedOrder.getTotalByOrderId(selectedOrderItem.getOrderId()));

						refreshOrderedTable();
					}

					UpdateItemName.clear();
					UpdateItemPrice.clear();
					UpdateItemQuantity.clear();
					
					orderItemTable.getSelectionModel().clearSelection();
				}
			}
		});

		return form;
	}

}
