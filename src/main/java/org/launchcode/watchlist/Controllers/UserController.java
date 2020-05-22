package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.model.MovieDb;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.launchcode.watchlist.Models.Movie;
import org.launchcode.watchlist.Models.MovieService;
import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.Models.UserService;
import org.launchcode.watchlist.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    MovieService movieService;

    @Autowired
    UserService userService;

    @GetMapping("/{username}")
    public String displayUser(@PathVariable String username, HttpServletRequest request, Model model){

        // Check if provided user exists - if not go to home page?
        User providedUser = userRepository.findByUsername(username);

        if (providedUser == null){
            return "redirect:";
        }

        List<Movie> movies = providedUser.getWatchlist();
        model.addAttribute("isUserList", true);
        model.addAttribute("url", movieService.getBaseUrl(0));
        model.addAttribute("movies", movies);

        return "user/index";
    }

    @GetMapping("manage")
    public String displayManagePage(HttpServletRequest request, Model model){

        return "user/manage";
    }

    @PostMapping("uploadImage")
    public String uploadImage(@RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request){
        User user = authenticationController.getUserFromSession(request.getSession());

        try{
                userService.saveProfilePicture(user, imageFile);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        return "user/manage";
    }
}
