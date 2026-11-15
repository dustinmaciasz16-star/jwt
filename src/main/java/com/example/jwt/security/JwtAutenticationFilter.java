package com.example.jwt.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jwt.service.TokenBlackListService;
import com.example.jwt.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAutenticationFilter extends OncePerRequestFilter {

	private final TokenBlackListService blackList;

	public JwtAutenticationFilter(TokenBlackListService blackList) {
		this.blackList = blackList;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String authHeader = request.getHeader("Authorization");
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = authHeader.substring(7);
		if(blackList.estaInvalidado(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Acceoso denegado : Sesion Cerrada");
			return;
		}
		DecodedJWT datosToken = JwtUtil.validarToke(token);
		if(datosToken != null) {
			String username = datosToken.getSubject();
			String rolOriginal = datosToken.getClaim("rol").asString();
			
			String rolString = "ROLE_" + rolOriginal;
			
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(rolString);
			
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.singleton(authority));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

}
