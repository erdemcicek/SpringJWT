package com.springJWT_Trksh.controller;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springJWT_Trksh.model.ERoller;
import com.springJWT_Trksh.model.Kisi;
import com.springJWT_Trksh.model.KisiRole;
import com.springJWT_Trksh.repository.KisiRepository;
import com.springJWT_Trksh.repository.RoleRepository;
import com.springJWT_Trksh.reqres.LoginRequest;
import com.springJWT_Trksh.reqres.MesajResponse;
import com.springJWT_Trksh.reqres.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	KisiRepository kisiRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	public ResponseEntity<?> girisYap(@RequestBody LoginRequest loginRequest){
		
		// Kimlik denetiminin yapilmasi
		Authentication authentication = authenticationManager.
				authenticate( new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		return ResponseEntity.ok("basarili");
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest ) {
		
		if(kisiRepository.existsByUsername(registerRequest.getUsername())) {
			return ResponseEntity.
								badRequest().
								body( new MesajResponse("Hata: username kullaniliyor..."));
		}
		
		
		if(kisiRepository.existsByEmail(registerRequest.getEmail())) {
			return ResponseEntity.
								badRequest().
								body( new MesajResponse("Hata: email kullaniliyor..."));
		}
		
		// Yeni kullaniciyi kaydet
		Kisi kisi = new Kisi(registerRequest.getUsername(), 
							passwordEncoder.encode(registerRequest.getPassword()), 
							registerRequest.getEmail());
		
		
		Set<String> stringRoller = registerRequest.getRole();
		Set<KisiRole> roller = new HashSet<>();
		
		if(stringRoller == null) {
			KisiRole userRole = roleRepository.findByName(ERoller.ROLE_USER).
								orElseThrow(() -> new RuntimeException("Hata: Veritabaninda Role kayitli degil..."));
			roller.add(userRole);
		}
		else {
			stringRoller.forEach( role -> {
				switch(role) {
					case "admin":
						KisiRole adminRole = roleRepository.findByName(ERoller.ROLE_ADMIN)
									.orElseThrow(() -> new RuntimeException("Hata: Role mevcut degil..."));
						roller.add(adminRole);
						break;
					case "mod":
						KisiRole modRole = roleRepository.findByName(ERoller.ROLE_MODERATOR)
									.orElseThrow(() -> new RuntimeException("Hata: Role mevcut degil..."));
						roller.add(modRole);
						break;
					default:
						KisiRole userRole = roleRepository.findByName(ERoller.ROLE_USER)
									.orElseThrow(() -> new RuntimeException("Hata: Role mevcut degil..."));
						roller.add(userRole);
				}
			});
			kisi.setRoller(roller);
			// Veritabanina yeni kaydi ekle.
			kisiRepository.save(kisi);
		}
		
		return ResponseEntity.ok( new MesajResponse("Kullanici basariyla kaydedildi..."));
	}
}
