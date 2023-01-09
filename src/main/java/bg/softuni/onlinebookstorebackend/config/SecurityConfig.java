package bg.softuni.onlinebookstorebackend.config;

import bg.softuni.onlinebookstorebackend.model.enums.UserRoleEnum;
import bg.softuni.onlinebookstorebackend.repositories.UserRepository;
import bg.softuni.onlinebookstorebackend.service.BookstoreUserDetailsService;
import bg.softuni.onlinebookstorebackend.service.OAuthSuccessHandler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           OAuthSuccessHandler oAuthSuccessHandler) throws Exception {

        http.
                // define which requests are allowed and which not
                        authorizeHttpRequests().requestMatchers("**").permitAll().
                // everyone can download static resources (css, js, images)
//                        requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().
//                // everyone can login and register
//                        requestMatchers("/orders/mine", "/cart/**").hasRole(UserRoleEnum.USER.name()).
//                requestMatchers("/authors/add", "/authors/update/**",
//                        "/books/add", "/books/update/**",
//                        "/orders/processed", "/orders/unprocessed", "/orders/statistics",
//                        "/users/all").hasRole(UserRoleEnum.ADMIN.name()).
//                requestMatchers("/", "/books/**", "/authors/**",
//                        "/api/**", "/maintenance", "/swagger-ui/**").permitAll().
//                requestMatchers("/users/login", "/users/register/**", "/password/**").anonymous().
//                // all other pages are available for logger in users
                        anyRequest().
                permitAll().
                and().
                // configuration of form login
                        formLogin().
                // the custom login form
                        loginPage("/users/login").
                // the name of the username form field
                        usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
                // the name of the password form field
                        passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                // where to go in case that the login is successful
                        defaultSuccessUrl("/").
                // where to go in case that the login failed
                        failureForwardUrl("/users/login-error").
                and().
                // configure logut
                        logout().
                // which is the logout url, must be POST request
                        logoutUrl("/users/logout").
                // on logout go to the home page
                        logoutSuccessUrl("/").
                // invalidate the session and delete the cookies
                        invalidateHttpSession(true).
                deleteCookies("JSESSIONID").
                and().
                //allow oauth login
                        oauth2Login().
                loginPage("/users/login").
                successHandler(oAuthSuccessHandler);

        http.csrf().disable();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new BookstoreUserDetailsService(userRepository);
    }
}
