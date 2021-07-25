package com.springJWT_Trksh.reqres;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;


@Setter @Getter
public class RegisterRequest {

	@NotBlank
	private String username;
	
	@NotBlank
	@Size(min=6, max=40)
	private String password;
	private String email;
	private Set<String> role;
}
