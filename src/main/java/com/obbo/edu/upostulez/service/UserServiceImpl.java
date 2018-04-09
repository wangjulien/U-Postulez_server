package com.obbo.edu.upostulez.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.domain.UserDto;
import com.obbo.edu.upostulez.exception.UserAlreadyExistException;
import com.obbo.edu.upostulez.protocol.DbEntityProtocol.RoleName;
import com.obbo.edu.upostulez.repository.RoleRepository;
import com.obbo.edu.upostulez.repository.UserRepository;

public class UserServiceImpl implements IUserService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException {
	  
	    if (userRepository.findByEmail(accountDto.getEmail()).isPresent()) {
	        throw new UserAlreadyExistException
	          ("There is an account with that email adress: " + accountDto.getEmail());
	    }
	    User user = new User();
	 
	    user.setFirstName(accountDto.getFirstName());
	    user.setLastName(accountDto.getLastName());
	    user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
	    user.setEmail(accountDto.getEmail());
	 
	    user.addRole(roleRepository.findByName(RoleName.ROLE_USER).get());
	    return userRepository.save(user);
	}
}
