package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("search")
@Controller
public class SearchController {

    @Autowired
    AuthenticationController authenticationController;

    @GetMapping
    public String displaySearchForm(HttpServletRequest request, Model model){
        model.addAttribute("title", "Search");
        return "/search/tmdbsearch";
    }

    @PostMapping
    public String processSearchForm(String searchTerm, Model model){
        String key = "9950b6bfd3eef8b5c9b7343ead080098";

        String baseUrl = new TmdbApi(key).getConfiguration().getBaseUrl();
        String size = new TmdbApi(key).getConfiguration().getPosterSizes().get(2);

        MovieResultsPage movies = new TmdbApi(key).getSearch().searchMovie(searchTerm,null,"en", false, 1);
        model.addAttribute("url", baseUrl + size + "/");
        model.addAttribute("movies", movies.getResults());
        return "/search/tmdbsearch";
    }

    // Currently for testing
//    @GetMapping("/movie")
//    public String movie(Model model){
//        String key = "9950b6bfd3eef8b5c9b7343ead080098";
//
//        String baseUrl = new TmdbApi(key).getConfiguration().getBaseUrl();
//        String size = new TmdbApi(key).getConfiguration().getPosterSizes().get(2);
//
//        MovieResultsPage movies = new TmdbApi(key).getSearch().searchMovie("matrix",null,"en", false, 1);
//        model.addAttribute("url", baseUrl + size + "/");
//        model.addAttribute("movies", movies.getResults());
//        return "movie";
//    }
}
