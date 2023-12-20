package com.ra.config;

import com.ra.security.jwt.JwtEntryPoint;
import com.ra.security.jwt.JwtTokenFilter;
import com.ra.security.user_principal.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private JwtTokenFilter jwtTokenFilter;
	@Autowired
	private JwtEntryPoint jwtEntryPoint;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				  .cors(auth -> auth.configurationSource(request -> {
					  CorsConfiguration config = new CorsConfiguration();
					  config.setAllowedOrigins(List.of("*"));
					  config.setAllowedMethods(List.of("*"));
					  return config;
				  }))
				  .csrf(AbstractHttpConfigurer::disable)
				  .authenticationProvider(authenticationProvider())
				  .authorizeHttpRequests((auth) ->
							 auth.requestMatchers("/auth/**").permitAll()
										.requestMatchers("/user/**").hasAuthority("ROLE_ADMIN")
										.anyRequest().authenticated())
				  .exceptionHandling((auth) ->
							 auth.authenticationEntryPoint(jwtEntryPoint))
				  
				  .sessionManagement((auth) -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				  .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
				  .build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
}
