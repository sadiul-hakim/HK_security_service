package xyz.sadiulhakim.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import xyz.sadiulhakim.refreshToken.RefreshTokenService;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final FilterChainExceptionHandler filterChainExceptionHandler;
    private final CustomAuthorizationFilter authorizationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final RefreshTokenService refreshTokenService;

    private final String[] publicApis = {};

    @Bean
    SecurityFilterChain config(HttpSecurity http) throws Exception {
        return http.addFilterBefore(filterChainExceptionHandler, LogoutFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers(publicApis).permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.PATCH).denyAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.TRACE).denyAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.HEAD).denyAll())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout.logoutUrl("/logout").clearAuthentication(true).deleteCookies("Authorization"))
                .authenticationProvider(authenticationProvider)
                .addFilter(new CustomAuthenticationFilter(authenticationProvider, refreshTokenService))
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setMaxAge(3600L);
        configuration.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "X-Requested-With", "Origin", "Accept",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"
        ));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
