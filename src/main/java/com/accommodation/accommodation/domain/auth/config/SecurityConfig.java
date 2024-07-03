package com.accommodation.accommodation.domain.auth.config;

import com.accommodation.accommodation.domain.auth.config.filter.JwtAuthenticationFilter;
import com.accommodation.accommodation.domain.auth.config.filter.LoginFilter;
import com.accommodation.accommodation.domain.auth.service.TokenService;
import com.accommodation.accommodation.domain.auth.service.UserDetailsServiceImpl;
import com.accommodation.accommodation.global.util.CookieUtil;
import com.accommodation.accommodation.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
        AuthenticationManager authenticationManager) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable);

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http
            .authorizeHttpRequests(matcher -> matcher
                .requestMatchers("/api/register/**", "/api/login", "/api/check").permitAll()
                .requestMatchers("/api/accommodation/**").permitAll()
                .requestMatchers("/api/accommodation").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated());

        http.addFilterBefore(loginFilter(authenticationManager),
            UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager, tokenService);
        loginFilter.setFilterProcessesUrl("/api/login");
        return loginFilter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenService, jwtProvider, cookieUtil,
            userDetailsService);
    }


    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("https://fe-mini-project-ten.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
