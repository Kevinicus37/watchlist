package org.launchcode.watchlist.Models.dto;

import org.launchcode.watchlist.Models.Movie;

import java.util.List;

public class MovieListDTO extends AbstractMovieListDTO{

    private List<Movie> movies;

    private List<Movie> upcoming;

    private final int firstPage = 0;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(List<Movie> upcoming) {
        this.upcoming = upcoming;
    }

    public int getFirstPage() {
        return firstPage;
    }
}
