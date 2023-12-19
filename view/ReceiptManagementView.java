package view;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.MenuItem;
import model.Receipt;
import model.User;

public class ReceiptManagementView{
	private TableView<Receipt> table = new TableView<>();
	private User currentUser;
	
	public VBox getRoot() {
        
		if (currentUser != null) {
            VBox root = new VBox();
            root.getChildren().add(createReceiptTable());
            return root;
        } else {
            return new VBox(); 
        }
    }
	public ReceiptManagementView() {
		
	}
	private TableView<Receipt> createReceiptTable() {
		
		return null;
	}
	
	private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
