package org.launchcode.watchlist.Controllers;

import org.launchcode.watchlist.Models.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    MovieService movieService;

    @GetMapping()
    public String displayLandingPage(Model model) {
        model.addAttribute("title", "Welcome to Watchlist!");
        model.addAttribute("isUserList", false);
        model.addAttribute("upcoming", movieService.getComingSoon());
        model.addAttribute("nowPlaying", movieService.getNowPlaying());

        return "home";
    }
}
