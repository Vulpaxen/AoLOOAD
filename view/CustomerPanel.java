package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomerPanel extends Stage {
    private VBox root = new VBox(10);
    private Scene scene = new Scene(root, 500, 300);
    
    public CustomerPanel() {
        super(StageStyle.DECORATED);
        this.setScene(scene);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);
        
        this.setTitle("Customer Dashboard");
        showCustomerOptions();
    }

    private void showCustomerOptions() {
        Button viewMenuButton = new Button("View Menu Items and Add Order");
        viewMenuButton.setOnAction(e -> {
            // Code to navigate to View Menu Items and Add Order page
            // You can create a new stage or update the current stage's scene
        });

        Button viewOrderedItemsButton = new Button("View Ordered Menu Items and Update Ordered Menu Items");
        viewOrderedItemsButton.setOnAction(e -> {
            // Code to navigate to View Ordered Menu Items and Update Ordered Menu Items page
        });

        Button viewOrderDetailsButton = new Button("View Order Details and Process Order Payment");
        viewOrderDetailsButton.setOnAction(e -> {
            // Code to navigate to View Order Details and Process Order Payment page
        });

        root.getChildren().clear();
        root.getChildren().addAll(viewMenuButton, viewOrderedItemsButton, viewOrderDetailsButton);
    }
}

