package org.launchcode.watchlist.Controllers;

import org.launchcode.watchlist.Models.Movie;
import org.launchcode.watchlist.Models.MovieService;
import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.Models.UserService;
import org.launchcode.watchlist.Models.dto.NewPasswordFormDTO;
import org.launchcode.watchlist.data.MovieRepository;
import org.launchcode.watchlist.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("user")
public class UserController extends AbstractBaseController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    MovieService movieService;

    @Autowired
    UserService userService;

    @GetMapping("/{username}")
    public String displayUser(@PathVariable String username, @RequestParam(defaultValue = "title") String sortOption,
                              @RequestParam(defaultValue = "0") int page,
                              HttpServletRequest request,
                              Model model){

        // Check if provided user exists - if not go to home page?
        int userId = userRepository.findIdByUsername(username);
        if (userId < 1){
            return "redirect:";
        }

        //

        Page<Movie> userMovieResults = movieRepository.findByUserId(userId, PageRequest.of(page,20));
        int pages = userMovieResults.getTotalPages();
        long totalCount = userMovieResults.getTotalElements();
        int first = (page * 20) + 1;
        List<Movie> movies = new ArrayList<>(userMovieResults.toList());
        movieService.sortMovies(movies, sortOption);

        Sort sort = Sort.by("sortByDate");
        Page<Movie> upcoming = movieRepository.findByUserIdAndSortByDateAfter(userId, movieService.getCurrentDateFormatted(), PageRequest.of(0,10,sort));
        List<Movie> upcomingMovies = upcoming.toList();

        model.addAttribute("first", first);
        model.addAttribute("currentPage", page);
        model.addAttribute("movieCount", totalCount);
        model.addAttribute("pages", pages);
        model.addAttribute("upcoming", upcomingMovies);
        model.addAttribute("isUserList", true);
        model.addAttribute("url", movieService.getBaseUrl(0));
        model.addAttribute("movies", movies);

        return "user/index";
    }

    @GetMapping("manage")
    public String displayManagePage(Model model){
        model.addAttribute("title", "Manage User Account");
        model.addAttribute(new NewPasswordFormDTO());
        return "user/manage";
    }

    @PostMapping("manage")
    public String processUpdatePasswordRequest(@ModelAttribute @Valid NewPasswordFormDTO newPasswordFormDTO, Errors errors,
                                               HttpServletRequest request, Model model) {

        if (errors.hasErrors()){
            model.addAttribute("title", "Manage User Account");
            return "/user/manage";
        }

        String password = newPasswordFormDTO.getNewPassword();
        String verifyPassword = newPasswordFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("newPassword", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Manage User Account");
            return "/user/manage";
        }

        User user = authenticationController.getUserFromSession(request.getSession());

        if (!user.isMatchingPassword(newPasswordFormDTO.getOldPassword())){
            errors.rejectValue("oldPassword", "passwords.mismatch", "Password does not match the user's password");
            model.addAttribute("title", "Manage User Account");
            return "/user/manage";
        }

        user.setPasswordHash(newPasswordFormDTO.getNewPassword());
        userRepository.save(user);

        model.addAttribute("success", "Password Updated!");
        return "user/manage";
    }

    @PostMapping("uploadImage")
    public String uploadImage(@RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request, Model model){
        User user = authenticationController.getUserFromSession(request.getSession());

        try {
                userService.saveProfilePicture(user, imageFile);
                model.addAttribute("success", "New Image Uploaded!");
            }
        catch (Exception e) {
                e.printStackTrace();
            }

        model.addAttribute(new NewPasswordFormDTO());

        return "user/manage";
    }
}
