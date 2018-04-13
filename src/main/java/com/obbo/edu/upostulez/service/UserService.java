package com.obbo.edu.upostulez.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.domain.VerificationToken;
import com.obbo.edu.upostulez.exception.UserAlreadyExistException;
import com.obbo.edu.upostulez.protocol.AppComProtocol.TokenState;
import com.obbo.edu.upostulez.protocol.DbEntityProtocol.RoleName;
import com.obbo.edu.upostulez.repository.RoleRepository;
import com.obbo.edu.upostulez.repository.UserRepository;
import com.obbo.edu.upostulez.repository.VerificationTokenRepository;

@Service
@Transactional
public class UserService extends AbstractService<User> implements IUserService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected PagingAndSortingRepository<User, Long> getDao() {
		return userRepository;
	}

	@Override
	public User registerNewUserAccount(User newUser) {

		if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
			throw new UserAlreadyExistException("There is an account with that email adress: " + newUser.getEmail());
		}

		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.addRole(roleRepository.findByName(RoleName.ROLE_USER).get());
		return userRepository.save(newUser);
	}

	@Override
	public void createVerificationTokenForUser(User user, String token) {
		final VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}

	@Override
	public TokenState validateVerificationToken(String token) {
		Optional<VerificationToken> optVerificationToken = tokenRepository.findByToken(token);
		
		if ( !optVerificationToken.isPresent() ) {
			return TokenState.TOKEN_INVALID;
		}
		
		VerificationToken verificationToken = optVerificationToken.get();
		
		final User user = verificationToken.getUser();
		if ( verificationToken.isExpired() ) {
			tokenRepository.delete(verificationToken);
			return TokenState.TOKEN_EXPIRED;
		}

		user.setEnabled(true);
		// tokenRepository.delete(verificationToken);
		userRepository.save(user);
		return TokenState.TOKEN_VALID;
	}

	@Override
	public User getUser(String token) {
		return tokenRepository.findByToken(token).orElseThrow(IllegalStateException::new).getUser();
	}
}
