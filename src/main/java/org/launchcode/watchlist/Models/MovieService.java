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

        for (PersonCrew crewMember : movie.getCrew()){
            if (crewMember.getJob().equals("Director")){
                directors.add(crewMember);
            }
        }

        return directors;
    }

    public List<Video> getTrailers(MovieDb movie){
        List<Video> trailers = new ArrayList<>();

        List<Video> allVideos = movie.getVideos();

        for (Video video : allVideos){
            if (video.getType().equalsIgnoreCase("trailer")){
                trailers.add(video);
            }
        }

        return trailers;
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
        List<MovieDb> movies = search.searchMovie(searchTerm, null, "en", false, 1).getResults();

        for (MovieDb movie : movies){
            movie.setCredits(tmdbMovies.getCredits(movie.getId()));
            movie.getCredits().setCrew(getDirectors(movie));
            movie.setReleaseDate(getReleaseDateYearForDisplay(movie));

            //movie.setVideos((Video.Results) getTrailers(movie)); -- I need to debug why this causes an error
        }

        return movies;
    }

}
