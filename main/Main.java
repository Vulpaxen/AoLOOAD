package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.Authentication;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Authentication authentication = new Authentication();
        authentication.show();
        
//        AdminPanel adm = new AdminPanel();
//        adm.show
    }
}
