package kz.atu.lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/kz/atu/lab/hello-view.fxml"));
        stage.setTitle("Ввод и вывод данных");
        stage.setScene(new Scene(root, 380, 280));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
