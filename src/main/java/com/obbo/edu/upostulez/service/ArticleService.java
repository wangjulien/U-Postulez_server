package com.obbo.edu.upostulez.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obbo.edu.upostulez.domain.Article;
import com.obbo.edu.upostulez.repository.ArticleRepository;

@Service
@Transactional
public class ArticleService extends AbstractService<Article> implements IArticleService {
	
	@Autowired
	private ArticleRepository dao;
	

	@Override
	protected PagingAndSortingRepository<Article, Long> getDao() {
		return dao;
	}

	@Override
	public Page<Article> findPaginated(Pageable pageable) {
		return dao.findAll(pageable);
	}
}
