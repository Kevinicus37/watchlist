package org.launchcode.watchlist.Controllers;

import org.launchcode.watchlist.Models.Movie;
import org.launchcode.watchlist.Models.MovieService;
import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.Models.UserService;
import org.launchcode.watchlist.Models.dto.MovieListDTO;
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
    public String displayUser(@PathVariable String username,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              Model model){

        // Check if provided user exists - if not go to home page?
        int userId = userRepository.findIdByUsername(username);
        if (userId < 1){
            return "redirect:/";
        }

        Page<Movie> userMovieResults = movieRepository.findByUserId(userId, PageRequest.of(page,size));

        MovieListDTO movieListDTO = new MovieListDTO();
        movieListDTO.setUsername(username);
        movieListDTO.setPages(userMovieResults.getTotalPages());
        movieListDTO.setMovieCount(userMovieResults.getTotalElements());
        movieListDTO.setFirstElement((page * size) + 1);
        movieListDTO.setMovies(userMovieResults.toList());
        movieListDTO.setCurrentPage(page);
        movieListDTO.setUserList(true);
        movieListDTO.setUrl(movieService.getBaseUrl(0));

        Page<Movie> upcoming = movieRepository.findByUserIdAndSortByDateAfter(userId,
                movieService.getCurrentDateFormatted(),
                PageRequest.of(0,10,Sort.by("sortByDate")));
        movieListDTO.setUpcoming(upcoming.toList());

        model.addAttribute("dto", movieListDTO);

        return "user/index";
    }

    @PostMapping("/{username}")
    public String processWatchlistSortAndSearch(@PathVariable String username,
                                                @ModelAttribute MovieListDTO movieListDto,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size,
                                                Model model){

        int userId = userRepository.findIdByUsername(username);
        if (userId < 1){
            return "redirect:/";
        }

        Sort sort = movieService.getSort(movieListDto.getSortOption());
        Page<Movie> userMovieResults = movieRepository.findByUserIdAndTitleContaining(userId,
                movieListDto.getSearchTerm(),
                PageRequest.of(page,size, sort));
        List<Movie> movies = new ArrayList<>(userMovieResults.toList());

        movieListDto.setPages(userMovieResults.getTotalPages());
        movieListDto.setMovieCount(userMovieResults.getTotalElements());
        movieListDto.setMovies(movies);
        movieListDto.setCurrentPage(page);
        movieListDto.setFirstElement((page * size) + 1);
        movieListDto.setUserList(true);

        model.addAttribute("dto", movieListDto);

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
