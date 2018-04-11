package com.obbo.edu.upostulez.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
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
import com.obbo.edu.upostulez.protocol.DbEntityProtocol.PrivilegeName;
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

		Optional<User> optUser = userRepo.findByEmail(email);

		LOGGER.info("User login with email : {} ", email);

		User user = optUser.orElseThrow(() -> {
			String msg = "User can not be found by the email " + email;
			LOGGER.error(msg);
			return new UsernameNotFoundException(msg);
		});

		LOGGER.info("User found in the DB : {} ", user.getFirstName() + " " + user.getLastName());

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				getAuthorities(user.getRoles()));
	}

	private final Collection<? extends GrantedAuthority> getAuthorities(final Set<Role> roles) {

		return getGrantedAuthorities(getPrivileges(roles));
	}

	private final Set<PrivilegeName> getPrivileges(final Set<Role> roles) {

		Set<Privilege> allPrivileges = new HashSet<>();
		for (Role role : roles) {
			allPrivileges.addAll(role.getPrivileges());
		}

		return allPrivileges.stream().map(Privilege::getName).collect(Collectors.toSet());
	}

	private final Set<GrantedAuthority> getGrantedAuthorities(final Set<PrivilegeName> privileges) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		for (PrivilegeName privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege.toString()));
		}
		return authorities;
	}
}
