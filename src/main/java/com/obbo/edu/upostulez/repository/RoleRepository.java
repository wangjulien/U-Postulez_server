package com.obbo.edu.upostulez.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.obbo.edu.upostulez.domain.Role;
import com.obbo.edu.upostulez.protocol.DbEntityProtocol.RoleName;

/**
 * Interface ext. JapRepository de DAO Employee (Abstract parent de Conseiller et Gerant)
 * 
 * @author JW
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	public Optional<Role> findByName(RoleName name);
}
