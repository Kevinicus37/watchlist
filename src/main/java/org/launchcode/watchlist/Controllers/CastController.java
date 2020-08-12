package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.launchcode.watchlist.Models.dto.CastListDTO;
import org.launchcode.watchlist.Services.MovieService;
import org.launchcode.watchlist.Services.PagingService;
import org.launchcode.watchlist.data.CastMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("cast")
public class CastController extends AbstractBaseController {

    @Autowired
    MovieService movieService;

    @Autowired
    PagingService pagingService;

    @Autowired
    CastMemberRepository castMemberRepository;

    @GetMapping("view/{tmdbId}")
    public String castInfo(@PathVariable int tmdbId,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "20") int size,
                           Model model){

        MovieResultsPage results = movieService.searchForMovieDbsByCastMember(tmdbId, page + 1);
        CastListDTO dto = new CastListDTO();
        dto.setCast(movieService.getCastMemberByTmdbId(tmdbId));
        updateDTOFromResults(dto, results, page, size);
        dto.setProfileUrl(movieService.getBaseUrl(3));
        dto.setFormAction("/cast/view/" + tmdbId);
        model.addAttribute("dto", dto);

        return "/cast/view";
    }

    public void updateDTOFromResults(CastListDTO dto, MovieResultsPage results, int page, int size){
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
