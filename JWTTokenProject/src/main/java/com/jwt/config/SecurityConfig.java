package com.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwt.filter.JWTAuthenticationFilter;
import com.jwt.serviceImpl.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	@Autowired
	UserDetailServiceImpl detailServiceImpl;
	
	@Autowired
	JWTAuthenticationFilter authenticationFilter;
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//Cross-Site Request Forgery
		//CSRF protection will not be applied to any requests handled by this security filter chain.
		return http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(//(authorizeHttpRequest)it specifies which requests should be permitted or denied
						req -> req.requestMatchers("/login/**","/register/**","/role/**").permitAll()
						.requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
						.requestMatchers("/user/**","/onlyuser/**").hasAnyAuthority("USER")
						.anyRequest().authenticated())
				
				
						.userDetailsService(detailServiceImpl)//configures custom user details service to retrieve user details during authentication
						.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
						//a custom authentication filter is added before UsernamePasswordAuthenticationFilter in filter chain
						.addFilterBefore(authenticationFilter,UsernamePasswordAuthenticationFilter.class)
						.build();
						
		
	}
	//for encoding the password
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	 
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
