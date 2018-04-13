package com.obbo.edu.upostulez.protocol;

/**
 * Les constants utilise dans les DB entities
 * 
 * @author Jiliang.WANG
 *
 */
public final class AppComProtocol {
	private AppComProtocol() {
		throw new AssertionError("Instantiation not allowed!");
	}
	
	public static enum TokenState {
		TOKEN_INVALID,
		TOKEN_EXPIRED,
		TOKEN_VALID;		
	}
}
