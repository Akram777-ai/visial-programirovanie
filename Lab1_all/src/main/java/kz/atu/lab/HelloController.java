package kz.atu.lab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSurname;

    @FXML
    private Label lblResult;

    @FXML
    protected void onHelloClick() {
        String name = txtName.getText().trim();
        String surname = txtSurname.getText().trim();

        if (name.isBlank() || surname.isBlank()) {
            lblResult.setText("Введите имя и фамилию!");
            return;
        }

        lblResult.setText("Здравствуйте, " + name + " " + surname + "!");
    }
}
