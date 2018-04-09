package com.obbo.edu.upostulez.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.obbo.edu.upostulez.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
