package com.ra.dto.response;

import com.ra.model.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtResponse {
	
	private Users users;
	
	private String token;
	
	private final String type = "Bearer"; // maybe
	
	private Set<String> roles;
}
