package view;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    private VBox root = new VBox(25);
    private Scene scene;
    private MenuBar menuBar = new MenuBar();
    
   
    public CustomerPanel() {
    	
        super(StageStyle.DECORATED);
        this.setTitle("Customer Dashboard");
                        
        scene = new Scene(borderPane, 1000, 600);
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
        
        borderPane.setCenter(root);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: lightgray;");
        borderPane.setCenter(root);
                      
       
        
    }
    
    //variabel nyimpen select dan form
    private TextField ItemId = new TextField();
    private TextField ItemName = new TextField();
    private TextField ItemDesc = new TextField();
    private TextField ItemPrice = new TextField();
    private TextField ItemQuantity= new TextField();
    
    TableView<OrderItem> createdCartTable = createCartTable();
    private void addOrder() {
    	//buat hilangin tampilan isi sebelumnya
    	root.getChildren().clear();
    	
    	//buat tampilan baru
    	TableView<MenuItem> tableMenuItem = createMenuItemTable();
    	tableMenuItem.setStyle("-fx-background-color: lightblue;");
    	GridPane form = createOrderForm(tableMenuItem);
       	root.getChildren().addAll(tableMenuItem, form, createdCartTable);
    	

	}
    
	private TableView<MenuItem> createMenuItemTable() {
    	TableView<MenuItem> table = new TableView<>();
    	table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    	
    	TableColumn<MenuItem, String> menuItemName = new TableColumn<>("Item Name");
    	menuItemName.setCellValueFactory(new PropertyValueFactory<>("menuItemName"));
    	menuItemName.setPrefWidth(200);
    	
    	TableColumn<MenuItem, String> menuItemDesc = new TableColumn<>("Item Description");
    	menuItemDesc.setCellValueFactory(new PropertyValueFactory<>("menuItemDescription"));
    	menuItemName.setPrefWidth(200);
    	
    	TableColumn<MenuItem, String> menuItemPrice = new TableColumn<>("Item Price");
    	menuItemPrice.setCellValueFactory(new PropertyValueFactory<>("menuItemPrice"));
    	menuItemName.setPrefWidth(200);
    	
    	table.getColumns().add(menuItemName);
    	table.getColumns().add(menuItemDesc);
    	table.getColumns().add(menuItemPrice);
    	
    	table.setItems(FXCollections.observableArrayList(MenuItem.getAllMenuItems()));
    	
    	//biar bisa select data-data
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
	
	private TableView<OrderItem> createCartTable() {
	    TableView<OrderItem> table = new TableView<>();

	    // Tambahkan kolom-kolom yang sesuai dengan atribut OrderItem
	    TableColumn<OrderItem, String> itemName = new TableColumn<>("Item Name");
	    itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));

	    TableColumn<OrderItem, String> itemDesc = new TableColumn<>("Item Description");
	    itemDesc.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));

	    TableColumn<OrderItem, Double> itemPrice = new TableColumn<>("Item Price");
	    itemPrice.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));

	    TableColumn<OrderItem, Integer> itemQuantity = new TableColumn<>("Quantity");
	    itemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

	    table.getColumns().addAll(itemName, itemDesc, itemPrice, itemQuantity);
	    
	    
	    return table;
	}
    
    
	ArrayList<OrderItem> tempCart = new ArrayList<>();;;
	
    private GridPane createOrderForm(TableView<MenuItem> tableMenuItem) {
    	GridPane form = new GridPane();
    	form.setVgap(20);
        form.setHgap(10);
        
        Button addItemButton = new Button("Add Item");
        Button makeOrderButton = new Button("Make Order");
        
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
             
       
        addItemButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MenuItem selectedMenuItem = tableMenuItem.getSelectionModel().getSelectedItem();
                if (selectedMenuItem != null && ItemQuantity.equals("0") == false) {
                	tempCart.add(new OrderItem(0, selectedMenuItem.getMenuItemId(), Integer.parseInt(ItemQuantity.getText())));
                	createdCartTable.setItems(FXCollections.observableArrayList(tempCart));
                	
                }
            }
        });
        
        makeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tempCart != null) {
                	LocalDate currentDate = LocalDate.now();
                	DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					String formattedDate = currentDate.format(formatDate);
					Date date = Date.valueOf(formattedDate);
			                	
                	Order.createOrder(User.getUserById(0), tempCart, date);
                	tempCart.clear();
                	
                }
            }
        });
        
		return form;
	}


    
    
    
    private void viewOrdered() {
		// TODO Auto-generated method stub
    	root.getChildren().clear();
    	
    	//buat tampilan baru
    	TableView<Order> tableOrdered = createOrderedTable();
    	root.getChildren().add(tableOrdered);
    	
    	//biar bisa select data-data
    	tableOrdered.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	        	
	        }
	    });
  
    	
    	

	}
    
    private TableView<Order> createOrderedTable() {
		// TODO Auto-generated method stunt
    	TableView<Order> table = new TableView<>();
//    	tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    	    
        TableColumn<Order, Integer> orderID = new TableColumn<>("Order ID");
        orderID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        
        TableColumn<Order, String> orderStatus= new TableColumn<>("Status");
        orderStatus.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        
        TableColumn<Order, Date> orderDate = new TableColumn<>("Date");
        orderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
       
        table.getColumns().addAll(orderID, orderStatus, orderDate);
        table.setItems(FXCollections.observableArrayList(Order.getOrdersByCustomerId(1)));
        
        return table;
	}
    
   

}


//private void showCustomerOptions() {
//Button viewMenuButton = new Button("View Menu Items and Add Order");
//viewMenuButton.setOnAction(e -> {
//  // Code to navigate to View Menu Items and Add Order page
//  // You can create a new stage or update the current stage's scene
//});
//
//Button viewOrderedItemsButton = new Button("View Ordered Menu Items and Update Ordered Menu Items");
//viewOrderedItemsButton.setOnAction(e -> {
//  // Code to navigate to View Ordered Menu Items and Update Ordered Menu Items page
//});
//
//Button viewOrderDetailsButton = new Button("View Order Details and Process Order Payment");
//viewOrderDetailsButton.setOnAction(e -> {
//  // Code to navigate to View Order Details and Process Order Payment page
//});
//
//root.getChildren().clear();
//root.getChildren().addAll(viewMenuButton, viewOrderedItemsButton, viewOrderDetailsButton);
//}

