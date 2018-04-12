package com.obbo.edu.upostulez.service;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.exception.UserAlreadyExistException;

public interface IUserService extends ICrudService<User> {

	public User registerNewUserAccount(User newUser) throws UserAlreadyExistException;

	public void createVerificationTokenForUser(User user, String token);
}
