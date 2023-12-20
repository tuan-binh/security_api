package com.ra.security.user_principal;

import com.ra.model.Users;
import com.ra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> optionalUsers = userRepository.findByUsername(username);
		if (optionalUsers.isPresent()) {
			Users users = optionalUsers.get();
			UserPrincipal userPrincipal = UserPrincipal.builder()
					  .users(users)
					  .authorities(users.getRoles().stream().map(item -> new SimpleGrantedAuthority(item.getName())).toList())
					  .build();
			return userPrincipal;
		} else {
			throw new UsernameNotFoundException(username + " not found");
		}
	}
}
