package com.obbo.edu.upostulez.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.obbo.edu.upostulez.protocol.DbEntityProtocol.PrivilegeName;

@Entity
public class Privilege {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private PrivilegeName name;
 
//    @ManyToMany(mappedBy = "privileges")
//    private Set<Role> roles = new HashSet<>();
    
    public Privilege() {
		super();
	}

	public Privilege(PrivilegeName name) {
		super();
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PrivilegeName getName() {
		return name;
	}

	public void setName(PrivilegeName name) {
		this.name = name;
	}

//	public Set<Role> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(Set<Role> roles) {
//		this.roles = roles;
//	}   
}