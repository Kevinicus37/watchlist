package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Models.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("search")
@Controller
public class SearchController extends AbstractBaseController{

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    MovieService movieService;

    @GetMapping
    public String displaySearchForm(HttpServletRequest request, Model model){
        model.addAttribute("title", "Search TMDb.org by:");
        return "/search/tmdbsearch";
    }

    @PostMapping
    public String processSearchForm(String searchTerm, String searchOption, Model model){
        List<MovieDb> movies = new ArrayList<>();

        if (searchOption != null && searchOption.equals("cast")){
            List<Integer> castIds = movieService.searchForCastMember(searchTerm);
            if (castIds.size() > 0){
                movies = movieService.searchForMovieDbByCastMember(castIds.get(0));
            }
        }
        else {
            movies = movieService.searchMovies(searchTerm);
        }

        model.addAttribute("movies", movies);
        model.addAttribute("url", movieService.getBaseUrl(0));
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("isUserList", false);
        model.addAttribute("title", "Search TMDB.org by:");

        return "/search/tmdbsearch";
    }

    @GetMapping("cast/{castId}")
    public String getMovieDbsByCastMember(@PathVariable int castId, Model model){
        // castId should be the id for the cast Member on TMDb.org
        model.addAttribute("movies", movieService.searchForMovieDbByCastMember(castId));
        model.addAttribute("url", movieService.getBaseUrl(0));
        model.addAttribute("isUserList", false);

        return "/search/tmdbsearch";
    }
}
