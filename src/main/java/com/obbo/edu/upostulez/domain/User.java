package com.obbo.edu.upostulez.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * Entity abstract Employee (strategy JOINED TABLE)
 * 
 * @author JW
 *
 */

@JsonIgnoreProperties(value = { "password" }, allowSetters = true)
@Entity
@Table(name = "user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7594618877533202094L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String firstName;
	private String lastName;

	@Embedded
	private Adresse adresse;

	@Column(unique = true)
	private String email;
	private String password;

	@ManyToMany
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Role> roles;

	public User() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void addRole(Role role) {
		role.addUser(this);
		this.roles.add(role);
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final User user = (User) obj;
		return email.equals(user.email);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("User [firstName=").append(firstName).append("]").append("[lastName=").append(lastName)
				.append("]").append("[username").append(email).append("]");
		return builder.toString();
	}
}