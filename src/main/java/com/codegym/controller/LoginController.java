package com.codegym.controller;

import com.codegym.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("user")
public class LoginController {
    //them user vao model attributes
    @ModelAttribute("user")
    public User setUpUserForm() {
        return new User();
    }

    @RequestMapping("/login")
    public ModelAndView index(@CookieValue(value = "setUser", defaultValue = "") String setUser) {
        Cookie cookie = new Cookie("setUser", setUser);
        return  new ModelAndView("login", "cookieValue", cookie);
    }

    @PostMapping("/do-login")
    public ModelAndView doLogin(@ModelAttribute("user") User user, @CookieValue(value = "setUser", defaultValue = "") String setUser,
                                HttpServletResponse response, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("login");
        //implement business logic
        if (user.getUserName().equals("admin") && user.getPassWord().equals("admin")) {
            if (user.getUserName() != null) {
                setUser = user.getUserName();

                //create cookie and set it in response
                Cookie cookie = new Cookie("setUser", setUser);
                cookie.setMaxAge(10);
                response.addCookie(cookie);

                //get al cookie
                Cookie[] cookies = request.getCookies();

                //iterate each cookie
                for (Cookie c : cookies) {
                    //display only the cookie with the name "setUser"
                    if (c.getName().equals("setUser")) {
                        modelAndView.addObject("cookieValue", c);
                    } else {
                        c.setValue("");
                        modelAndView.addObject("cookieValue", c);
                    }
                    break;
                }
                modelAndView.addObject("message", "Login success. Welcome ");
            }
        } else {
            user.setUserName("");
            Cookie cookie = new Cookie("setUser", setUser);
            modelAndView.addObject("cookieValue", cookie);
            modelAndView.addObject("message", "Login fail. Try again!!");
        }
        return modelAndView;
    }

}
