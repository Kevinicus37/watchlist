package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Models.*;
import org.launchcode.watchlist.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

        //MovieService movieService = new MovieService();
        MovieDb tmdbMovie = movieService.getMovie(id);
        User user = authenticationController.getUserFromSession(request.getSession());

        if (tmdbMovie != null){

            // Check to see if user already has this movie in their list.
            Movie movie = movieRepository.findByTitleAndUserId(tmdbMovie.getTitle(), user.getId());

            if (movie == null) {
               movie = movieService.convertFromMovieDb(tmdbMovie);

//                for (Director director : movie.getDirectors()) {
//                    if (directorRepository.findByName(director.getName()) == null) {
//                        directorRepository.save(director);
//                    }
//                }
//
//                for (CastMember castMember : movie.getCast()) {
//                    CastMember existing = castMemberRepository.findByCastId(castMember.getCastId());
//
//                    if (existing == null) {
//                        castMemberRepository.save(castMember);
//                    }
//                    else{
//                        castMember = existing;
//                    }
//                }
//
//                for (Genre genre : movie.getGenres()) {
//                    Genre existingGenre = genreRepository.findByName(genre.getName());
//
//                    if (existingGenre == null) {
//                        genreRepository.save(genre);
//                    }
//                    else{
//                        genre = existingGenre;
//                    }
//                }

                movie.setUser(user);
                movieRepository.save(movie);
            }

            model.addAttribute("movie", movie);
            model.addAttribute("trailerUrl", movie.getTrailerUrl());
            model.addAttribute("url", movieService.getBaseUrl(3));
            model.addAttribute("isUserMovie", true);

            return "movie";
        }

        return "redirect:/user/" + user.getUsername();
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
