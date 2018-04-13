package com.obbo.edu.upostulez.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.domain.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	
	Optional<VerificationToken> findByToken(String token);

	Optional<VerificationToken> findByUser(User user);

	Stream<VerificationToken> findAllByExpiryDateLessThan(LocalDateTime now);

	void deleteByExpiryDateLessThan(LocalDateTime now);

	@Modifying
	@Query("delete from VerificationToken t where t.expiryDate <= ?1")
	void deleteAllExpiredSince(Date now);
}
