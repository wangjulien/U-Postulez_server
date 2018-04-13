package com.obbo.edu.upostulez.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.obbo.edu.upostulez.domain.Privilege;
import com.obbo.edu.upostulez.domain.Role;
import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.repository.UserRepository;

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
	private UserRepository userRepo;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		LOGGER.info("User login with email : {} ", email);

		User user = userRepo.findByEmail(email).orElseThrow(() -> {
			String msg = "User can not be found by the email " + email;
			LOGGER.error(msg);
			return new UsernameNotFoundException(msg);
		});

		LOGGER.info("User found in the DB : {} ", user.getFirstName() + " " + user.getLastName());

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				user.isEnabled(), true, true, true, getAuthorities(user.getRoles()));
	}

	private final Collection<? extends GrantedAuthority> getAuthorities(final Set<Role> roles) {
		
		Set<Privilege> privileges = roles.stream().map(role -> role.getPrivileges()).flatMap(s -> s.stream())
				.collect(Collectors.toSet());
		Set<GrantedAuthority> authorities = privileges.stream().map(p -> new SimpleGrantedAuthority(p.getName().toString()))
				.collect(Collectors.toSet());
		
		return authorities;
	}
}
