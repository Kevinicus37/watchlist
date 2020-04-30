package org.launchcode.watchlist.Controllers;

import org.launchcode.watchlist.Models.MovieService;
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

        MovieService movieService = new MovieService();
        model.addAttribute("movies", movieService.searchMovies(searchTerm));
        model.addAttribute("url", movieService.getBaseUrl(0));

        return "/search/tmdbsearch";
    }
}
