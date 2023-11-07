package com.jung9928.dashboardproject.repository;

import com.jung9928.dashboardproject.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}