package com.obbo.edu.upostulez.service;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.domain.UserDto;
import com.obbo.edu.upostulez.exception.UserAlreadyExistException;

public interface IUserService {

	User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

}
