package com.obbo.edu.upostulez.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface IOperations<T extends Serializable> {
	
	// read - one

	Optional<T> findById(final Long id);

	// read - all

	List<T> findAll();

	Page<T> findPaginated(int page, int size);

	// write

	T create(final T entity);

	T update(final T entity);

	void delete(final T entity);

	void deleteById(final Long entityId);
}
