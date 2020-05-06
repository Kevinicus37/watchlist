package org.launchcode.watchlist.Controllers;

import org.launchcode.watchlist.Models.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping()
    public String displayLandingPage(Model model) {
        MovieService movieService = new MovieService();

        model.addAttribute("title", "Welcome to Watchlist!");
        model.addAttribute("upcoming", movieService.getComingSoon());
        model.addAttribute("nowPlaying", movieService.getNowPlaying());

        return "home";
    }
}
