package com.checkping.domain.question;

import com.checkping.domain.BaseEntity;
import com.checkping.domain.member.Member;
import com.checkping.domain.project.ProgressStep;
import com.checkping.domain.project.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "question")
@Entity
public class Question extends BaseEntity {

    /*
    id : id
    number : 게시글 번호
    title : 게시글 제목
    content : 게시글 본문
    regAt : 작성 일시
    editAt : 수정 일시
    approverAt : 승인 일시
    category : 게시글 유형
    status : 게시글 상태
    deletedYn : 삭제 여부
    parent : 부모 게시글
    register : 작성자 (register_id)
    updater : 수정자 (updater_id)
    project : 프로젝트 (project_id)
    progress_step : 진행 단계 (progress_step_id)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Builder.Default
    @Column(name = "number", nullable = false)
    private Integer number = 1;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @CreatedDate
    @Column(name = "reg_at", nullable = false)
    private LocalDateTime regAt;

    @LastModifiedDate
    @Column(name = "edit_at")
    private LocalDateTime editAt;

    @Column(name = "approver_at")
    private LocalDateTime approverAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 100)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 100)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "deleted_yn", nullable = false)
    private DeleteStatus deletedYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Question parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id", nullable = false)
    private Member register;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id")
    private Member updater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_step_id", nullable = false)
    private ProgressStep progressStep;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<QuestionComment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<QuestionLink> questionLinkList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<QuestionFile> questionFileList = new ArrayList<>();

    @Getter
    @RequiredArgsConstructor
    public enum Category {
        QUESTION("질문"), ANSWER("답변");
        private final String description;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        WAIT("대기"), RETURNING("반려"), COMPLETED("승인");
        private final String description;
    }

    @Getter
    @RequiredArgsConstructor
    public enum DeleteStatus {
        Y("비활성화"), N("활성화");
        private final String description;
    }

    // soft delete 적용 = 게시글 비활성화
    public void deactivate() {
        this.deletedYn = DeleteStatus.Y;
    }

    // soft delete 해제 = 게시글 활성화
    public void activate() {
        this.deletedYn = DeleteStatus.N;
    }

    // update status
    public void updateStatus(Status status) {
        this.status = status;
    }

    // update category
    public void updateCategory(Category category) {
        this.category = category;
    }

    // update
    public void update(String title, String content) {

        if (!this.title.equals(title)) {
            this.title = title;
        }

        if (!this.content.equals(content)) {
            this.content = content;
        }
    }

    // ADD QuestionLink
    public void addLink(QuestionLink link) {
        // null check & contains check
        if (link != null && !this.questionLinkList.contains(link)) {
            this.questionLinkList.add(link);
        }
    }

    // ADD QuestionFile
    public void addFile(QuestionFile file) {

        // null check & contains check
        if (file != null && !this.questionFileList.contains(file)) {

            // Add
            this.questionFileList.add(file);
        }
    }

    // ADD QuestionFile List
    public void addFile(List<QuestionFile> fileList) {
        // loop for Add
        for (QuestionFile file : fileList) {

            // Add QuestionFile
            addFile(file);
        }
    }
}
