package com.obbo.edu.upostulez.rest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.domain.UserDto;
import com.obbo.edu.upostulez.rest.event.OnRegistrationCompleteEvent;
import com.obbo.edu.upostulez.service.IUserService;

@Controller
public class RegistrationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

	@Autowired
	private IUserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@PostMapping("/registration")
	public ResponseEntity<User> registerUserAccount(@Valid final UserDto accountDto, final HttpServletRequest request) {
		LOGGER.info("Registering user account with information : {}", accountDto);

		final User registered = userService.registerNewUserAccount(modelMapper.map(accountDto, User.class));

		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));

		LOGGER.info("User registrated : {}", registered);

		return ResponseEntity.ok(registered);
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
