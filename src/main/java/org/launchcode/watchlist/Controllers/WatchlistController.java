package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Models.*;
import org.launchcode.watchlist.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
        User user = authenticationController.getUserFromSession(request.getSession());

        // Check to see if user already has this movie in their list.
        if (tmdbMovie != null){
            // TODO a better search here would be for tmdbId instead of title (might not be unique).
            Movie movie = movieRepository.findByTitleAndUserId(tmdbMovie.getTitle(), user.getId());

            if (movie == null) {
               movie = movieService.convertFromMovieDb(tmdbMovie);
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
        User user = authenticationController.getUserFromSession(request.getSession());
        Optional<Movie> result = movieRepository.findById(id);

        if (result.isPresent()){
            Movie movie = result.get();
            if (movie.getUser().equals(user)){
                movieRepository.delete(movie);
            }
        }

        return "redirect:/user/" + user.getUsername();
    }

}
