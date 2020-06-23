package org.launchcode.watchlist.Models;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.ReleaseDate;
import info.movito.themoviedbapi.model.ReleaseInfo;
import info.movito.themoviedbapi.model.Video;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.people.PersonCrew;
import org.launchcode.watchlist.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MovieService {

    private static String key;

    private static String apiBaseUrl = "https://api.themoviedb.org/3/";
    private static String options = "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false";

    private TmdbMovies tmdbMovies;
    private TmdbSearch search;
    private TmdbDiscover discover;

    @Autowired
    DirectorRepository directorRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CastMemberRepository castMemberRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    public MovieService(){}

    @PostConstruct
    private void postConstruct(){
        ApiKey apiKey = apiKeyRepository.findByName("tmdb");
        this.key = apiKey.getValue();

        TmdbApi api = new TmdbApi(this.key);
        this.tmdbMovies = api.getMovies();
        this.search = api.getSearch();
        this.discover = api.getDiscover();
    }

     // MovieDb specific methods

    public MovieResultsPage searchForMovieDbByCastMember(int id){
        return searchForMovieDbByCastMember(id, 1);
    }

    public MovieResultsPage searchForMovieDbByCastMember(int id, int page){

        if (page < 1){
            page = 1;
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = apiBaseUrl + "discover/movie?api_key=" + key + options + "&page=" + page + "&with_cast=" + id;
        String response = restTemplate.getForObject(url, String.class);

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        //JsonArray jArr = jobj.getAsJsonArray("results");
        //Type castType = new TypeToken<MovieResultsPage>(){}.getType();
        //MovieResultsPage results = new Gson().fromJson(jobj, castType);
        MovieResultsPage results = new Gson().fromJson(jobj.toString(), MovieResultsPage.class);
        results.setTotalPages(new Gson().fromJson(jobj.get("total_pages"), Integer.class));
        results.setTotalResults(new Gson().fromJson(jobj.get("total_results"), Integer.class));
//        Type listType = new TypeToken<List<MovieDb>>(){}.getType();
//        List<MovieDb> movies = new Gson().fromJson(jArr, listType);
//
//        List<MovieDb> output = new ArrayList<>();
//
//        for (MovieDb movie : movies) {
//            MovieDb newMovie = getTmdbMovie(movie.getId());
//            setDirectors(newMovie);
//            newMovie.setReleaseDate(getReleaseDateYearForDisplay(newMovie.getReleaseDate()));
//            output.add(newMovie);
//        }

        return results;
    }

    public List<Integer> searchForCastMember(String searchTerm){
        List<Integer> output = new ArrayList<>();
        List<Person> personList = search.searchPerson(searchTerm,false, 0).getResults();
        for (Person person : personList){
            if (person.getId() > 0){
                output.add(person.getId());
            }
        }

        return output;
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
        // Sets the Crew to only the Directors

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

    public List<MovieDb> searchMovies(String searchTerm) {
        return getResultsFromPage(getSearchResultsPage(searchTerm));
    }

    public MovieDb getTmdbMovie(int id){
        MovieDb movie =  tmdbMovies.getMovie(id,
                "en",
                TmdbMovies.MovieMethod.credits,
                TmdbMovies.MovieMethod.videos,
                TmdbMovies.MovieMethod.release_dates,
                TmdbMovies.MovieMethod.releases);
        removeNonTrailers(movie);

        return movie;
    }

    public String getTrailerUrl(MovieDb movie){
        if (movie.getVideos().size() < 1){
            return null;
        }

        removeNonTrailers(movie);

        String videoKey = movie.getVideos().get(movie.getVideos().size() -1).getKey();

        return "https://www.youtube.com/embed/" + videoKey;
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
            MovieDb newMovie = getTmdbMovie(movie.getId());
            setDirectors(newMovie);
            newMovie.setReleaseDate(getReleaseDateYearForDisplay(newMovie.getReleaseDate()));
            movies.add(newMovie);
        }

        return movies;
    }

    public int getSearchResultsPageCount(String searchTerm){
        return getSearchResultsPage(searchTerm).getTotalPages();
    }

    public int getTotalResultsCount(String searchTerm, int page){
        return getSearchResultsPage(searchTerm, page).getTotalResults();
    }

    public String getFormattedReleaseDate(MovieDb movie){
        String date = movie.getReleaseDate();

        return getFormattedDate(date);
    }

    // TMDB.org Utility Methods

    public String getBaseUrl(int size) {
        // sizes 0 = w92, 1 = w154, 2 = w185, 3 = w342, 4 = w500, 5 = w780, 6 = original

        if (size < 0 || size > 6){
            size = 0;
        }

        String baseUrl = new TmdbApi(key).getConfiguration().getBaseUrl();
        String sizeUrl = new TmdbApi(key).getConfiguration().getPosterSizes().get(size);

        return baseUrl + sizeUrl;
    }

    public List<MovieDb> getNowPlaying(int page){
        if (page < 1){
            page = 1;
        }

        List<MovieDb> movies = tmdbMovies.getNowPlayingMovies("en", page, "US").getResults();
        List<MovieDb> output = new ArrayList<>();
        String dateStr = getCurrentDateFormatted();

        for (MovieDb movie : movies){
            if (movie.getReleaseDate().compareTo(dateStr) <= 0){
                movie.setReleaseDate(getFormattedReleaseDate(movie));
                output.add(movie);
            }
        }

        output.sort(new SortMovieDbByDateDesc());

        return output;
    }

    public List<MovieDb> getNowPlaying(){
        return getNowPlaying(1);
    }

    public List<MovieDb> getComingSoon(int page){

        if (page < 1){
            page = 1;
        }

        List<MovieDb> movies = tmdbMovies.getUpcoming("en", page, "US").getResults();
        for (MovieDb movie : movies){
            movie.setReleaseDate(getFormattedReleaseDate(movie));
        }

        movies.sort(new SortMovieDbByDate());

        return movies;
    }

    public List<MovieDb> getComingSoon(){
        return getComingSoon(1);
    }

    // Movie methods

    public List<Movie> getUnreleasedMovies(List<Movie> movies){
        List<Movie> output = new ArrayList<>();
        String currentDate = getCurrentDateFormatted();

        for (Movie movie : movies){
            String sortDate = getSortByDate(movie.getReleaseDate());
            if (sortDate.compareTo(currentDate) > 0){
                output.add(movie);
            }
        }

        return output;
    }

    public List<Movie> getWatchlistUpcoming(List<Movie> movies){
        int startIndex = 0;
        int endIndex = 10;
        List<Movie> output = getUnreleasedMovies(movies);
        output.sort(new SortMovieByDate());

        if (output.size() < endIndex){
            endIndex = output.size();
        }

        return output.subList(startIndex,endIndex);
    }

    public Movie convertFromMovieDb(MovieDb tmdbMovie){
        if (tmdbMovie == null){
            return null;
        }

        Movie movie = new Movie();
        movie.setTitle(tmdbMovie.getTitle());
        movie.setReleaseDate(getFormattedReleaseDate(tmdbMovie));
        movie.setDirectors(getDirectorsFromMovieDb(tmdbMovie));
        movie.setCast(getCastFromMovieDb(tmdbMovie));
        movie.setRuntime(tmdbMovie.getRuntime());
        movie.setPosterPath(tmdbMovie.getPosterPath());
        movie.setTrailerUrl(getTrailerUrl(tmdbMovie));
        movie.setTmdbId(tmdbMovie.getId());
        movie.setOverview(tmdbMovie.getOverview());
        movie.setTmdbId(tmdbMovie.getId());
        movie.setGenres(getGenresFromMovieDb(tmdbMovie));

        return movie;
    }

    private List<Director> getDirectorsFromMovieDb(MovieDb tmdbMovie){
        if (tmdbMovie == null){
            return null;
        }

        List<Director> directors = new ArrayList<>();

        if (tmdbMovie.getCrew() != null) {
            for (PersonCrew crewMember : tmdbMovie.getCrew()) {
                if (crewMember.getJob().equals("Director")) {

                    Director director= directorRepository.findByName(crewMember.getName());

                    if (director == null){
                        director = new Director(crewMember.getName());
                        directorRepository.save(director);
                    }

                    directors.add(director);
                }
            }
        }

        return directors;
    }

    private List<CastMember> getCastFromMovieDb(MovieDb tmdbMovie){
        if (tmdbMovie == null){
            return null;
        }

        List<CastMember> cast = new ArrayList<>();

        if (tmdbMovie.getCast() != null) {
            for (PersonCast castMember : tmdbMovie.getCast()) {

                CastMember newMember = castMemberRepository.findByCastId(castMember.getId());

                if (newMember == null){
                    newMember = new CastMember(castMember.getName(), castMember.getId());
                    castMemberRepository.save(newMember);
                }
                cast.add(newMember);
            }
        }

        return cast;
    }

    private List<Genre> getGenresFromMovieDb(MovieDb tmdbMovie){
        List<Genre> genres = new ArrayList<>();

        for (info.movito.themoviedbapi.model.Genre genre: tmdbMovie.getGenres()){
            Genre newGenre = genreRepository.findByName(genre.getName());

            if (newGenre == null){
                newGenre = new Genre(genre.getName());
                genreRepository.save(newGenre);
            }
            genres.add(newGenre);
        }

        return genres;
    }

    public List<Movie> getMoviesFromTitleSearch(List<Movie> movies, String searchTerm){
        List<Movie> output = new ArrayList<>();

        for (Movie movie : movies){
            if (movie.getTitle().toLowerCase().contains(searchTerm.toLowerCase())){
                output.add(movie);
            }
        }

        return output;
    }

    public void setUpcomingWatchlistHomeReleases(List<Movie> watchlistMovies){
        for (Movie movie : watchlistMovies){

           List<ReleaseDate> dates = getReleaseDates(movie);
           for (ReleaseDate date : dates) {
               if (date.getType().equals("4")) {
                   movie.setDigitalReleaseDate(getFormattedDate(date.getReleaseDate()));
               }
               if (date.getType().equals("5")) {
                   movie.setPhysicalReleaseDate(getFormattedDate(date.getReleaseDate()));
               }
           }
        }
    }

    public void setUpcomingWatchListTheaterRelease(List<Movie> watchlistMovies){
        for (Movie movie : watchlistMovies){
            List<ReleaseDate> releaseDates = getReleaseDates(movie);

                for (ReleaseDate date : releaseDates) {
                    if (date.getType().equals("3")) {
                        movie.setReleaseDate(getFormattedDate(date.getReleaseDate()));
                    }
                }
        }
    }

    public List<ReleaseDate> getReleaseDates(Movie movie){
        List<ReleaseInfo> releases = tmdbMovies.getReleaseInfo(movie.getTmdbId(), "US");

        for (ReleaseInfo info : releases) {
            if (info.getCountry().equals("US")){
                return info.getReleaseDates();
            }
        }

        return null;
    }

    public Sort getSort(String sortOption){
        Boolean isDescending = false;

        if (sortOption == null || sortOption.isBlank()){
            sortOption = "dateAdded";
        }

        if (sortOption.endsWith("Desc")){
            isDescending = true;
            sortOption = sortOption.replace("Desc", "");
        }

        Sort sort = Sort.by(sortOption);
        if (isDescending){
            sort = sort.descending();
        }

        return sort;
    }

//    public void sortMovies(List<Movie> movies, String sortOption){
//        if (sortOption.equals("releaseDate")){
//            sortMoviesByDate(movies);
//        }
//        else if (sortOption.equals("releaseDateDesc")){
//            sortMoviesByDateDesc(movies);
//        }
//        else if (sortOption.equals("dateAdded")){
//            sortMovieByDateAdded(movies);
//        }
//        else if (sortOption.equals("dateAddedDesc")){
//            sortMovieByDateAddedDesc(movies);
//        }
//        else if (sortOption.equals("titleDesc")){
//            sortMovieByTitleDesc(movies);
//        }
//        else {
//            sortMovieByTitle(movies);
//        }
//    }

    public void sortMoviesByDate(List<Movie> movies){
        movies.sort(new SortMovieByDate());
    }

    public void sortMoviesByDateDesc(List<Movie> movies){
        movies.sort(new SortMovieByDateDesc());
    }

    public void sortMovieByTitle(List<Movie> movies){
        movies.sort(new SortMovieByTitle());
    }

    public void sortMovieByTitleDesc(List<Movie> movies){
        movies.sort(new SortMovieByTitleDesc());
    }

    public void sortMovieByDateAdded(List<Movie> movies){
        movies.sort(new SortMovieByDateAdded());
    }

    public void sortMovieByDateAddedDesc(List<Movie> movies){
        movies.sort(new SortMovieByDateAddedDesc());
    }

    public List<Movie> findPaginatedByUserId(int id, int pageNo, int pageSize, Sort sort) {

        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        Page<Movie> pagedResult = movieRepository.findByUserId(id, paging);

        return pagedResult.toList();
    }

    // General Utility methods

    public String getReleaseDateYearForDisplay(String date) {

        if (date != null && !date.isEmpty()){
            return "(" + date.split("-")[0] + ")";
        }

        return "";
    }

    public String getFormattedDate(String dateString){
        truncateDate(dateString);

        if (dateString != null && !dateString.isEmpty()){
            String[] dateParts = dateString.split("-");
            return dateParts[1] + '-' + dateParts[2] + '-' + dateParts[0];
        }

        return dateString;
    }

    public String getSortByDate(String dateString){

        truncateDate(dateString);

        if (dateString != null && !dateString.isEmpty()){
            String[] dateParts = dateString.split("-");
            return dateParts[2] + '-' + dateParts[0] + '-' + dateParts[1];
        }

        return dateString;
    }

    public void truncateDate(String dateString){
        int index = dateString.indexOf('T');

        if (index >= 0){
            dateString = dateString.substring(0,index);
        }

    }

    public String getCurrentDateFormatted(){
        LocalDateTime date = LocalDateTime.now();
        String month = date.getMonthValue() < 10? "0" + date.getMonthValue() : "" + date.getMonthValue();
        String day = date.getDayOfMonth() < 10? "0" + date.getDayOfMonth() : "" + date.getDayOfMonth();
        String dateStr = date.getYear() + "-" + month + "-" + day;

        return dateStr;
    }

    // Comparator Classes

    class SortMovieDbByDate implements Comparator<MovieDb>
    {
        public int compare(MovieDb a, MovieDb b)
        {
            String dateA = getSortByDate(a.getReleaseDate());
            String dateB = getSortByDate(b.getReleaseDate());

            return dateA.compareTo(dateB);
        }
    }

    class SortMovieDbByDateDesc implements Comparator<MovieDb>
    {
        public int compare(MovieDb a, MovieDb b)
        {
            String dateA = getSortByDate(a.getReleaseDate());
            String dateB = getSortByDate(b.getReleaseDate());

            return dateB.compareTo(dateA);
        }
    }

    class SortMovieDbByTitle implements Comparator<MovieDb>{
        public int compare(MovieDb a, MovieDb b){
            return a.getTitle().compareTo(b.getTitle());
        }
    }

    class SortMovieDbByTitleDesc implements Comparator<MovieDb>{
        public int compare(MovieDb a, MovieDb b){
            return b.getTitle().compareTo(a.getTitle());
        }
    }

    class SortMovieByDateDesc implements Comparator<Movie>{
        public int compare(Movie a, Movie b){

            String dateA = getSortByDate(a.getReleaseDate());
            String dateB = getSortByDate(b.getReleaseDate());

            return dateB.compareTo(dateA);
        }
    }

    class SortMovieByDate implements Comparator<Movie>{
        public int compare(Movie a, Movie b){

            String dateA = getSortByDate(a.getReleaseDate());
            String dateB = getSortByDate(b.getReleaseDate());

            return dateA.compareTo(dateB);
        }
    }

    class SortMovieByTitle implements Comparator<Movie>{
        public int compare(Movie a, Movie b){
            return a.getTitle().compareTo(b.getTitle());
        }
    }

    class SortMovieByTitleDesc implements Comparator<Movie>{
        public int compare(Movie a, Movie b){
            return b.getTitle().compareTo(a.getTitle());
        }
    }

    class SortMovieByDateAdded implements Comparator<Movie>{
        public int compare(Movie a, Movie b){
            return a.getDateAdded().compareTo(b.getDateAdded());
        }
    }

    class SortMovieByDateAddedDesc implements Comparator<Movie>{
        public int compare(Movie a, Movie b){
            return b.getDateAdded().compareTo(a.getDateAdded());
        }
    }

}
