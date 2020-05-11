package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.launchcode.watchlist.Models.MovieService;
import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;

    @GetMapping("/{username}")
    public String displayUser(@PathVariable String username, HttpServletRequest request, Model model){
        // Check if provided user exists - if not go to home page?
        User providedUser = userRepository.findByUsername(username);

        if (providedUser == null){
            return "redirect:";
        }

        // Check if provided user matches current user in session - if not, return to current user's page.
        User user = authenticationController.getUserFromSession(request.getSession());

        if (user.getUsername() != providedUser.getUsername()){
            return "redirect:/" + user.getUsername();
        }

        // *** MOCK DATA ***
        MovieService movieService = new MovieService();
        List<MovieDb> movies = movieService.searchMovies("Matrix");
        model.addAttribute("isUserList", true);
        model.addAttribute("url", movieService.getBaseUrl(0));
        model.addAttribute("movies", movies);

        return "user/index";
    }
}
