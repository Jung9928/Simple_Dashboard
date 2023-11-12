package com.jung9928.dashboardproject.repository;

import com.jung9928.dashboardproject.domain.Article;
import com.jung9928.dashboardproject.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle>
{
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);

        // 제목, 내용, 해시태그, 작성일시, 작성자로 검색 가능하도록 함.
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);

        // likeIgnoreCase의 경우, 개발자가 직접 %를 작성해줘야 하는 특징이 있음. (%를 수동으로 넣을지 안넣을지 정하고 싶을 때 사용하면 될 듯)
        //bindings.bind(root.title).first(StringExpression::likeIgnoreCase);      // 쿼리 생성 시, like '${v]'
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);      // 쿼리 생성 시, like '%${v}%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
