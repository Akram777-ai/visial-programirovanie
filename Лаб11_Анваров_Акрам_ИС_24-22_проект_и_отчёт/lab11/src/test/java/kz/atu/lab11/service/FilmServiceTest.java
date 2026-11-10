package kz.atu.lab11.service;

import kz.atu.lab11.model.Film;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {

    @Test void shouldAddFilm() {
        FilmService s = new FilmService();
        s.addFilm("Начало", "Фантастика", 8.8);
        assertEquals(1, s.getFilms().size());
    }

    @Test void shouldTrimTitleAndGenre() {
        FilmService s = new FilmService();
        s.addFilm("  Начало ", " Фантастика  ", 8.8);
        assertEquals("Начало", s.getFilms().get(0).getTitle());
        assertEquals("Фантастика", s.getFilms().get(0).getGenre());
    }

    @Test void shouldRejectInvalidRating() {
        FilmService s = new FilmService();
        assertThrows(IllegalArgumentException.class, () -> s.addFilm("Начало", "Фантастика", 11.0));
        assertThrows(IllegalArgumentException.class, () -> s.addFilm("Начало", "Фантастика", -0.1));
        assertThrows(IllegalArgumentException.class, () -> s.addFilm("Начало", "Фантастика", Double.NaN));
    }

    @Test void shouldRejectBlankTitleAndGenre() {
        FilmService s = new FilmService();
        assertThrows(IllegalArgumentException.class, () -> s.addFilm("  ", "Драма", 5));
        assertThrows(IllegalArgumentException.class, () -> s.addFilm("Фильм", "", 5));
    }

    @Test void shouldRejectDuplicate() {
        FilmService s = new FilmService();
        s.addFilm("Начало", "Фантастика", 8.8);
        assertThrows(IllegalArgumentException.class, () -> s.addFilm("начало", "фантастика", 7.0));
    }

    @Test void shouldUpdateFilm() {
        FilmService s = new FilmService();
        s.addFilm("Начало", "Фантастика", 8.8);
        Film f = s.getFilms().get(0);
        s.updateFilm(f, "Начало (2010)", "Триллер", 9.0);
        assertEquals("Начало (2010)", f.getTitle());
        assertEquals(9.0, f.getRating());
    }

    @Test void shouldAllowUpdateWithoutChangingKey() {
        FilmService s = new FilmService();
        s.addFilm("Начало", "Фантастика", 8.8);
        Film f = s.getFilms().get(0);
        assertDoesNotThrow(() -> s.updateFilm(f, "Начало", "Фантастика", 9.1));
    }

    @Test void shouldFailUpdateOrDeleteWithoutSelection() {
        FilmService s = new FilmService();
        assertThrows(IllegalArgumentException.class, () -> s.updateFilm(null, "A", "B", 5));
        assertThrows(IllegalArgumentException.class, () -> s.deleteFilm(null));
    }

    @Test void shouldDeleteFilm() {
        FilmService s = new FilmService();
        s.addFilm("Начало", "Фантастика", 8.8);
        s.deleteFilm(s.getFilms().get(0));
        assertTrue(s.getFilms().isEmpty());
    }

    @Test void shouldCalculateAverageRating() {
        FilmService s = new FilmService();
        assertEquals(0.0, s.averageRating());
        s.addFilm("A", "Драма", 8.0);
        s.addFilm("B", "Драма", 6.0);
        assertEquals(7.0, s.averageRating(), 1e-9);
    }

    @Test void shouldBuildDynamicGenreList() {
        FilmService s = new FilmService();
        s.addFilm("A", "Драма", 5);
        s.addFilm("B", "Комедия", 5);
        s.addFilm("C", "драма", 5);
        assertEquals(2, s.getGenres().size()); // «Драма» и «драма» — один жанр
        assertTrue(s.getGenres().contains("Комедия"));
    }
}
