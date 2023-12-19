package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AdminPanel extends Stage {

    public AdminPanel() {
        start();
    }

    private void start() {
        AnchorPane root = new AnchorPane();

        TabPane tabPane = new TabPane();
        Tab adminTab = new Tab("Admin Panel");

        adminTab.setContent(createAdminContent());

        tabPane.getTabs().add(adminTab);

        root.getChildren().add(tabPane);

        Scene scene = new Scene(root, 800, 600);
        setScene(scene);
        setTitle("Admin Panel");

        UserManagementView userManagementView = new UserManagementView();
        userManagementView.viewAllUsers();
    }

    private AnchorPane createAdminContent() {
        AnchorPane adminContent = new AnchorPane();

        UserManagementView userManagementView = new UserManagementView();
        MenuManagementView menuManagementView = new MenuManagementView();

        AnchorPane.setTopAnchor(userManagementView.getRoot(), 10.0);
        AnchorPane.setLeftAnchor(userManagementView.getRoot(), 10.0);

        AnchorPane.setTopAnchor(menuManagementView.getRoot(), 10.0);
        AnchorPane.setLeftAnchor(menuManagementView.getRoot(), 10.0);

        adminContent.getChildren().addAll(userManagementView.getRoot(), menuManagementView.getRoot());

        return adminContent;
    }

    public void showAdminPanel() {
        showAndWait();
    }
} 