package com.obbo.edu.upostulez.protocol;

/**
 * Les constants utilise dans les DB entities
 * 
 * @author Jiliang.WANG
 *
 */
public final class DbEntityProtocol {
	private DbEntityProtocol() {
		throw new AssertionError("Instantiation not allowed!");
	}
	
	public static enum RoleName {
		ROLE_ADMIN,
		ROLE_TUTOR,
		ROLE_USER,
		ROLE_PRIME_USER;		
	}
	
	public static enum PrivilegeName {
		READ_PRIVILEGE,
		WRITE_PRIVILEGE;
	}

}
