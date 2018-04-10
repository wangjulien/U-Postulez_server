package com.obbo.edu.upostulez.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.obbo.edu.upostulez.domain.Article;

public interface IArticleService extends ICrudService<Article> {
	Page<Article> findPaginated(Pageable pageable);
}
