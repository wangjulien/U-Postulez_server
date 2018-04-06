package com.obbo.edu.upostulez.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.obbo.edu.upostulez.domain.Privilege;
import com.obbo.edu.upostulez.protocol.DbEntityProtocol.PrivilegeName;

/**
 * Interface ext. JapRepository de DAO Employee (Abstract parent de Conseiller et Gerant)
 * 
 * @author JW
 *
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
	
	public Optional<Privilege> findByName(PrivilegeName name);
}
