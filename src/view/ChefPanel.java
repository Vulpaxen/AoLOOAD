package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Order;
import model.OrderItem;

public class ChefPanel extends Stage {
 private VBox root = new VBox(10);
 private Scene scene = new Scene(root, 800, 600);
 private ListView<String> ordersListView = new ListView<>();
 private TextArea orderDetailsTextArea = new TextArea();
 private Button prepareOrderBtn = new Button("Prepare Order");
 private Button updateOrderBtn = new Button("Update Order");
 private Button removeOrderBtn = new Button("Remove Order");

 public ChefPanel() {
     super(StageStyle.DECORATED);
     this.setScene(scene);
     root.setPadding(new Insets(10));
     root.setAlignment(Pos.TOP_CENTER);

     this.setTitle("Chef Panel");
     showChefPanel();
 }

 private void showChefPanel() {
     Label titleLabel = new Label("Chef Panel");
     titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
     titleLabel.setAlignment(Pos.CENTER);

     ordersListView.setPrefHeight(300);

     prepareOrderBtn.setMaxWidth(Double.MAX_VALUE);
     prepareOrderBtn.setOnAction(e -> prepareSelectedOrder());

     updateOrderBtn.setMaxWidth(Double.MAX_VALUE);
     updateOrderBtn.setOnAction(e -> updateSelectedOrder());

     removeOrderBtn.setMaxWidth(Double.MAX_VALUE);
     removeOrderBtn.setOnAction(e -> removeSelectedOrder());

     VBox container = new VBox(10);
     container.setPadding(new Insets(20));
     container.setAlignment(Pos.CENTER);
     container.getChildren().addAll(titleLabel, ordersListView, orderDetailsTextArea, prepareOrderBtn, updateOrderBtn, removeOrderBtn);

     root.getChildren().clear();
     root.getChildren().add(container);

     // TODO: Load and display Pending orders in ordersListView
     // You may need to have a method to load orders and update UI accordingly
 }

 private void prepareSelectedOrder() {
     // TODO: Implement logic to update the status of the selected order to "Prepared"
     // Update the UI accordingly
 }

 private void updateSelectedOrder() {
     // TODO: Implement logic to update the details of the selected order
     // Update the UI accordingly
 }

 private void removeSelectedOrder() {
     // TODO: Implement logic to remove the selected order
     // Update the UI accordingly
 }
}
