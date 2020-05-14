package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Models.Movie;
import org.launchcode.watchlist.Models.MovieService;
import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.data.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("movie")
public class MovieController {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    AuthenticationController authenticationController;

    @GetMapping("tmdb/{id}")
    public String viewTmdbMovieDetails(@PathVariable int id, Model model){
        MovieService movieService = new MovieService();
        MovieDb movie = movieService.getMovie(id);

        model.addAttribute("movie", movie);
        model.addAttribute("trailerUrl", movieService.getTrailerUrl(movie));
        model.addAttribute("url", movieService.getBaseUrl(3));
        model.addAttribute("isUserMovie", false);

        return "movie";
    }

    @GetMapping("{id}")
    public String viewWatchlistMovie(@PathVariable int id, HttpServletRequest request, Model model){
        Optional<Movie> result = movieRepository.findById(id);
        MovieService movieService = new MovieService();

        User user = authenticationController.getUserFromSession(request.getSession());

        if (!result.isPresent()){
            return "redirect:";
        }

        Movie movie = result.get();

        if (!user.equals(movie.getUser())){
            return "redirect:";
        }

        model.addAttribute("movie", movie);
        model.addAttribute("trailerUrl", movie.getTrailerUrl());
        model.addAttribute("url", movieService.getBaseUrl(3));
        model.addAttribute("isUserMovie", true);

        return "redirect:";
    }
}
