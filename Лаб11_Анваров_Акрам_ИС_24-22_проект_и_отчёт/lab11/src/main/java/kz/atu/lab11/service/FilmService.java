package kz.atu.lab11.service;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kz.atu.lab11.model.Film;

import java.util.List;
import java.util.TreeSet;

/** Сервисный слой: вся бизнес-логика каталога фильмов (без классов GUI). */
public class FilmService {
    public static final double MIN_RATING = 0.0;
    public static final double MAX_RATING = 10.0;

    // extractor: список сообщает об изменении полей элемента (важно для FilteredList)
    private final ObservableList<Film> films = FXCollections.observableArrayList(
            f -> new Observable[]{f.titleProperty(), f.genreProperty(), f.ratingProperty()});

    public ObservableList<Film> getFilms() { return films; }

    public void addFilm(String title, String genre, double rating) {
        validate(title, genre, rating);
        checkDuplicate(title, genre, null);
        films.add(new Film(title.trim(), genre.trim(), rating));
    }

    public void updateFilm(Film film, String title, String genre, double rating) {
        if (film == null) throw new IllegalArgumentException("Фильм не выбран");
        validate(title, genre, rating);
        checkDuplicate(title, genre, film);
        film.setTitle(title.trim());
        film.setGenre(genre.trim());
        film.setRating(rating);
    }

    public void deleteFilm(Film film) {
        if (film == null) throw new IllegalArgumentException("Фильм не выбран");
        films.remove(film);
    }

    /** Средний рейтинг всех фильмов каталога (0, если каталог пуст). */
    public double averageRating() {
        return films.stream().mapToDouble(Film::getRating).average().orElse(0.0);
    }

    /** Динамический список жанров, сформированный из данных каталога. */
    public List<String> getGenres() {
        TreeSet<String> genres = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (Film f : films) genres.add(f.getGenre());
        return List.copyOf(genres);
    }

    private void validate(String title, String genre, double rating) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Введите название фильма");
        if (genre == null || genre.isBlank()) throw new IllegalArgumentException("Введите жанр");
        if (!Double.isFinite(rating) || rating < MIN_RATING || rating > MAX_RATING)
            throw new IllegalArgumentException("Рейтинг должен быть от 0 до 10");
    }

    /** Запрет дублей по паре «название + жанр»; редактируемая запись игнорируется. */
    private void checkDuplicate(String title, String genre, Film except) {
        String t = title.trim(), g = genre.trim();
        for (Film f : films) {
            if (f != except && f.getTitle().equalsIgnoreCase(t) && f.getGenre().equalsIgnoreCase(g))
                throw new IllegalArgumentException("Такой фильм уже есть в каталоге");
        }
    }
}
