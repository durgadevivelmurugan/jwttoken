package com.jwt.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwt.serviceImpl.JwtService;
import com.jwt.serviceImpl.UserDetailServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	JwtService jwtService;
	
	@Autowired
	UserDetailServiceImpl serviceImpl;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response,  
			FilterChain filterChain)
			throws ServletException, IOException {
		
		//because when we make a request we have to send jwt token in header 
		String authHeader=request.getHeader("Authorization");
		
		//bearer token always starts with "bearer" keyword
		if(authHeader==null||!authHeader.startsWith("Bearer ")) {
			//if condition is true then the request and response pass to next filter
			filterChain.doFilter(request, response);
		return;
		}
		
		//extract token from header
		String token=authHeader.substring(7);
		//after extracting token then extract username(from jwt token)
		String username=jwtService.extractUsername(token); 
	
		//the user is not yet authenticated( SecurityContextHolder.getContext().getAuthentication()==null)
	if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
		UserDetails userDetails=serviceImpl.loadUserByUsername(username);
		
		//check token is valid
		if(jwtService.isValid(token, userDetails)) {
			UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
		}
	}
	filterChain.doFilter(request, response);
	
	}

}
