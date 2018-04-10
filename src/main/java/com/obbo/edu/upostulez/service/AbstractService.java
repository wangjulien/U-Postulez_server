package com.obbo.edu.upostulez.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractService<T extends Serializable> implements ICrudService<T> {

    // read - one

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(final Long id) {
        return getDao().findById(id);
    }

    // read - all

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
    	List<T> allList = new ArrayList<>();
        getDao().findAll().forEach(allList::add);
        return allList;
    }

    @Override
    public Page<T> findPaginated(final int page, final int size) {
        return getDao().findAll(PageRequest.of(page, size));
    }

    // write

    @Override
    public T create(final T entity) {
        return getDao().save(entity);
    }

    @Override
    public T update(final T entity) {
        return getDao().save(entity);
    }

    @Override
    public void delete(final T entity) {
        getDao().delete(entity);
    }

    @Override
    public void deleteById(final Long entityId) {
        getDao().deleteById(entityId);
    }

    protected abstract PagingAndSortingRepository<T, Long> getDao();
}
