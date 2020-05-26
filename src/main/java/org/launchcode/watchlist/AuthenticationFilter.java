package org.launchcode.watchlist;

import org.launchcode.watchlist.Controllers.AuthenticationController;
import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HandlerInterceptorAdapter {

    @Autowired
    AuthenticationController authenticationController;

    private static final List<String> whitelist = Arrays.asList("/login", "/register",  "/css", "/search", "/error");

    private static boolean isWhitelisted(String path) {
        for (String pathRoot : whitelist) {
            if (path.startsWith(pathRoot) || path.equals("/")) {
                return true;
            }
        }

        return false;
    }

    private void addUserToModel(ModelAndView model, User user) {
        model.addObject("user", user);
    }

    public static boolean isRedirectView(ModelAndView mv) {
        String viewName = mv.getViewName();
        if (viewName.startsWith("redirect:")) {
            return true;
        }
        View view = mv.getView();
        return (view != null && view instanceof SmartView
                && ((SmartView) view).isRedirectView());
    }

    private boolean isUserLoggedIn(HttpSession session){
        return authenticationController.getUserFromSession(session) != null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        // Don't require sign-in for whitelisted pages
        if (isWhitelisted(request.getRequestURI())) {
            // returning true indicates that the request may proceed
            return true;
        }

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);

        // The user is logged in
        if (user != null) {
            return true;
        }

        // The user is NOT logged in
        response.sendRedirect("Authentication/login");
        return false;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object o,
            ModelAndView model) throws Exception {

        if (model != null && !isRedirectView(model)) {
            HttpSession session = request.getSession();
            if (isUserLoggedIn(session)) {
                addUserToModel(model, authenticationController.getUserFromSession(session));
            }
        }
    }


}
