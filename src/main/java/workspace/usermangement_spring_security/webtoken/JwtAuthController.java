package workspace.usermangement_spring_security.webtoken;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import workspace.usermangement_spring_security.service.AppUserDetailsService;

@RestController
public class JwtAuthController {
    //will help us authenticate using username and password
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final AppUserDetailsService appUserDetailsService;

    public JwtAuthController(AuthenticationManager authenticationManager, JwtService jwtService, AppUserDetailsService appUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.appUserDetailsService = appUserDetailsService;
    }

    @PostMapping("/authenticate")
    public String authenticateAndGenerateToken(@RequestBody LoginForm loginForm){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginForm.userName(), loginForm.password()));

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(appUserDetailsService.loadUserByUsername(loginForm.userName()));
        }else{
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }

    @GetMapping("/welcome/home")
    public String welcome(){
        return "welcome home";
    }
    //admin home page
    @GetMapping("/jwtadmin/welcome/admin/home")
    public String welcomeAdminHome(){
        return "welcome home_admin";
    }

    //user home page
    @GetMapping("/jwtuser/user/home")
    public String welcomeUserHome(){
        return "welcome home_user";
    }


}
