package com.obbo.edu.upostulez.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.obbo.edu.upostulez.config.ConstantsConfig;
import com.obbo.edu.upostulez.service.JwtTokenService;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String jwtToken = req.getHeader(ConstantsConfig.HEADER_STRING);

		if (jwtToken == null || !jwtToken.startsWith(ConstantsConfig.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		SecurityContextHolder.getContext().setAuthentication(jwtTokenService.decodeJwtToken(jwtToken));
		chain.doFilter(req, res);
	}
}