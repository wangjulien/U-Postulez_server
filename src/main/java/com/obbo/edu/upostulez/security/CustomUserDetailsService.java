package com.obbo.edu.upostulez.security;

import java.util.HashSet;
import java.util.Set;

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

import com.obbo.edu.upostulez.domain.Role;
import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.repository.UserRepository;

/**
 * Classe implements UserDetailsService (Spring Security) pour offrir se logger a partir user dans DB
 * 
 * @author JW
 *
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER =  LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	
	@Autowired
    private UserRepository userRepo;
	
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	
    	User user = userRepo.findByEmail(email);
    	
    	LOGGER.info("Utilisateur login ", email);
		if(user == null){
			LOGGER.error("L'utilisateur non trouve");
			throw new UsernameNotFoundException("L'utilisateur non trouve par email " + email);
		}
				
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (Role role : user.getRoles()) {
			setAuths.add(new SimpleGrantedAuthority(role.toString()));
		}
		
		LOGGER.info("Utilisateur trouve de DB ", user.getFirstName() + " " + user.getLastName());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), setAuths);
    }
}
