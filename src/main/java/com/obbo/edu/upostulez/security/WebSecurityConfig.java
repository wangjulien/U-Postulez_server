package com.obbo.edu.upostulez.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.obbo.edu.upostulez.config.ConstantsConfig;

/**
 * Classe de configuration Spring Security - filtrage des acces selon ROLE -
 * configurer AuthenticationManagerBuilder pour verifier password utilisateur
 * logge
 * 
 * @author JW
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackageClasses = CustomUserDetailsService.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${security.cors.permit-host}")
	private String corsPermitHost;

	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	private static final String DEV_PORT = ":4200";
	private static final String HTTPS_PORT = ":8443";

	@Autowired
	private UserDetailsService customUserDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, ConstantsConfig.SIGN_UP_URL).permitAll()
			.anyRequest().authenticated()
			.and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
			.and()
			.addFilter(jwtAuthenticationFilter())
			.addFilter(jwtAuthorizationFilter())
			// this disables session creation on Spring Security
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}


	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConf = new CorsConfiguration();

		// Dev Option
//		corsConf.addAllowedOrigin("*");
		
		corsConf.setAllowedOrigins(Arrays.asList(HTTP + corsPermitHost + DEV_PORT, HTTPS + corsPermitHost,
				HTTPS + corsPermitHost + HTTPS_PORT));
		corsConf.setAllowedMethods(
				Arrays.asList(HttpMethod.GET.toString(), HttpMethod.POST.toString(), HttpMethod.DELETE.toString()));
		
		corsConf.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request will fail with 403 Invalid CORS request
		corsConf.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConf);
		
		return source;
	}
	
	@Bean
	public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		return new JWTAuthenticationFilter(authenticationManager());
	}
	
	@Bean
	public JWTAuthorizationFilter jwtAuthorizationFilter() throws Exception {
		return new JWTAuthorizationFilter(authenticationManager());
	}
}