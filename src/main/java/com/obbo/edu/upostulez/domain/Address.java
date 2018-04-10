package com.obbo.edu.upostulez.domain;

import javax.persistence.Embeddable;

/**
 * Entity adresse EMBEDDED dans Client et Employee
 * @author JW
 *
 */

@Embeddable
public class Address {
	
	private String rue;
	private int codePostal;
	private String ville;
	private String tel;
	
	public Address() {
		super();
	}

	public Address(String rue, int codePostal, String ville, String tel) {
		super();
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.tel = tel;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public int getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(int codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String toString() {
		return rue + ", " + codePostal + ", " + ville + " \nTel: " + tel;
	}
	
	
}
