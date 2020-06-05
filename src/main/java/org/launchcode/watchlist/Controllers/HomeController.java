package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Models.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    MovieService movieService;

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

        model.addAttribute("title", "Welcome to Watchlist!");
        model.addAttribute("isUserList", false);
        model.addAttribute("upcoming", upcoming.subList(0,upcomingEndIndex));
        model.addAttribute("nowPlaying", nowPlaying.subList(0,nowPlayingEndIndex));

        return "home";
    }
}
