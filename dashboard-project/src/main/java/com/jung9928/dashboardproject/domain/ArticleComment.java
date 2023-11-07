package com.jung9928.dashboardproject.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@EqualsAndHashCode     // 게시글을 list에 담아서 게시글 화면에 뿌려준다거나 List 컬렉션으로 비교 로직을 작성할 때 필요
@ToString
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})

@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private Article article;            // 게시글 (ID)
    @Setter @Column(nullable = false, length = 500) private String content;             // 본문

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;    // 생성일시

    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy;           // 생성자

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;   // 수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy;          // 수정자

    protected ArticleComment() {}

    public ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment articleComment)) return false;
        return id != null && id.equals(articleComment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}