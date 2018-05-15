package com.obbo.edu.upostulez.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

public interface IJwtTokenService {

	public void addJwtTokenToResponse(Authentication auth, HttpServletResponse response);

	public Authentication decodeJwtToken(String jwtToken);

}
