package com.checkping.domain.notice;

import com.checkping.domain.BaseEntity;
import com.checkping.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Notice extends BaseEntity {

    /*
        id : 공지사항 아이디
        title : 공지사항 제목
        content : 공지사항 본문
        category : 공지사항 카테고리
        priority : 공지사항 중요도
        reg_at : 작성 일시
        updated_at : 수정 일시
        admin_id : 관리자 아이디
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @CreatedDate
    @Column(name = "reg_at", nullable = false)
    private LocalDateTime regAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Getter
    @RequiredArgsConstructor
    public enum Category {
        SERVICE_UPDATE("서비스 업데이트"),
        POLICY_CHANGE("정책 변경"),
        MAINTENANCE("점검 안내"),
        OTHER("기타");

        private final String descrption;

    }
    @Getter
    @RequiredArgsConstructor
    public enum Priority {
        EMERGENCY("긴급"),
        NORMAL("일반");

        private final String descrption;
    }

    public void updateNotice(String title, String content,String category, String priority){
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (category != null) this.category = Category.valueOf(category);
        if (priority != null) this.priority = Priority.valueOf(priority);
    }

    public void updatePriority(Notice.Priority priority) {
        this.priority = priority;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }
}
