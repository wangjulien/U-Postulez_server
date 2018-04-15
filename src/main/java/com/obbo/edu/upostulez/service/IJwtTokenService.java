package com.obbo.edu.upostulez.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

public interface IJwtTokenService {

	void addJwtTokenToResponse(Authentication auth, HttpServletResponse response);

	String buildJwtToken(Authentication auth);

	Authentication decodeJwtToken(String jwtToken);

}
