package workspace.usermangement_spring_security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import workspace.usermangement_spring_security.model.AppUser;
import workspace.usermangement_spring_security.repository.AppUserRepository;

import java.util.Optional;

//this is a custom implementation of the UserDetailService
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findAppUserByUserName(username);
        if(appUser.isPresent()){
            AppUser appUser1 = appUser.get();
            return User.builder().
                    username(appUser1.getUserName()).
                    password(appUser1.getPassword()).
                    roles(getRoles(appUser1)).
                    build();
        }
        else throw new UsernameNotFoundException(username);
    }

    public String[] getRoles(AppUser appUser){
        if(appUser.getRole() == null){
            return new String[]{"USER"};
        }
        return appUser.getRole().split(",");
    }

}