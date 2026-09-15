package kz.atu.lab03;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Лабораторная работа №3. "Интерактивный калькулятор" (JavaFX).
 * ------------------------------------------------------------
 * Всё приложение (Application + FXML + Controller) собрано в ОДНОМ файле
 * для удобства запуска / отправки, но структура сохраняет требуемые
 * лабораторной работой принципы: интерфейс описан в FXML (строкой,
 * эквивалентной calculator-view.fxml из Scene Builder), а логика вынесена
 * в отдельный класс-контроллер CalculatorController с аннотациями @FXML.
 *
 * Реализовано:
 *  - основная часть (п.1-26): сложение, вычитание, умножение, деление,
 *    один общий обработчик #onOperation для нескольких кнопок,
 *    event.getSource(), проверка пустых полей, NumberFormatException,
 *    деление на ноль, Alert, Очистить, Выход, события мыши, disable-свойство;
 *  - самостоятельная часть (п.27): возведение в степень (x^y), остаток
 *    от деления (%), округление результата до 2 знаков, счётчик операций;
 *  - индивидуальный вариант №7: "Квадрат первого числа" (кнопка x²A).
 *
 * Для запуска нужен JavaFX SDK, например:
 *   javac --module-path <путь_к_javafx>/lib --add-modules javafx.controls,javafx.fxml Lab03Calculator.java
 *   java  --module-path <путь_к_javafx>/lib --add-modules javafx.controls,javafx.fxml kz.atu.lab03.Lab03Calculator
 */
public class Lab03Calculator extends Application {

