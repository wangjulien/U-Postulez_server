package com.obbo.edu.upostulez.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.service.IUserService;

/**
 * Classe implements UserDetailsService (Spring Security) pour offrir se logger
 * a partir user dans DB
 * 
 * @author JW
 *
 */

@Component
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	private IUserService userService;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		LOGGER.info("User login with email : {} ", email);

		User user = userService.findByEmail(email).orElseThrow(() -> {
			String msg = "User can not be found by the email " + email;
			LOGGER.error(msg);
			return new UsernameNotFoundException(msg);
		});

		LOGGER.info("User found in the DB : {} ", user.getFirstName() + " " + user.getLastName());

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				user.isEnabled(), true, true, true, userService.getAuthoritiesFromUser(user));
	}
}
