package com.example.jwt.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAutenticationFilter autentication;

	public SecurityConfig(JwtAutenticationFilter autentication) {
		super();
		this.autentication = autentication;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		return http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/login", "/api/auth/registrar").permitAll()
						.anyRequest().authenticated()).addFilterBefore(autentication, UsernamePasswordAuthenticationFilter.class).build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration configuration = new CorsConfiguration();
		
		configuration.setAllowedOrigins(List.of("http://localhost:5173"));
		
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		
		configuration.setAllowedHeaders(List.of("*"));
		
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
	}
}
