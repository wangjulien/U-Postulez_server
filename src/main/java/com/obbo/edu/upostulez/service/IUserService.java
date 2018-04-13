package com.obbo.edu.upostulez.service;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.protocol.AppComProtocol.TokenState;

public interface IUserService extends ICrudService<User> {

	public User registerNewUserAccount(User newUser);

	public void createVerificationTokenForUser(User user, String token);

	public TokenState validateVerificationToken(String token);

	public User getUser(String token);
}
