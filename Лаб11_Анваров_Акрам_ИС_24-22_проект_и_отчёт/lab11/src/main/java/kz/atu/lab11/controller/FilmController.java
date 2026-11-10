package kz.atu.lab11.controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import kz.atu.lab11.model.Film;
import kz.atu.lab11.service.FilmService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Контроллер: только реакция на действия пользователя и обновление представления. */
public class FilmController {
    private static final String ALL = "Все";

    @FXML private TableView<Film> tableFilms;
    @FXML private TableColumn<Film, String> colTitle, colGenre;
    @FXML private TableColumn<Film, Number> colRating;
    @FXML private TextField txtTitle, txtGenre, txtRating, txtSearch;
    @FXML private ComboBox<String> cmbGenreFilter;
    @FXML private Label lblCount, lblAverage;

    private final FilmService service = new FilmService();
    private final ObservableList<String> genreItems = FXCollections.observableArrayList();
    private FilteredList<Film> filtered;

    @FXML
    private void initialize() {
        colTitle.setCellValueFactory(c -> c.getValue().titleProperty());
        colGenre.setCellValueFactory(c -> c.getValue().genreProperty());
        colRating.setCellValueFactory(c -> c.getValue().ratingProperty());

        service.addFilm("Начало", "Фантастика", 8.8);
        service.addFilm("Интерстеллар", "Фантастика", 8.6);
        service.addFilm("Крёстный отец", "Драма", 9.2);
        service.addFilm("Один дома", "Комедия", 7.7);

        filtered = new FilteredList<>(service.getFilms(), f -> true);
        SortedList<Film> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableFilms.comparatorProperty());
        tableFilms.setItems(sorted);

        cmbGenreFilter.setItems(genreItems);
        refreshGenres();

        txtSearch.textProperty().addListener((o, a, b) -> applyFilter());
        cmbGenreFilter.valueProperty().addListener((o, a, b) -> applyFilter());

        // счётчик и средний рейтинг обновляются при любом изменении данных
        filtered.addListener((ListChangeListener<Film>) c -> updateStats());
        service.getFilms().addListener((ListChangeListener<Film>) c -> updateStats());

        // выбор строки заполняет поля ввода
        tableFilms.getSelectionModel().selectedItemProperty().addListener((o, old, f) -> {
            if (f != null) {
                txtTitle.setText(f.getTitle());
                txtGenre.setText(f.getGenre());
                txtRating.setText(String.valueOf(f.getRating()));
            }
        });
        applyFilter();
    }

    private void applyFilter() {
        String q = txtSearch.getText() == null ? "" : txtSearch.getText().trim().toLowerCase(Locale.ROOT);
        String genre = cmbGenreFilter.getValue();
        filtered.setPredicate(f -> f.getTitle().toLowerCase(Locale.ROOT).contains(q)
                && (genre == null || genre.equals(ALL) || f.getGenre().equalsIgnoreCase(genre)));
        updateStats();
    }

    private void updateStats() {
        lblCount.setText("Найдено фильмов: " + filtered.size());
        lblAverage.setText(String.format("Средний рейтинг каталога: %.2f", service.averageRating()));
    }

    /** Динамический список жанров формируется из данных каталога. */
    private void refreshGenres() {
        String current = cmbGenreFilter.getValue();
        List<String> list = new ArrayList<>();
        list.add(ALL);
        list.addAll(service.getGenres());
        genreItems.setAll(list);
        cmbGenreFilter.setValue(list.contains(current) ? current : ALL);
    }

    private double readRating() {
        try {
            return Double.parseDouble(txtRating.getText().trim().replace(',', '.'));
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Введите числовой рейтинг");
        }
    }

    @FXML
    private void onAdd() {
        try {
            service.addFilm(txtTitle.getText(), txtGenre.getText(), readRating());
            refreshGenres();
            applyFilter();
            clearFields();
        } catch (IllegalArgumentException e) { showError(e.getMessage()); }
    }

    @FXML
    private void onUpdate() {
        try {
            Film f = tableFilms.getSelectionModel().getSelectedItem();
            service.updateFilm(f, txtTitle.getText(), txtGenre.getText(), readRating());
            refreshGenres();
            applyFilter();
        } catch (IllegalArgumentException e) { showError(e.getMessage()); }
    }

    @FXML
    private void onDelete() {
        try {
            Film f = tableFilms.getSelectionModel().getSelectedItem();
            if (f == null) throw new IllegalArgumentException("Фильм не выбран");
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Удалить фильм «" + f.getTitle() + "»?", ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Подтверждение удаления");
            confirm.setHeaderText(null);
            if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;
            service.deleteFilm(f);
            refreshGenres();
            applyFilter();
            clearFields();
        } catch (IllegalArgumentException e) { showError(e.getMessage()); }
    }

    private void clearFields() {
        txtTitle.clear(); txtGenre.clear(); txtRating.clear();
        tableFilms.getSelectionModel().clearSelection();
    }

    private void showError(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Ошибка");
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
