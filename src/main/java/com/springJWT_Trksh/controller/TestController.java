package com.springJWT_Trksh.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

	@GetMapping("/all")
	public String allAccess() {
		return "Herkese acik icerik";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
	public String userAccess() {
		return "Yalnizca kayitli kisilere ait icerik";
	}
	
	@GetMapping("/mod")
	@PreAuthorize("hasRole('ROLE_MODERATOR')")
	public String modAccess() {
		return "Yalnizca Moderator'a ait icerik";
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String adminAccess() {
		return "Yalnizca Admin'e ait icerik";
	}
}
