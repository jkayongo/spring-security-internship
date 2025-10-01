package workspace.usermangement_spring_security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
    @GetMapping("/home")
    public String handleWelcome(){
        return "home";
    }
    //admin home page
    @GetMapping("/admin/home")
    public String handleAdminHome(){
        return "home_admin";
    }

    //user home page
    @GetMapping("/user/home")
    public String handleUserHome(){
        return "home_user";
    }

    //login page
    @GetMapping("/login")
    public String displayLoginPage(){
        return "login_page";
    }
}
