package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Models.MovieService;
import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("watchlist")
public class WatchlistController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;

    public String addMovie(@RequestParam int id, HttpServletRequest request, Model model){

        MovieService movieService = new MovieService();
        MovieDb movie = movieService.getMovie(id);

        // User user = authenticationController.getUserFromSession(request.getSession());

        model.addAttribute("movie", movie);
        model.addAttribute("title", "Movie Details");
        model.addAttribute("trailerUrl", movieService.getTrailerUrl(movie));
        model.addAttribute("url", movieService.getBaseUrl(3));

        return "movie";
    }

    @PostMapping("search")
    public String searchWatchlist(String searchTerm, HttpServletRequest request, Model model){

        MovieService movieService = new MovieService();

        User user = authenticationController.getUserFromSession(request.getSession());

        List<MovieDb> movies = new ArrayList<>();

        MovieDb movie = new MovieDb();
        movie.setTitle("This is my Movie");
        movie.setReleaseDate("6-6-20");
        movie.setOverview("This is a made up movie, used for mock data.");
        movie.setPosterPath("oHg5SJYRHA0");
        movies.add(movie);

        movie = new MovieDb();
        movie.setTitle("This is my other Movie");
        movie.setReleaseDate("8-20-20");
        movie.setOverview("This is another made up movie, used for mock data.");
        movie.setPosterPath("oHg5SJYRHA0");
        movies.add(movie);

        model.addAttribute("movies", movies);
        model.addAttribute("isUserList", true);
        model.addAttribute("url", movieService.getBaseUrl(0));
        return "/user/index";
    }

}
