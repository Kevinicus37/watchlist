package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Models.*;
import org.launchcode.watchlist.Services.MovieService;
import org.launchcode.watchlist.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("watchlist")
public class WatchlistController extends AbstractBaseController{

    @Autowired
    UserRepository userRepository;

    @Autowired
    DirectorRepository directorRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CastMemberRepository castMemberRepository;

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    MovieService movieService;

    @GetMapping("add")
    public String addMovie(@RequestParam int id, HttpServletRequest request, Model model){

        MovieDb tmdbMovie = movieService.getTmdbMovie(id);
        User user = (User) model.getAttribute("user");

        // Check to see if user already has this movie in their list.
        if (tmdbMovie != null){
            Movie movie = movieRepository.findByTmdbIdAndUserId(tmdbMovie.getId(), user.getId());

            if (movie == null) {
               movie = movieService.createMovieFromMovieDb(tmdbMovie);
               movie.setUser(user);
               movieRepository.save(movie);
            }

            model.addAttribute("movie", movie);
            model.addAttribute("trailerUrl", movie.getTrailerUrl());
            model.addAttribute("url", movieService.getBaseUrl(3));
            model.addAttribute("isUserMovie", true);

            return "movie/movie";
        }

        return "redirect:/user/" + user.getUsername();
    }

    @GetMapping("remove")
    public String removeFromWatchlist(@RequestParam int id, HttpServletRequest request, Model model){
        User user = (User) model.getAttribute("user");
        Optional<Movie> result = movieRepository.findById(id);

        if (result.isPresent()){
            Movie movie = result.get();
            if (movie.getUser().equals(user)){
                movieRepository.delete(movie);
            }
        }

        return "redirect:/user/" + user.getUsername();
    }

    @GetMapping("update")
    public String updateWatchListMovie(@RequestParam int id, HttpServletRequest request, Model model){
        User user = (User) model.getAttribute("user");
        Optional<Movie> result = movieRepository.findById(id);

        if (result.isPresent()){
            Movie movie = result.get();
            if (movie.getUser().equals(user)){
                movieService.updateMovieFromTMDB(movie);
            }
        }

        return "redirect:/user/" + user.getUsername();
    }

    @GetMapping("fullUpdate")
    public String updateWatchlist(Model model){
        User user = (User) model.getAttribute("user");
        List<Movie> movies = user.getWatchlist();
        movieService.updateMovieList(movies);

        return "redirect:/user/" + user.getUsername();
    }

}
