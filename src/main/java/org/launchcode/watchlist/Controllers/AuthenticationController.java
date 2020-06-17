package org.launchcode.watchlist.Controllers;

import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.Models.dto.RegisterFormDTO;
import org.launchcode.watchlist.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    @Lazy
    @Autowired
    AuthenticationManager authenticationManager;

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

    @GetMapping("/register")
    public String displayRegistrationForm(HttpServletRequest request, Model model) {

        if (isLoggedIn(request.getSession())){
            return "redirect:";
        }

        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Register");
        return "/Authentication/register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request, HttpServletResponse response,
                                          Model model) {

        // verify valid and new registration
        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "/Authentication/register";
        }

        User existingUser = userRepository.findByUsername(registerFormDTO.getUsername());

        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "/Authentication/register";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Register");
            return "/Authentication/register";
        }

        // Create and save new user
        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword());
        newUser.setProfilePicturePath("defaultProfilePicture.png");
        userRepository.save(newUser);

        // Log in the new user
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                registerFormDTO.getUsername(), registerFormDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(authRequest);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return "redirect:";
    }

    @GetMapping("/login")
    public String displayLoginForm(HttpServletRequest request, Model model, Principal user, String error, String logout){

        if (isLoggedIn(request.getSession())){
            return "redirect:";
        }

        if (user != null) {
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute("message", "danger|Your username and password are invalid");
        }

        if (logout != null) {
            model.addAttribute("message", "info|You have logged out");
        }

        return "/Authentication/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:";
    }

}
