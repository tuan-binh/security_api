package com.ra.controller;

import com.ra.dto.request.UserRequestLogin;
import com.ra.dto.request.UserRequestRegister;
import com.ra.dto.response.JwtResponse;
import com.ra.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> handleLogin(@RequestBody UserRequestLogin userRequestLogin) {
		return new ResponseEntity<>(userService.handleLogin(userRequestLogin), HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> handleRegister(@RequestBody UserRequestRegister userRequestRegister) {
		return new ResponseEntity<>(userService.handleRegister(userRequestRegister), HttpStatus.CREATED);
	}
	
}
