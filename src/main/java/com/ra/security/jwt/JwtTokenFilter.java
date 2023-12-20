package com.ra.security.jwt;

import com.ra.security.user_principal.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	
	private Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private UserDetailService userDetailService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = getTokenFromRequest(request);
			if (token != null && jwtProvider.validateToken(token)) {
				String username = jwtProvider.getUsernameFromToken(token);
				UserDetails userDetails = userDetailService.loadUserByUsername(username);
				if (userDetails != null) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception e) {
			logger.error("Un Authentication", e.getMessage());
		}
		filterChain.doFilter(request, response);
	}
	
	public String getTokenFromRequest(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) { // Bearer 32sadijsao1983129038180sdadsa
			return header.substring(7);
		}
		return null;
	}
	
}
