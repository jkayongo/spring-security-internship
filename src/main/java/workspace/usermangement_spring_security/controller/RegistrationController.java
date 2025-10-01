package workspace.usermangement_spring_security.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import workspace.usermangement_spring_security.model.AppUser;
import workspace.usermangement_spring_security.repository.AppUserRepository;

@RestController
public class RegistrationController {

    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    public RegistrationController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

}
