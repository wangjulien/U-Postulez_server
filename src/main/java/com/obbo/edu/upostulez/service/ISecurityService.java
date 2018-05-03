package com.obbo.edu.upostulez.service;

public interface ISecurityService {

	String validatePasswordResetToken(long id, String token);

}
