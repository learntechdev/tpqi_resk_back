package com.TPQI.thai2learn.config;

import com.TPQI.thai2learn.security.AuthTokenFilter;
import com.TPQI.thai2learn.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Configuration
    @Profile("prod")
    @EnableMethodSecurity
    public class ProdSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChainProd(HttpSecurity http) throws Exception {
            System.out.println(">>> กำลังทำงานในโหมด Production Security <<<");
            http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/api/auth/profile"
                    ).authenticated()
                    .requestMatchers(
                            "/api/auth/login", 
                            "/api/auth/request-credentials",
                            "/swagger-ui/**",
                            "/v3/api-docs/**"
                    ).permitAll()
                    .anyRequest().authenticated()
                );
            http.authenticationProvider(authenticationProvider());
            http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }
    }

    @Configuration
    @Profile("dev")
    public class DevSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChainDev(HttpSecurity http) throws Exception {
            System.out.println(">>> คำเตือน: กำลังทำงานในโหมด Development Security (ปิดการป้องกันทั้งหมด) <<<");
            http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
            return http.build();
        }
    }
}