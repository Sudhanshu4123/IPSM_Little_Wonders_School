package com.littlewonders.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.core.annotation.Order;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/admin/**", "/ipsm-admin", "/ipsm-admin/process")
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/ipsm-admin", "/ipsm-admin/process", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/ipsm-admin")
                .loginProcessingUrl("/ipsm-admin/process")
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/home")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/", "/home", "/about", "/contact", "/admission/**", "/blog/**", "/careers/**", "/franchise/**", "/gallery", "/toddler", "/pre-nursery", "/nursery", "/prep", "/methodology", "/facilities", "/speciality", "/holidays", "/celebrations", "/mission", "/vision", "/branches").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**", "/fonts/**", "/webjars/**").permitAll()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            var authorities = authentication.getAuthorities();
            String loginPath = request.getRequestURI(); 
            String redirectUrl = "/home";

            boolean isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isUser = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

            if (isAdmin) {
                if (!loginPath.contains("ipsm-admin")) {
                    request.getSession().invalidate(); // Force logout
                    response.sendRedirect("/login?error=true&admin_not_allowed=true");
                    return;
                }
                redirectUrl = "/admin/dashboard";
            } else if (isUser) {
                if (loginPath.contains("ipsm-admin")) {
                    request.getSession().invalidate(); // Force logout
                    response.sendRedirect("/ipsm-admin?error=true&user_not_allowed=true");
                    return;
                }
                redirectUrl = "/user/dashboard";
            }

            response.sendRedirect(redirectUrl);
        };
    }
}
