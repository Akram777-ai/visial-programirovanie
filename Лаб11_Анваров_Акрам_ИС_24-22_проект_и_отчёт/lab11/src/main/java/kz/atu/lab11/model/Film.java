package kz.atu.lab11.model;

import javafx.beans.property.*;

/** Модель данных: фильм каталога. Поля хранятся в JavaFX Properties. */
public class Film {
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty genre = new SimpleStringProperty();
    private final DoubleProperty rating = new SimpleDoubleProperty();

    public Film(String title, String genre, double rating) {
        setTitle(title);
        setGenre(genre);
        setRating(rating);
    }

    public String getTitle() { return title.get(); }
    public void setTitle(String v) { title.set(v); }
    public StringProperty titleProperty() { return title; }

    public String getGenre() { return genre.get(); }
    public void setGenre(String v) { genre.set(v); }
    public StringProperty genreProperty() { return genre; }

    public double getRating() { return rating.get(); }
    public void setRating(double v) { rating.set(v); }
    public DoubleProperty ratingProperty() { return rating; }
}
