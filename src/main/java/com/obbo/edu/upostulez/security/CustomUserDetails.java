package com.obbo.edu.upostulez.security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Classe implement interface UserDetail (Spring Security) et encapsule l'utilisateur logge (Employee)
 * pour acceder les attributs ID, NOM, PRENOM etc
 * 
 * @author JW
 *
 */
public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
		
	private String username;
	private String password;
	
	private Set<GrantedAuthority> setAuths;
	
	public CustomUserDetails() {
		super();
	}

	public CustomUserDetails(String username, String password, Set<GrantedAuthority> setAuths) {
		this.username = username;
		this.password = password;
		this.setAuths = setAuths;		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return setAuths;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}
}
