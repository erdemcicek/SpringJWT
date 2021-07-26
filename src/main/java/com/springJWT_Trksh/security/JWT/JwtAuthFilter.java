package com.springJWT_Trksh.security.JWT;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springJWT_Trksh.service.KisiService;

public class JwtAuthFilter extends OncePerRequestFilter {
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	KisiService kisiService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
									HttpServletResponse response, 
									FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			// Request icersindeki Header'a git ve Authorization kismindaki token'i ayikla
			String jwt = jwtAl(request);
			// Token'i gecerle
			if(jwt != null && jwtUtils.JwtTokenGecerle(jwt)) {
				String username = jwtUtils.usernameAl(jwt);
				// Istekte bulunan kisinin bilgilerini Service Layer'dan getir.
				UserDetails kisiDetaylar = kisiService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
																	kisiDetaylar, null, kisiDetaylar.getAuthorities());
				
				authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
				
				// Kimlik denetim bilgilerini tutan Security Context'in guncellenmesi
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			
		} catch (Exception e) {
			System.out.println("Kimlik denetimi gerceklestirilemedi" + e.getMessage());
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	public String jwtAl(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		return null;
	}

}
