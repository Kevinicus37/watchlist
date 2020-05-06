package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Models.MovieService;
import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

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

}
