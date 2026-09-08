import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Лабораторная работа №3 (лекция 1) — «Ввод и вывод данных»
 * Пользователь вводит имя и фамилию. После нажатия «Поприветствовать»
 * приложение выводит персональное приветствие.
 *
 * Единый файл (без FXML/Scene Builder) — интерфейс собран программно.
 */
public class GreetingApp extends Application {

    private TextField txtName;
    private TextField txtSurname;
    private Label lblResult;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20, 30, 20, 30));

        Label title = new Label("ВВОД И ВЫВОД ДАННЫХ");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getColumnConstraints().addAll(
                new ColumnConstraints(100),
                new ColumnConstraints(220));

        txtName = new TextField();
        txtSurname = new TextField();

        grid.add(new Label("Имя:"), 0, 0);
        grid.add(txtName, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(txtSurname, 1, 1);

        Button btnGreet = new Button("Поприветствовать");
        btnGreet.setOnAction(e -> onGreetClick());

        lblResult = new Label();
        lblResult.setWrapText(true);

        root.getChildren().addAll(title, grid, btnGreet, lblResult);

        stage.setTitle("Ввод и вывод данных");
        stage.setScene(new Scene(root, 380, 260));
        stage.show();
    }

    private void onGreetClick() {
        String name = txtName.getText().trim();
        String surname = txtSurname.getText().trim();

        if (name.isBlank() || surname.isBlank()) {
            lblResult.setText("Введите имя и фамилию!");
            return;
        }

        lblResult.setText("Здравствуйте, " + name + " " + surname + "!");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
