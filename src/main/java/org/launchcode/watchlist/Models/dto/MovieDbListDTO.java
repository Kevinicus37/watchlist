package org.launchcode.watchlist.Models.dto;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Models.Movie;

import java.util.List;

public class MovieDbListDTO extends AbstractMovieListDTO {

    private List<MovieDb> movies;

    private List<Movie> upcoming;

    private final int firstPage = 0;

    public List<MovieDb> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieDb> movies) {
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
