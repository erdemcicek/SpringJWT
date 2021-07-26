package com.springJWT_Trksh.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springJWT_Trksh.security.JWT.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	@Bean
	public JwtAuthFilter jwtTokenFilter() {
		return new JwtAuthFilter();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.
				cors().		// Cross Origine Resource Sharing ( UI ve Backend arasinda capraz iletisim icin gereklidir. )
				and().
				csrf().disable().
				sessionManagement().
				sessionCreationPolicy(SessionCreationPolicy.STATELESS). 	// JSW oturumunun durumsuz oldugunu belirtiriz.
				and().
				authorizeRequests().
				antMatchers("/api/test/**").permitAll().
				antMatchers("/api/auth/**").permitAll().
				anyRequest().authenticated().and().httpBasic();
		
		
		
		http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
		
		
		
		
		
		
		
	}
}
