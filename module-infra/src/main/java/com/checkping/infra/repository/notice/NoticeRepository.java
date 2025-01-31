package com.checkping.infra.repository.notice;

import com.checkping.domain.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 삭제되지 않은 모든 공지사항 조회
    List<Notice> findAllByIsDeletedFalse();

    // 삭제되지 않은 공지사항 중 ID로 조회
    Optional<Notice> findByIdAndIsDeletedFalse(Long noticeid);

    // 페이징
    Page<Notice> findAllByIsDeletedFalse(Pageable pageable);

    // 키워드로 검색
    Page<Notice> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    // 카테고리로 검색
    Page<Notice> findByCategory(Notice.Category category, Pageable pageable);

    // 카테고리와 키워드로 검색
    Page<Notice> findByCategoryAndTitleContainingOrContentContaining(Notice.Category category, String title, String content, Pageable pageable);



}
