package com.obbo.edu.upostulez.rest;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.obbo.edu.upostulez.config.ConstantsConfig;
import com.obbo.edu.upostulez.domain.Privilege;
import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.domain.UserDto;
import com.obbo.edu.upostulez.protocol.AppComProtocol.TokenState;
import com.obbo.edu.upostulez.rest.event.OnRegistrationCompleteEvent;
import com.obbo.edu.upostulez.service.IUserService;
import com.obbo.edu.upostulez.service.JwtTokenService;

@Controller
public class RegistrationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

	@Autowired
	private MessageSource messages;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@PostMapping("/registration")
	public ResponseEntity<User> registerUserAccount(@Valid final UserDto accountDto, final HttpServletRequest request) {
		LOGGER.info("Registering user account with information : {}", accountDto);

		final User registered = userService.registerNewUserAccount(modelMapper.map(accountDto, User.class));

		eventPublisher
				.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));

		LOGGER.info("User registrated : {}", registered);

		return ResponseEntity.ok(registered);
	}

	@GetMapping("/registrationConfirm")
	public void confirmRegistration(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam("token") final String token) throws UnsupportedEncodingException {
		
		final TokenState tokenState = userService.validateVerificationToken(token);
		if (TokenState.TOKEN_VALID == tokenState) {
			final User user = userService.getUser(token);
			// if (user.isUsing2FA()) {
			// model.addAttribute("qr", userService.generateQRUrl(user));
			// return "redirect:/qrcode.html?lang=" + locale.getLanguage();
			// }
			Authentication auth = authWithoutPassword(user);

			response.addHeader(ConstantsConfig.HEADER_STRING, ConstantsConfig.TOKEN_PREFIX + jwtTokenService.buildJwtToken(auth));
			response.addHeader(ConstantsConfig.HEADER_ACCESS, ConstantsConfig.HEADER_STRING);
		}
		
		// TODO
	}
	
	// Set Authentification of a given User
	private Authentication authWithoutPassword(User user) {
		Set<Privilege> privileges = user.getRoles().stream().map(role -> role.getPrivileges()).flatMap(s -> s.stream())
				.collect(Collectors.toSet());
		Set<GrantedAuthority> authorities = privileges.stream().map(p -> new SimpleGrantedAuthority(p.getName().toString()))
				.collect(Collectors.toSet());

		Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}

	private String getAppUrl(HttpServletRequest request) {
		StringBuilder url = new StringBuilder();
		url.append(request.getScheme()).append("://").append(request.getServerName()).append(":")
				.append(request.getServerPort()).append(request.getContextPath());
		return url.toString();
	}
}
