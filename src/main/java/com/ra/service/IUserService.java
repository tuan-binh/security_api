package com.ra.service;

import com.ra.dto.request.UserRequestLogin;
import com.ra.dto.request.UserRequestRegister;
import com.ra.dto.response.JwtResponse;
import com.ra.model.Users;

import java.util.List;

public interface IUserService {
	
	JwtResponse handleLogin(UserRequestLogin userRequestLogin);
	
	String handleRegister(UserRequestRegister userRequestRegister);
	
	List<Users> getAllUser();
	
}
