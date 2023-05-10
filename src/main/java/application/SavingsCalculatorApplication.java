package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SavingsCalculatorApplication extends Application{

    public static void main(String[] args) {
        launch(SavingsCalculatorApplication.class);
    }

    @Override
    public void start(Stage window) throws Exception {
        UserInterface ui = new UserInterface();
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        ui.start(layout);
        Scene view = new Scene(layout, 640, 480);
        window.setScene(view);
        window.show();
    }

}
