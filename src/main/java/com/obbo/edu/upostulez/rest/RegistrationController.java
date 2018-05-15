package com.obbo.edu.upostulez.rest;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.domain.VerificationToken;
import com.obbo.edu.upostulez.domain.dto.PasswordDto;
import com.obbo.edu.upostulez.domain.dto.UserDto;
import com.obbo.edu.upostulez.exception.GenericResponse;
import com.obbo.edu.upostulez.exception.UserNotFoundException;
import com.obbo.edu.upostulez.protocol.AppComProtocol.TokenState;
import com.obbo.edu.upostulez.rest.event.OnRegistrationCompleteEvent;
import com.obbo.edu.upostulez.service.IJwtTokenService;
import com.obbo.edu.upostulez.service.ISecurityService;
import com.obbo.edu.upostulez.service.IUserService;

@Controller
public class RegistrationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

	@Value("${support.email}")
	private String supportEmail;

	@Autowired
	private IUserService userService;
	
	
	private ISecurityService securityService;

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private IJwtTokenService jwtTokenService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@GetMapping("/auth/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable(value = "email") String email) {

		User foundUser = userService.findByEmail(email).orElseThrow(() -> {
			String msg = "User can not be found by the email " + email;
			LOGGER.error(msg);
			return new UsernameNotFoundException(msg);
		});
		
		LOGGER.info("Login successful : " + foundUser.getFirstName() + " " + foundUser.getLastName());

		return ResponseEntity.ok(foundUser);
	}
	

	@PostMapping("/user/registration")
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
			@RequestParam("token") final String token) {

		final TokenState tokenState = userService.validateVerificationToken(token);
		if (TokenState.TOKEN_VALID == tokenState) {
			final User user = userService.getUserFromToken(token);
			// if (user.isUsing2FA()) {
			// model.addAttribute("qr", userService.generateQRUrl(user));
			// return "redirect:/qrcode.html?lang=" + locale.getLanguage();
			// }
			Authentication auth = authWithoutPassword(user);

			jwtTokenService.addJwtTokenToResponse(auth, response);

			// ToDo, redirect to client side Login page
		}

		// TODO: send back to client Error reason
	}

	@GetMapping("/user/resendRegistrationToken")
	public ResponseEntity<GenericResponse> resendRegistrationToken(HttpServletRequest request,
			@RequestParam("token") String existingToken) {
		VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
		User user = newToken.getUser();

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		SimpleMailMessage email = constructResendVerificationTokenEmail(appUrl, request.getLocale(), newToken, user);
		mailSender.send(email);

		return ResponseEntity
				.ok(new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale())));
	}

	@PostMapping("/user/resetPassword")
	public ResponseEntity<GenericResponse> resetPassword(HttpServletRequest request,
			@RequestParam("email") String userEmail) {
		Optional<User> userOpt = userService.findByEmail(userEmail);

		User user = userOpt.orElseThrow(UserNotFoundException::new);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
		return ResponseEntity
				.ok(new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale())));
	}

	@GetMapping("/user/changePassword")
	public ResponseEntity<GenericResponse> showChangePasswordPage(Locale locale, @RequestParam("id") long id,
			@RequestParam("token") String token) {
		String result = securityService.validatePasswordResetToken(id, token);
		if (result != null) {
			return ResponseEntity
					.ok(new GenericResponse(messages.getMessage("auth.message." + result, null, locale)));
		}
		return ResponseEntity
				.ok(new GenericResponse(messages.getMessage("message.changePassword", null, locale)));
	}

	@PostMapping("/user/savePassword")
	public ResponseEntity<GenericResponse> savePassword(Locale locale, @Valid PasswordDto passwordDto) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		userService.changeUserPassword(user, passwordDto.getNewPassword());
		return ResponseEntity
				.ok(new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale)));
	}

	private SimpleMailMessage constructResendVerificationTokenEmail(String contextPath, Locale locale,
			VerificationToken newToken, User user) {
		String confirmationUrl = contextPath + "/regitrationConfirm.html?token=" + newToken.getToken();
		String message = messages.getMessage("message.resendToken", null, locale);
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject("Resend Registration Token");
		email.setText(message + " rn" + confirmationUrl);
		email.setFrom(supportEmail);
		email.setTo(user.getEmail());
		return email;
	}

	private MimeMessage constructResetTokenEmail(String appUrl, Locale locale, String token, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	// Set Authentication of a given User
	private Authentication authWithoutPassword(User user) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
				userService.getAuthoritiesFromUser(user));

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
