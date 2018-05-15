package com.obbo.edu.upostulez.service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.obbo.edu.upostulez.config.ConstantsConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenService implements IJwtTokenService{
	
	@Override
	public void addJwtTokenToResponse(final Authentication auth, final HttpServletResponse response) {
		response.addHeader(ConstantsConfig.HEADER_STRING, ConstantsConfig.TOKEN_PREFIX + buildJwtToken(auth));
		response.addHeader(ConstantsConfig.HEADER_ACCESS, ConstantsConfig.HEADER_STRING);
	}
	
	@Override
	public Authentication decodeJwtToken(final String jwtToken) {
		Claims jwsClaims = Jwts.parser().setSigningKey(ConstantsConfig.SECRET.getBytes())
				.parseClaimsJws(jwtToken.replace(ConstantsConfig.TOKEN_PREFIX, "")).getBody();

		String user = jwsClaims.getSubject();
		
		@SuppressWarnings("unchecked")
		List<String> privileges = jwsClaims.get("authorities", List.class);
		Set<GrantedAuthority> authorities = privileges.stream().map(p -> new SimpleGrantedAuthority(p))
				.collect(Collectors.toSet());
			
		if (user != null) {
			return new UsernamePasswordAuthenticationToken(user, null, authorities);
		}

		return null;
	}
	
	private String buildJwtToken(final Authentication auth) {
		Claims claims = Jwts.claims().setSubject(auth.getName());
		claims.put("authorities", auth.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));

		return Jwts.builder().setClaims(claims)
				.setExpiration(new Date(System.currentTimeMillis() + ConstantsConfig.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, ConstantsConfig.SECRET.getBytes()).compact();
	}
}
