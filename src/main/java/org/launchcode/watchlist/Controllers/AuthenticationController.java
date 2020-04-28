package org.launchcode.watchlist.Controllers;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.Models.dto.LoginFormDTO;
import org.launchcode.watchlist.Models.dto.RegisterFormDTO;
import org.launchcode.watchlist.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    private boolean isLoggedIn(HttpSession session){
        if (getUserFromSession(session) != null){
            return true;
        }

        return false;
    }

    @GetMapping
    public String displayLandingPage(Model model) {
        model.addAttribute("title", "Welcome to Watchlist!");
        return "/Authentication/index";
    }

    @GetMapping("/register")
    public String displayRegistrationForm(HttpServletRequest request, Model model) {

        if (isLoggedIn(request.getSession())){
            return "redirect:";
        }

        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Register");
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request,
                                          Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "register";
        }

        User existingUser = userRepository.findByUsername(registerFormDTO.getUsername());

        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "register";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Register");
            return "register";
        }

        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);

        return "redirect:";
    }

    @GetMapping("/login")
    public String displayLoginForm(HttpServletRequest request, Model model){

        if (isLoggedIn(request.getSession())){
            return "redirect:";
        }

        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Login");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO, Errors errors,
                                   HttpServletRequest request, Model model){

        if (errors.hasErrors()){
            model.addAttribute("title", "Login");
            return "login";
        }

        User user = userRepository.findByUsername(loginFormDTO.getUsername());

        if (user == null){
            errors.rejectValue("username", "username.invalid", "Username does not exist.");
            model.addAttribute("title", "Login");
            return "login";
        }

        if (!user.isMatchingPassword(loginFormDTO.getPassword())){
            errors.rejectValue("password", "password.mismatch", "The provided password is invalid.");
            model.addAttribute("title", "Login");
            return "login";
        }

        setUserInSession(request.getSession(), user);

        return "redirect:";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:";
    }




}
