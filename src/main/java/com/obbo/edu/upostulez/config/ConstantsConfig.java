package com.obbo.edu.upostulez.config;

/**
 * Classs de configuration. a charger depuis un fichier . properties - Pour
 * generer automatiquement prefix
 * 
 * @author JW
 *
 */
public class ConstantsConfig {
	// Security constant

	public static final String SECRET = "SecretKeyToGenJWTs";
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String HEADER_ACCESS = "Access-Control-Expose-Headers";
	public static final String SIGN_UP_URL = "/users/sign-up";
}
