package org.launchcode.watchlist.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping()
    public String displayLandingPage(Model model) {
        model.addAttribute("title", "Welcome to Watchlist!");
        return "home";
    }
}
