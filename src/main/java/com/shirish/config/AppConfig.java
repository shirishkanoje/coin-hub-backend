package com.shirish.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                // ✅ VERY IMPORTANT (fixes preflight CORS issue)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/coins/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )

            .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)

            .csrf(csrf -> csrf.disable())

            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration cfg = new CorsConfiguration();

        // ✅ Allowed frontend domains
        cfg.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:3000",
                "https://coinhubcrypto.vercel.app"
        ));

        // ✅ Allowed HTTP methods
        cfg.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        // ✅ Allowed headers
        cfg.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type"
        ));

        // ✅ Allow credentials (JWT / cookies)
        cfg.setAllowCredentials(true);

        // ✅ Expose headers to frontend
        cfg.setExposedHeaders(List.of("Authorization"));

        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", cfg);

        return source;
    }
}
