package workspace.usermangement_spring_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import workspace.usermangement_spring_security.handler.AuthenticationSuccessHandler;
import workspace.usermangement_spring_security.service.AppUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AppUserDetailsService appUserDetailsService;

    public SecurityConfig(AppUserDetailsService appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    //implementing role based authentication
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(registry -> {
            registry.requestMatchers("/home", "/register").permitAll();
            registry.requestMatchers("/admin/**").hasRole("ADMIN");
            registry.requestMatchers("/user/**").hasRole("USER");
            registry.anyRequest().authenticated();
        }).formLogin(httpSecurityFormLoginConfigurer -> {
            httpSecurityFormLoginConfigurer.loginPage("/login").
                    successHandler(new AuthenticationSuccessHandler())
                    .permitAll();
        }).logout(httpSecurityLogoutConfigurer -> {
            httpSecurityLogoutConfigurer.logoutUrl("/logout").
                    invalidateHttpSession(true).clearAuthentication(true).
                    deleteCookies("JSESSIONID").
                    permitAll();
        }).build();
    }


    @Bean
   public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(appUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
   }


    //password encoding
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
