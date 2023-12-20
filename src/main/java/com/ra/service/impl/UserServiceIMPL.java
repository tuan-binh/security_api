package com.ra.service.impl;

import com.ra.dto.request.UserRequestLogin;
import com.ra.dto.request.UserRequestRegister;
import com.ra.dto.response.JwtResponse;
import com.ra.model.Roles;
import com.ra.model.Users;
import com.ra.repository.RoleRepository;
import com.ra.repository.UserRepository;
import com.ra.security.jwt.JwtProvider;
import com.ra.security.user_principal.UserPrincipal;
import com.ra.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceIMPL implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationProvider authenticationProvider;
	@Autowired
	private JwtProvider jwtProvider;
	
	@Override
	public JwtResponse handleLogin(UserRequestLogin userRequestLogin) {
		Authentication authentication;
		try {
			authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userRequestLogin.getUsername(), userRequestLogin.getPassword()));
		} catch (AuthenticationException ex) {
			throw new RuntimeException("username or password invalid");
		}
		
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		String token = jwtProvider.generateToken(userPrincipal);
		JwtResponse jwtResponse = JwtResponse.builder()
				  .users(userPrincipal.getUsers())
				  .token(token)
				  .roles(userPrincipal.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toSet()))
				  .build();
		
		return jwtResponse;
	}
	
	@Override
	public String handleRegister(UserRequestRegister userRequestRegister) {
		
		Set<Roles> roles = new HashSet<>();
		
		userRequestRegister.getRoles().forEach(role -> {
//			// lặp từng phần tử trong mảng role string từ người dùng gửi lên
			roles.add(roleRepository.findByName(role).orElseThrow(() -> new RuntimeException(role + " not found")));
		});
		
		Users users = Users.builder()
				  .fullName(userRequestRegister.getFullName())
				  .username(userRequestRegister.getUsername())
				  .password(passwordEncoder.encode(userRequestRegister.getPassword()))
				  .status(true)
				  .roles(roles)
				  .build();
		userRepository.save(users);
		return "Success";
	}
	
	@Override
	public List<Users> getAllUser() {
		return userRepository.findAll();
	}
}
