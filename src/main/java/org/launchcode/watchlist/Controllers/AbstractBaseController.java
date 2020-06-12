package org.launchcode.watchlist.Controllers;

import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

public abstract class AbstractBaseController {

    @Autowired
    UserRepository userRepository;

    @ModelAttribute("user")
    public User getLoggedInUser(Principal principal){
        if (principal != null){
            return userRepository.findByUsername(principal.getName());
        }
        return null;
    }
}
