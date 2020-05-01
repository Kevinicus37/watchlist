package org.launchcode.watchlist.Models;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video;
import info.movito.themoviedbapi.model.people.PersonCrew;

import java.util.ArrayList;
import java.util.List;

public class MovieService {

    // Need to make this secure.
    private final static String key = "9950b6bfd3eef8b5c9b7343ead080098";

    private TmdbMovies tmdbMovies = new TmdbApi(key).getMovies();
    private TmdbSearch search = new TmdbApi(key).getSearch();

    public MovieService(){
    }

    public List<PersonCrew> getDirectors(MovieDb movie){
        List<PersonCrew> directors = new ArrayList<>();

        if (movie.getCrew() != null) {
            for (PersonCrew crewMember : movie.getCrew()) {
                if (crewMember.getJob().equals("Director")) {
                    directors.add(crewMember);
                }
            }
        }
        return directors;
    }

    public void removeNonTrailers(MovieDb movie){
        List<Video> videos = movie.getVideos();

        if (videos == null){
            return;
        }
        videos.clear();

        for (Video video : tmdbMovies.getVideos(movie.getId(), "en")){
            if (video.getType().equalsIgnoreCase("trailer")){
                videos.add(video);
            }
        }
    }

    public String getReleaseDateYearForDisplay(MovieDb movie) {

        String date = movie.getReleaseDate();

        if (date != null && !date.isEmpty()){
             return "(" + date.split("-")[0] + ")";
        }

        return "";
    }

    public String getBaseUrl(int size) {

        // sizes 0 =

        String baseUrl = new TmdbApi(key).getConfiguration().getBaseUrl();
        String sizeUrl = new TmdbApi(key).getConfiguration().getPosterSizes().get(size);

        return baseUrl + sizeUrl;
    }

    public List<MovieDb> searchMovies(String searchTerm) {
        List<MovieDb> searchResults = search.searchMovie(searchTerm, null, "en", false, 1).getResults();
        List<MovieDb> movies = new ArrayList<>();

        for (MovieDb movie : searchResults){
            MovieDb newMovie = tmdbMovies.getMovie(movie.getId(), "en", TmdbMovies.MovieMethod.credits, TmdbMovies.MovieMethod.videos);
            newMovie.getCredits().setCrew(getDirectors(newMovie));
            newMovie.setReleaseDate(getReleaseDateYearForDisplay(newMovie));
            removeNonTrailers(newMovie);
            movies.add(newMovie);
        }

        return movies;
    }

}
