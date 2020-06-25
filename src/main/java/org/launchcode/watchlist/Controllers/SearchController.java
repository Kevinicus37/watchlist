package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.launchcode.watchlist.Models.MovieService;
import org.launchcode.watchlist.Models.dto.MovieDbListDTO;
import org.launchcode.watchlist.Services.PagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequestMapping("search")
@Controller
public class SearchController extends AbstractBaseController{

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    MovieService movieService;

    @Autowired
    PagingService pagingService;

    @GetMapping
    public String displaySearchForm(HttpServletRequest request, Model model){
        model.addAttribute("title", "Search TMDb.org by:");
        model.addAttribute("dto", new MovieDbListDTO());

        return "/search/tmdbsearch";
    }

    @PostMapping
    public String processSearchForm(@ModelAttribute MovieDbListDTO dto,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size,
                                    Model model){

        List<MovieDb> movies = new ArrayList<>();
        MovieResultsPage results = new MovieResultsPage();

        if (dto.getSearchOption() != null && dto.getSearchOption().equals("cast")){

            List<Integer> castIds = movieService.searchForCastMember(dto.getSearchTerm());
            if (castIds.size() > 0){
                results = movieService.searchForMovieDbByCastMember(castIds.get(0), page + 1);
            }
        }
        else {
            results = movieService.getSearchResultsPage(dto.getSearchTerm(), page + 1);
        }

        movies = movieService.getResultsFromPage(results);
        dto.setMovieCount(results.getTotalResults());
        dto.setPages(results.getTotalPages());
        dto.setPageNumbers(pagingService.getDisplayedPageNumbers(page, results.getTotalPages()));
        dto.setMovies(movies);
        dto.setCurrentPage(page);
        dto.setFirstElement((page * size) + 1);
        dto.setUrl(movieService.getBaseUrl(0));
        dto.setUserList(false);

        model.addAttribute("dto", dto);
        model.addAttribute("title", "Search TMDB.org by:");

        return "/search/tmdbsearch";
    }

//    Was used for testing
//    @GetMapping("cast/{castId}")
//    public String getMovieDbsByCastMember(@PathVariable int castId, Model model){
//        // castId should be the id for the cast Member on TMDb.org
//        model.addAttribute("movies", movieService.searchForMovieDbByCastMember(castId));
//        model.addAttribute("url", movieService.getBaseUrl(0));
//        model.addAttribute("isUserList", false);
//
//        return "/search/tmdbsearch";
//    }
//
}
