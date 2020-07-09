package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Services.MovieService;
import org.launchcode.watchlist.Models.dto.HomePageDTO;
import org.launchcode.watchlist.data.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController extends AbstractBaseController{

    @Autowired
    MovieService movieService;

    @Autowired ApiKeyRepository apiKeyRepository;

    @GetMapping()
    public String displayLandingPage(Model model) {

        List<MovieDb> upcoming = movieService.getComingSoon();
        List<MovieDb> nowPlaying = movieService.getNowPlaying();

        int upcomingEndIndex = upcoming.size();
        int nowPlayingEndIndex = nowPlaying.size();

        if (upcomingEndIndex > 10){
            upcomingEndIndex = 10;
        }

        if (nowPlayingEndIndex > 10){
            nowPlayingEndIndex = 10;
        }

        HomePageDTO dto = new HomePageDTO();
        dto.setNowPlaying(nowPlaying.subList(0, nowPlayingEndIndex));
        dto.setUpcoming(upcoming.subList(0,upcomingEndIndex));
        dto.setUserList(false);

        model.addAttribute("title", "Welcome to Watchlist!");
        model.addAttribute("dto", dto);

        return "home";
    }
}
