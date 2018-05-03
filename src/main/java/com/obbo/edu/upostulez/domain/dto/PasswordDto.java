package com.obbo.edu.upostulez.domain.dto;

import com.obbo.edu.upostulez.validation.ValidPassword;

public class PasswordDto {
	
	public PasswordDto() {
		super();
	}

	private String oldPassword;
	
	@ValidPassword
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
