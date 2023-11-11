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
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})

@Entity
public class Article extends AuditingFields {

    /**
     * 아래의 메타 데이터들(createdAt, createdBy, modifiedAt, modifiedBy)을 개발자가 임의로 셋팅하지 않도록 하기 위해
     * 자동으로 JPA가 값을 셋팅해줄 수 있도록 클래스 전체에 @setter 적용을 하지 않도록 함.
     * */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)           // -> 옵션 없을 시, Column 어노테이션 생략 가능(Transient 어노테이션이 없는 한, 컬럼으로 간주)
    private String title;               // 제목

    @Setter
    @Column(nullable = false, length = 10000)
    private String content;             // 본문

    @Setter
    private String hashtag;             // 해시태그

    // -> One To Many(양방향 바인딩)를 사용하여 article과 articleComment를 합쳐서 테이블을 생성하지 않도록 함.
    // cascade 사용 시, 데이터 마이그레이션 or 수정 작업 시, 불편하거나 데이터 소실 발생 가능성이 있음
    // ex) 논리 상, 게시글이 삭제되면 게시글과 관련된 댓글 또한 의미가 없으므로 삭제되어야 맞지만
    //     데이터 백업 목적으로 댓글이 필요할 수 도 있는 상황이 존재할 수 있음. -> 그러므로 cascade = delete가 아닌
    //     모든 경우에 대해서 cascding constraint를 위해 CascadeType.ALL 을 사용.

    // ToString 메소드를 여기서 끊어주는게 합리적이라 판단
    // => 댓글로부터 게시글을 참조하는 경우가 일반적인데 댓글리스트를 전부 조회하는 걸 여기서 하는 건 정상적이지 않으므로
    //    순환참조(연속적인 toString 메소드 호출)로 인한 OMM 방지를 위해 여기서 toString 메소드를 끊어준다.
    @ToString.Exclude
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    // 코드 밖에서 new 객체 생성 방지를 위해 protected로 생성자 작성
    protected Article() {}

    // id, 메타데이터를 제외한 생성자 초기화
    // 생성자에서 전부 셋팅할 필요가 없음 (도메인과 관련이 있는 정보만 open하는게 맞다고 보임)
    // ==> (게시글 생성 시, 자동으로 메타 데이터가 insert 되길 원함, id또한 자동 생성되길 원하기 때문)
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // new 키워드를 사용하지 않고 편리하게 쓸 수 있게 factory 생성자 메소드 작성
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;

        // 위 pattern matching을 적용한 코드로 대체
//        if (!(o instanceof Article)) return false;
//        Article article = (Article) o;

        // db 연결 전 즉, insert 하기 전 생성된 엔티티 id가 없으므로 null 방어 (동등성 검사 수행)
        // => 영속화되지 않은 생성된 모든 엔티티는 메타 데이터들이 같다 하더라도 각각 다른 값으로 취급하게 함.
        //
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
