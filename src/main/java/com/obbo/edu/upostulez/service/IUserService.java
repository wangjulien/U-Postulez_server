package com.obbo.edu.upostulez.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.domain.VerificationToken;
import com.obbo.edu.upostulez.protocol.AppComProtocol.TokenState;

public interface IUserService extends ICrudService<User> {

	public Optional<User> findByEmail(final String email);

	public User registerNewUserAccount(final User newUser);

	public void createVerificationTokenForUser(final User user, final String token);

	public TokenState validateVerificationToken(final String token);

	public User getUserFromToken(final String token);

	public Set<GrantedAuthority> getAuthoritiesFromUser(final User user);

	public VerificationToken generateNewVerificationToken(final String existingToken);

	public void createPasswordResetTokenForUser(User user, String token);

	public void changeUserPassword(User user, String newPassword);
}