    // ---------------------------------------------------------------
    // FXML интерфейса (эквивалент calculator-view.fxml из Scene Builder)
    // ---------------------------------------------------------------
    private static final String FXML = """
            <?xml version="1.0" encoding="UTF-8"?>

            <?import javafx.geometry.Insets?>
            <?import javafx.scene.control.Button?>
            <?import javafx.scene.control.Label?>
            <?import javafx.scene.control.TextField?>
            <?import javafx.scene.layout.GridPane?>
            <?import javafx.scene.layout.HBox?>
            <?import javafx.scene.layout.VBox?>

            <VBox spacing="15" alignment="CENTER" xmlns="http://javafx.com/javafx"
                  xmlns:fx="http://javafx.com/fxml">
                <padding>
                    <Insets top="20" right="20" bottom="20" left="20"/>
                </padding>

                <Label text="ИНТЕРАКТИВНЫЙ КАЛЬКУЛЯТОР" style="-fx-font-size: 18; -fx-font-weight: bold;"/>

                <GridPane hgap="10" vgap="10" alignment="CENTER">
                    <Label text="Первое число:" GridPane.rowIndex="0" GridPane.columnIndex="0"/>
                    <TextField fx:id="txtNumber1" promptText="Первое число"
                               GridPane.rowIndex="0" GridPane.columnIndex="1"/>

                    <Label text="Второе число:" GridPane.rowIndex="1" GridPane.columnIndex="0"/>
                    <TextField fx:id="txtNumber2" promptText="Второе число"
                               GridPane.rowIndex="1" GridPane.columnIndex="1"/>
                </GridPane>

                <HBox spacing="10" alignment="CENTER">
                    <Button fx:id="btnAdd" text="+" onAction="#onOperation"
                            onMouseEntered="#onMouseEntered" onMouseExited="#onMouseExited"/>
                    <Button fx:id="btnSubtract" text="-" onAction="#onOperation"
                            onMouseEntered="#onMouseEntered" onMouseExited="#onMouseExited"/>
                    <Button fx:id="btnMultiply" text="×" onAction="#onOperation"
                            onMouseEntered="#onMouseEntered" onMouseExited="#onMouseExited"/>
                    <Button fx:id="btnDivide" text="÷" onAction="#onOperation"
                            onMouseEntered="#onMouseEntered" onMouseExited="#onMouseExited"/>
                    <Button fx:id="btnPower" text="x^y" onAction="#onOperation"
                            onMouseEntered="#onMouseEntered" onMouseExited="#onMouseExited"/>
                    <Button fx:id="btnMod" text="%" onAction="#onOperation"
                            onMouseEntered="#onMouseEntered" onMouseExited="#onMouseExited"/>
                </HBox>

                <HBox spacing="10" alignment="CENTER">
                    <Button fx:id="btnSquare" text="x²(A)" onAction="#onSquareClick"
                            onMouseEntered="#onMouseEntered" onMouseExited="#onMouseExited"/>
                </HBox>

                <Label fx:id="lblResult" text="Результат: 0"/>
                <Label fx:id="lblOperation" text="Операция: —"/>
                <Label fx:id="lblCount" text="Выполнено операций: 0"/>

                <HBox spacing="10" alignment="CENTER">
                    <Button fx:id="btnClear" text="Очистить" onAction="#onClearClick"/>
                    <Button fx:id="btnExit" text="Выход" onAction="#onExitClick"/>
                </HBox>

                <HBox spacing="10" alignment="CENTER">
                    <Button fx:id="btnLock" text="Блокировать поля" onAction="#onLockClick"/>
                    <Button fx:id="btnUnlock" text="Разблокировать" onAction="#onUnlockClick"/>
                </HBox>
            </VBox>
            """;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new CalculatorController());
        Parent root = loader.load(new ByteArrayInputStream(FXML.getBytes(StandardCharsets.UTF_8)));

        stage.setTitle("Lab03Calculator");
        stage.setScene(new Scene(root, 420, 420));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // ---------------------------------------------------------------
    // Controller (логика приложения)
    // ---------------------------------------------------------------
    public static class CalculatorController {

        @FXML
        private TextField txtNumber1;
        @FXML
        private TextField txtNumber2;
        @FXML
        private Label lblResult;
        @FXML
        private Label lblOperation;
        @FXML
        private Label lblCount;

        // п.27, задание 4: счётчик операций
        private int operationCount = 0;

        /**
         * Общий обработчик для кнопок +, -, ×, ÷, x^y, % (п.10, п.15, п.18).
         * Источник события определяется через event.getSource() (п.15).
         */
        @FXML
        private void onOperation(ActionEvent event) {
            if (txtNumber1.getText().isBlank() || txtNumber2.getText().isBlank()) {
                showError("Введите оба числа.");
                return;
            }

            try {
                double number1 = Double.parseDouble(txtNumber1.getText());
                double number2 = Double.parseDouble(txtNumber2.getText());

                Button button = (Button) event.getSource();
                String operation = button.getText();

                double result;
                switch (operation) {
                    case "+":
                        result = number1 + number2;
                        break;
                    case "-":
                        result = number1 - number2;
                        break;
                    case "×":
                        result = number1 * number2;
                        break;
                    case "÷":
                        if (number2 == 0) {
                            showError("Деление на ноль невозможно.");
                            return;
                        }
                        result = number1 / number2;
                        break;
                    case "x^y":
                        // самостоятельная часть, задание 1: возведение в степень
                        result = Math.pow(number1, number2);
                        break;
                    case "%":
                        // самостоятельная часть, задание 2: остаток от деления
                        if (number2 == 0) {
                            showError("Деление на ноль невозможно.");
                            return;
                        }
                        result = number1 % number2;
                        break;
                    default:
                        return;
                }

                showResult(result, operation);

            } catch (NumberFormatException e) {
                showError("Введите корректные числа.");
            }
        }

        /**
         * Индивидуальный вариант №7: "Квадрат первого числа".
         * Использует только первое поле ввода.
         */
        @FXML
        private void onSquareClick(ActionEvent event) {
            if (txtNumber1.getText().isBlank()) {
                showError("Введите первое число.");
                return;
            }

            try {
                double number1 = Double.parseDouble(txtNumber1.getText());
                double result = number1 * number1;
                showResult(result, "x²(A)");
            } catch (NumberFormatException e) {
                showError("Введите корректное число.");
            }
        }

        /**
         * Общий вывод результата: округление до 2 знаков (самостоятельная
         * часть, задание 3) и обновление счётчика операций (задание 4).
         */
        private void showResult(double result, String operationLabel) {
            lblResult.setText(String.format("Результат: %.2f", result));
            lblOperation.setText("Операция: " + operationLabel);

            operationCount++;
            lblCount.setText("Выполнено операций: " + operationCount);
        }

        @FXML
        private void onClearClick() {
            txtNumber1.clear();
            txtNumber2.clear();
            lblResult.setText("Результат: 0");
            lblOperation.setText("Операция: —");
            txtNumber1.requestFocus();
        }

        @FXML
        private void onExitClick() {
            Platform.exit();
        }

        @FXML
        private void onMouseEntered() {
            lblOperation.setText("Выберите операцию");
        }

        @FXML
        private void onMouseExited() {
            if (lblResult.getText().equals("Результат: 0")) {
                lblOperation.setText("Операция: —");
            }
        }

        @FXML
        private void onLockClick() {
            txtNumber1.setDisable(true);
            txtNumber2.setDisable(true);
        }

        @FXML
        private void onUnlockClick() {
            txtNumber1.setDisable(false);
            txtNumber2.setDisable(false);
            txtNumber1.requestFocus();
        }

        private void showError(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
}
