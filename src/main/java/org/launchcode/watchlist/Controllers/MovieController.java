package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Models.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("movie")
public class MovieController {

    @GetMapping("view")
    public String viewMovieDetails(@RequestParam int id, Model model){
        MovieService movieService = new MovieService();
        MovieDb movie = movieService.getMovie(id);

        // User user = authenticationController.getUserFromSession(request.getSession());

        model.addAttribute("movie", movie);
        model.addAttribute("trailerUrl", movieService.getTrailerUrl(movie));
        model.addAttribute("url", movieService.getBaseUrl(3));

        return "movie";
    }
}
