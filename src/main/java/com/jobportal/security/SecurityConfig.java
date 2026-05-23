package com.jobportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

private final CustomUserDetailsService userDetailsService;

public SecurityConfig(CustomUserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
}

// ✅ Password encoder
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// ✅ Authentication provider
@Bean
public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
}

// ✅ Main Security Configuration
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .authorizeHttpRequests(auth -> auth
            // ✅ PUBLIC ROUTES
            .requestMatchers(
                    "/", 
                    "/register", 
                    "/login", 
                    "/auth/**",
                    "/js/**", 
                    "/images/**", 
                    "/h2-console/**"
            ).permitAll()

            // ✅ ROLE-BASED ACCESS
            .requestMatchers("/jobs/post/**").hasAnyRole("EMPLOYER", "ADMIN")
            .requestMatchers("/applications/apply/**").hasRole("STUDENT")

            // ✅ AUTHENTICATED ROUTES
            .requestMatchers("/dashboard/**").authenticated()

            .anyRequest().authenticated()
        )

        // ✅ LOGIN CONFIG
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/dashboard", true)
            .permitAll()
        )

        // ✅ LOGOUT CONFIG
        .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .permitAll()
        )

        // ✅ H2 Console Fix
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers.frameOptions().disable());

    return http.build();
}


}
