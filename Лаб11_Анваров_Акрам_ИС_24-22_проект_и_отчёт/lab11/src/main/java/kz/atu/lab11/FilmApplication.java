package kz.atu.lab11;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FilmApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(FilmApplication.class.getResource("film-view.fxml"));
        stage.setTitle("Каталог фильмов");
        stage.setScene(new Scene(loader.load(), 720, 480));
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
