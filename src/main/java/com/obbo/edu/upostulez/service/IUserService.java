package com.obbo.edu.upostulez.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.protocol.AppComProtocol.TokenState;

public interface IUserService extends ICrudService<User> {

	public Optional<User> findByEmail(String email);

	public User registerNewUserAccount(User newUser);

	public void createVerificationTokenForUser(User user, String token);

	public TokenState validateVerificationToken(String token);

	public User getUserFromToken(String token);

	public Set<GrantedAuthority> getAuthoritiesFromUser(User user);

}
