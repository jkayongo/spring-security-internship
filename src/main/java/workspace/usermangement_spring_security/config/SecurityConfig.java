package workspace.usermangement_spring_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import workspace.usermangement_spring_security.handler.AuthenticationSuccessHandler;
import workspace.usermangement_spring_security.service.AppUserDetailsService;
import workspace.usermangement_spring_security.webtoken.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AppUserDetailsService appUserDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(AppUserDetailsService appUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.appUserDetailsService = appUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    //implementing role based authentication
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(registry -> {
            registry.requestMatchers("/home", "/register", "/authenticate", "/welcome/**").permitAll();
            registry.requestMatchers("/admin/**", "/jwtadmin/**").hasRole("ADMIN");
            registry.requestMatchers("/user/**", "/jwtuser/**").hasRole("USER");
            registry.anyRequest().authenticated();
        }).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(registry -> {
//            registry.requestMatchers("/home", "/register", "/authenticate").permitAll();
//            registry.requestMatchers("/admin/**").hasRole("ADMIN");
//            registry.requestMatchers("/user/**").hasRole("USER");
//            registry.anyRequest().authenticated();
//        }).formLogin(httpSecurityFormLoginConfigurer -> {
//            httpSecurityFormLoginConfigurer.loginPage("/login").
//                    successHandler(new AuthenticationSuccessHandler())
//                    .permitAll();
//        }).logout(httpSecurityLogoutConfigurer -> {
//            httpSecurityLogoutConfigurer.logoutUrl("/logout").
//                    invalidateHttpSession(true).clearAuthentication(true).
//                    deleteCookies("JSESSIONID").
//                    permitAll();
//        }).build();
//    }


    @Bean
   public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(appUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
   }

   @Bean
   public AuthenticationManager authenticationManager(){
       return new ProviderManager(authenticationProvider());
   }


    //password encoding
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
