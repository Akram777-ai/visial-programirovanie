package app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class TeacherController {

    @FXML private TextField txtSurname;
    @FXML private TextField txtName;
    @FXML private TextField txtDepartment;
    @FXML private ComboBox<String> cmbPosition;
    @FXML private TextField txtExperience;
    @FXML private ComboBox<String> cmbCity;
    @FXML private RadioButton radFullTime;
    @FXML private RadioButton radPartTime;
    @FXML private CheckBox chkDegree;
    @FXML private Label lblResult;

    @FXML
    public void initialize() {
        cmbPosition.getItems().addAll(
                "Ассистент", "Старший преподаватель", "Доцент",
                "Профессор", "Заведующий кафедрой");
        cmbPosition.getSelectionModel().selectFirst();

        cmbCity.getItems().addAll(
                "Алматы", "Астана", "Шымкент", "Караганда", "Актобе");
        cmbCity.getSelectionModel().selectFirst();
    }

    @FXML
    private void onCreateClick() {
        String surname = txtSurname.getText().trim();
        String name = txtName.getText().trim();
        String department = txtDepartment.getText().trim();
        String position = cmbPosition.getValue();
        String experience = txtExperience.getText().trim();
        String city = cmbCity.getValue();

        if (surname.isBlank() || name.isBlank() ||
                department.isBlank() || experience.isBlank()) {
            showError("Заполните все поля.");
            return;
        }

        int years;
        try {
            years = Integer.parseInt(experience);
        } catch (NumberFormatException e) {
            showError("Стаж должен быть числом.");
            return;
        }

        if (years < 0 || years > 60) {
            showError("Введите корректный стаж (0-60 лет).");
            return;
        }

        String employment = radFullTime.isSelected() ? "Штатный" : "Совместитель";
        String degree = chkDegree.isSelected() ? "да" : "нет";

        lblResult.setText(
                "Преподаватель: " + surname + " " + name +
                "\nКафедра: " + department +
                "\nДолжность: " + position +
                "\nСтаж: " + years + " лет" +
                "\nГород: " + city +
                "\nФорма занятости: " + employment +
                "\nУчёная степень: " + degree
        );
    }

    @FXML
    private void onClearClick() {
        txtSurname.clear();
        txtName.clear();
        txtDepartment.clear();
        txtExperience.clear();
        cmbPosition.getSelectionModel().selectFirst();
        cmbCity.getSelectionModel().selectFirst();
        radFullTime.setSelected(true);
        chkDegree.setSelected(false);
        lblResult.setText("");
        txtSurname.requestFocus();
    }

    @FXML
    private void onExitClick() {
        Platform.exit();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
