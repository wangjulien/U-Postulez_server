package com.obbo.edu.upostulez.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.obbo.edu.upostulez.domain.User;

/**
 * Interface ext. JapRepository de DAO 
 * 
 * @author JW
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
	
	public Optional<User> findByEmail(String email);
}
