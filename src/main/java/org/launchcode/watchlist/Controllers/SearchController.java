package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.launchcode.watchlist.Models.dto.MovieByDirectorDTO;
import org.launchcode.watchlist.Services.MovieService;
import org.launchcode.watchlist.Models.dto.MovieDbListDTO;
import org.launchcode.watchlist.Services.PagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        if (size != dto.getPreviousSize()){
            page = 0;
        }

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

        updateDTOFromResults(dto, results, page, size);
        model.addAttribute("dto", dto);
        model.addAttribute("title", "Search TMDB.org by:");

        return "/search/tmdbsearch";
    }

    @GetMapping("director/{tmdbId}")
    public String getDirectorMovies(@PathVariable int tmdbId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   Model model){

        MovieResultsPage results = movieService.searchForMovieDbByDirector(tmdbId, page + 1);
        MovieByDirectorDTO dto = new MovieByDirectorDTO();
        updateDTOFromResults(dto, results, page, size);
        dto.setFormAction("/search/director/" + tmdbId);
        model.addAttribute("dto", dto);

        return "/search/tmdbsearch";
    }

    @PostMapping("director/{tmdbId}")
    public String processDirectorMovies(@PathVariable int tmdbId,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size,
                                    Model model){

        MovieResultsPage results = movieService.searchForMovieDbByDirector(tmdbId, page + 1);
        MovieByDirectorDTO dto = new MovieByDirectorDTO();
        updateDTOFromResults(dto, results, page, size);
        dto.setFormAction("/search/director/" + tmdbId);
        model.addAttribute("dto", dto);

        return "/search/tmdbsearch";
    }

    public void updateDTOFromResults(MovieDbListDTO dto, MovieResultsPage results, int page, int size){
        dto.setMovieCount(results.getTotalResults());
        dto.setPages(results.getTotalPages());
        dto.setPageNumbers(pagingService.getDisplayedPageNumbers(page, results.getTotalPages()));
        dto.setMovies(movieService.getResultsFromPage(results));
        dto.setCurrentPage(page);
        dto.setFirstElement((page * size) + 1);
        dto.setUrl(movieService.getBaseUrl(0)); // TODO - Can this be sped up? -
        dto.setUserList(false);
        dto.setPreviousSize(size);
    }

}
