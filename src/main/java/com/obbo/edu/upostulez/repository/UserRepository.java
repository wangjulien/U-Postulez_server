package com.obbo.edu.upostulez.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.obbo.edu.upostulez.domain.User;

/**
 * Interface ext. JapRepository de DAO Employee (Abstract parent de Conseiller et Gerant)
 * 
 * @author JW
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByEmail(String email);
}
