package com.checkping.infra.repository.notice;

import com.checkping.domain.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 삭제되지 않은 공지사항을 ID로 조회
    Optional<Notice> findByIdAndIsDeletedFalse(Long noticeid);

    // 삭제되지 않은 긴급 공지사항의 갯수를 조회
    long countByPriorityAndIsDeletedFalse(Notice.Priority priority);

    // 키워드와 카테고리를 기반으로 공지사항 목록 조회
    @Query("SELECT n FROM Notice n WHERE (:category IS NULL OR n.category = :category) " +
            "AND (:keyword IS NULL OR n.title LIKE %:keyword% OR n.content LIKE %:keyword%) " +
            "AND n.isDeleted = false " +
            "ORDER BY " +
            "CASE WHEN n.priority = 'EMERGENCY' THEN 1 ELSE 2 END, " +  // 긴급 공지 우선 정렬
            "n.regAt DESC")
    Page<Notice> findSortedNotices(@Param("keyword") String keyword,
                                   @Param("category") Notice.Category category,
                                   Pageable pageable);

    @Query("SELECT n FROM Notice n WHERE (:category IS NULL OR n.category = :category) " +
            "AND (:keyword IS NULL OR n.title LIKE %:keyword% OR n.content LIKE %:keyword%) " +
            "ORDER BY " +
            "CASE WHEN n.priority = 'EMERGENCY' THEN 1 ELSE 2 END, " +  // 긴급 공지 우선 정렬
            "n.regAt DESC")
    Page<Notice> findSortedNoticesWithoutIsDeleted(@Param("keyword") String keyword,
                                                   @Param("category") Notice.Category category,
                                                   Pageable pageable);

}
