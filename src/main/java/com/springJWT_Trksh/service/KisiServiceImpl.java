package com.springJWT_Trksh.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springJWT_Trksh.model.Kisi;

public class KisiServiceImpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	private String username;
	private String email;
	
	@JsonIgnore					// Password bilgisini json dosyasina saklamasi icin kullanilan anotasyon
	private String password;
	
	private Collection<? extends GrantedAuthority> otoriteler;
	
	public KisiServiceImpl(Long id, String username, String email, String password,
			Collection<? extends GrantedAuthority> otoriteler) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.otoriteler = otoriteler;
	}
	
	
	public static KisiServiceImpl kisiOlustur(Kisi kisi) {
		
		List<GrantedAuthority> otoriteler = kisi.getRoller().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
		
		return new KisiServiceImpl(
				kisi.getId(),
				kisi.getUsername(),
				kisi.getEmail(),
				kisi.getPassword(),
				otoriteler
				);
	}
	
	public Long getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return otoriteler;
	}

	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public String getUsername() {
		return username;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
}
