package workspace.usermangement_spring_security.webtoken;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TokenResponse> authenticateAndGenerateToken(@RequestBody LoginForm loginForm){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginForm.userName(), loginForm.password()));

        if(authentication.isAuthenticated()){
            String accessToken = jwtService.generateAccessToken(appUserDetailsService.loadUserByUsername(loginForm.userName()));
            String refreshToken = jwtService.generateRefreshToken(appUserDetailsService.loadUserByUsername(loginForm.userName()));
            return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
        }else{
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        String refreshToken = refreshTokenRequest.refreshToken();
        if(refreshToken == null){
            return ResponseEntity.badRequest().build();
        }

        if(jwtService.isRefreshTokenValid(refreshToken)) {
            String userName = jwtService.extractUserNameFromRefreshToken(refreshToken);
            String newAccessToken = jwtService.generateAccessToken(appUserDetailsService.loadUserByUsername(userName));
            return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
