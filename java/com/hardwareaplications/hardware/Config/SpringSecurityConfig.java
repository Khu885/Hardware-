package com.hardwareaplications.hardware.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig{
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        http.csrf(customizer -> customizer.disable() ); // disable CSRF for simplicity in development
        http.authorizeHttpRequests(request -> request.anyRequest().authenticated()); // authenticate all requests
        //http.formLogin(Customizer.withDefaults()); // enable form-based login with default settings
        http.httpBasic(Customizer.withDefaults());  // enable HTTP Basic authentication with default settings
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // set session management to stateless for REST APIs
        return http.build(); // build the security filter chain

    }

    @Autowired
    private UserDetailsService userDetailsService; // declare a user details service for authentication

    @Bean
    public AuthenticationProvider authProvider() {
        // Use constructor that accepts the UserDetailsService (required by current Spring Security API)
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        PasswordEncoder passwordEncoder = passwordEncoder();
        provider.setPasswordEncoder(passwordEncoder); // set the configured PasswordEncoder
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // For development / debugging with plain-text passwords stored in DB, use NoOpPasswordEncoder.
        // WARNING: This is insecure and must NOT be used in production. For production, store bcrypt-hashed
        // passwords and use BCryptPasswordEncoder.
        return NoOpPasswordEncoder.getInstance();
    }

//    @Bean
//    public UserDetailsService userDetailsService (){
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("Arshad")
//                .password("AR@1234")
//                .roles("USER")
//                .build();
//        UserDetails admin1 = User
//                .withDefaultPasswordEncoder()
//                .username("Akif")
//                .password("AK@1234")
//                .roles("ADMIN")
//                .build();
//        UserDetails staff = User
//                .withDefaultPasswordEncoder()
//                .username("Arit")
//                .password("AR@1234")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, admin1, staff); // create an in-memory user details manager with the defined users
//    }
}