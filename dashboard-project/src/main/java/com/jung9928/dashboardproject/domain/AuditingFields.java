package com.jung9928.dashboardproject.domain;

import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
// abstract를 사용하여 추상클래스로화 하여 스스로 엔티티가 될 순 없지만 다른 엔티티가 상속을 했을 때, 상속 받은 클래스 내에 추가 필드로 들어가게 됨으로써
// 자식 클래스의 사용 목적에 좀 더 어울리도록 함.
public abstract class AuditingFields {

    /**
     * JpaAuditing 어노테이션을 사용함으로써 업데이트 시 마다
     * 자동으로 게시글 작성자와 작성일시 데이터를 실시간으로 저장하게되고
     * 최초 insert 시 자동으로 insert 작업 수행.
     * */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;    // 생성일시

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 100)
    private String createdBy;           // 생성자

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;   // 수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy;          // 수정자
}
