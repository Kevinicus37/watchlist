package org.launchcode.watchlist.Models;

import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCrew;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MovieService {

    // Need to make this secure.
    private final static String key = "9950b6bfd3eef8b5c9b7343ead080098";

    private TmdbMovies tmdbMovies = new TmdbApi(key).getMovies();
    private TmdbSearch search = new TmdbApi(key).getSearch();
    private TmdbDiscover discover = new TmdbApi(key).getDiscover();

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

    public void setDirectors(MovieDb movie){
        movie.getCredits().setCrew(getDirectors(movie));
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

    public String formatReleaseDate(MovieDb movie){

        String date = movie.getReleaseDate();

        if (date != null && !date.isEmpty()){
            String[] dateParts = date.split("-");
            dateParts[0] = dateParts[0].substring(2);
            return dateParts[1] + '-' + dateParts[2] + '-' + dateParts[0];
        }

        return date;
    }

    public String getBaseUrl(int size) {
        // sizes 0 = w92, 1 = w154, 2 = w185, 3 = w342, 4 = w500, 5 = w780, 6 = original

        if (size < 0 || size > 6){
            size = 0;
        }

        String baseUrl = new TmdbApi(key).getConfiguration().getBaseUrl();
        String sizeUrl = new TmdbApi(key).getConfiguration().getPosterSizes().get(size);

        return baseUrl + sizeUrl;
    }

    public List<MovieDb> searchMovies(String searchTerm) {
        return getResultsFromPage(getSearchResultsPage(searchTerm));
    }

    public List<MovieDb> getComingSoon(){
        List<MovieDb> movies = tmdbMovies.getUpcoming("en", 1, "US").getResults();
        for (MovieDb movie : movies){
            movie.setReleaseDate(formatReleaseDate(movie));
        }

        return movies;
    }

    public MovieDb getMovie(int id){
        MovieDb movie =  tmdbMovies.getMovie(id,
                "en",
                TmdbMovies.MovieMethod.credits,
                TmdbMovies.MovieMethod.videos,
                TmdbMovies.MovieMethod.release_dates);
        removeNonTrailers(movie);
        movie.setReleaseDate(getReleaseDateYearForDisplay(movie));

        return movie;
    }

    public String getTrailerUrl(MovieDb movie){
        if (movie.getVideos().size() < 1){
            return null;
        }

        String videoKey = movie.getVideos().get(movie.getVideos().size() -1).getKey();

        return "https://www.youtube.com/embed/" + videoKey;
    }

    public int getSearchResultsPages(String searchTerm){
        return getSearchResultsPage(searchTerm).getTotalPages();
    }

    public int getTotalResultsCount(String searchTerm, int page){
        return getSearchResultsPage(searchTerm, page).getTotalResults();
    }

    public MovieResultsPage getSearchResultsPage(String searchTerm, int page){
        return search.searchMovie(searchTerm, null, "en", false, page);
    }

    public MovieResultsPage getSearchResultsPage(String searchTerm){
        return getSearchResultsPage(searchTerm, 1);
    }

    public List<MovieDb> getResultsFromPage(MovieResultsPage resultsPage){
        List<MovieDb> searchResults = resultsPage.getResults();
        List<MovieDb> movies = new ArrayList<>();

        for (MovieDb movie : searchResults){
            MovieDb newMovie = getMovie(movie.getId());
            setDirectors(newMovie);
            movies.add(newMovie);
        }

        return movies;
    }

    public List<MovieDb> getNowPlaying(){
        List<MovieDb> movies = tmdbMovies.getNowPlayingMovies("en", 1, "US").getResults();
        movies.sort(new SortByDate());
        for (MovieDb movie : movies){
            movie.setReleaseDate(formatReleaseDate(movie));
        }
        int movieId = new TmdbApi(key).getPeople().getCombinedPersonCredits(500).getCast().get(0).getId();
        return movies;
    }

    class SortByDate implements Comparator<MovieDb>
    {
        // Used for sorting in descending order of
        // release date
        public int compare(MovieDb a, MovieDb b)
        {
            return b.getReleaseDate().compareTo(a.getReleaseDate());
        }

    }

    /* TODO - Create a movie class that extends MovieDb and move some of the formatting
        functions (like Date) into that class.
     */


}
