package com.springJWT_Trksh.security.JWT;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.springJWT_Trksh.service.KisiServiceImpl;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
	
	@Value("${springJWT.app.jwtExpirationMs}")
	private long jwtExpirationMs;
	
	@Value("${springJWT.app.jwtSecret}")
	private String jwtSecret;
	

	public String JwtOlustur(Authentication authentication) {
		
		KisiServiceImpl kisiBilgiler = (KisiServiceImpl) authentication.getPrincipal();
		
		return Jwts.builder().
				setSubject(kisiBilgiler.getUsername()).
				setIssuedAt( new Date()).
				setExpiration(new Date(new Date().getTime() + jwtExpirationMs)).
				signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
	
	public String usernameAl(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean JwtTokenGecerle(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (Exception e) {
			System.out.println("JWT Hatasi:" + e.getMessage());
		}
		return false;
	}
	
	
	
	
	
}
