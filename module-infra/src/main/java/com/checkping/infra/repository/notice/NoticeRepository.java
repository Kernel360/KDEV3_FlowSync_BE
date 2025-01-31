package com.checkping.infra.repository.notice;

import com.checkping.domain.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {


    // 삭제되지 않은 공지사항 중 ID로 조회
    Optional<Notice> findByIdAndIsDeletedFalse(Long noticeid);

    // 페이징
    Page<Notice> findAllByIsDeletedFalse(Pageable pageable);

    // 키워드 기반으로 공지사항을 조회하는 메서드
    Page<Notice> findByTitleContainingOrContentContainingAndIsDeletedFalse(String title, String content, Pageable pageable);

    // 카테고리로 검색
    Page<Notice> findByCategoryAndIsDeletedFalse(Notice.Category category, Pageable pageable);

    // 카테고리와 키워드로 검색
    Page<Notice> findByPriorityAndTitleContainingOrContentContainingAndIsDeletedFalse(
            Notice.Priority priority, String title, String content, Pageable pageable);

    // 긴급 공지사항 조회
    Page<Notice> findByPriorityAndIsDeletedFalse(Notice.Priority priority, Pageable pageable);

    // 검색 시 긴급 공지사항 조회
    Page<Notice> findByCategoryAndPriorityAndTitleContainingOrContentContainingAndIsDeletedFalse(
            Notice.Category category, Notice.Priority priority, String title, String content, Pageable pageable);
}
