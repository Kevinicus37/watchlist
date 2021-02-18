package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.launchcode.watchlist.Models.dto.PersonListDTO;
import org.launchcode.watchlist.Services.MovieService;
import org.launchcode.watchlist.Services.PagingService;
import org.launchcode.watchlist.data.CastMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;

@Controller
@RequestMapping("person")
public class PersonController extends AbstractBaseController {

    @Autowired
    MovieService movieService;

    @Autowired
    PagingService pagingService;

    @Autowired
    CastMemberRepository castMemberRepository;

    @GetMapping("view/{tmdbId}")
    public String getPersonMovies(@PathVariable int tmdbId,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue="20") int size,
                                  @RequestParam(defaultValue = "cast") String creditType,
                                  Model model){

        MovieResultsPage results;
        results = getMovieResultsPage(creditType, tmdbId, page, size);
        results.getResults().sort(Comparator.comparing(MovieDb::getPopularity).reversed());

        PersonListDTO dto = createDTOFromResults(tmdbId, results, page, size);
        model.addAttribute("dto", dto);

        return "/person/view";
    }

    @PostMapping("view/{tmdbId}")
    public String processSearch(@PathVariable int tmdbId, @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size,
                                @RequestParam(defaultValue = "cast") String creditType,
                                Model model){

        MovieResultsPage results;
        results = getMovieResultsPage(creditType, tmdbId, page, size);
        results.getResults().sort(Comparator.comparing(MovieDb::getPopularity).reversed());

        PersonListDTO dto = createDTOFromResults(tmdbId, results, page, size);
        model.addAttribute("dto", dto);

        return "/person/view";
    }


    private PersonListDTO createDTOFromResults(int tmdbId, MovieResultsPage results, int page, int size){
        PersonListDTO dto = new PersonListDTO();

        dto.setMovieCount(results.getTotalResults());
        dto.setPages(results.getTotalPages());
        dto.setPageNumbers(pagingService.getDisplayedPageNumbers(page, results.getTotalPages()));
        dto.setMovies(movieService.getResultsFromPage(results));
        dto.setCurrentPage(page);
        dto.setPerson(movieService.getProductionMemberByTmdbId(tmdbId));
        dto.setFormAction("/person/view/" + tmdbId); // TODO - set as default on model
        dto.setFirstElement((page * size) + 1);
        dto.setUrl(movieService.getBaseUrl(0)); // TODO - Can this be sped up? -
        dto.setProfileUrl(movieService.getBaseUrl(3));
        dto.setUserList(false);
        dto.setPreviousSize(size);

        return dto;
    }

    private MovieResultsPage getMovieResultsPage(String creditType, int tmdbId, int page, int size){
        MovieResultsPage results = new MovieResultsPage();

        if (creditType.equals("director")){
            results = movieService.searchForMovieDbsByDirector(tmdbId, page+1, size);
        }
        else {
            results = movieService.searchForMovieDbsByCastMember(tmdbId, page + 1);
        }

        return results;
    }

}
