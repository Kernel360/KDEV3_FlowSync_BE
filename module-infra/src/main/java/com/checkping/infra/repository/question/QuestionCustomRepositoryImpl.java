package com.checkping.infra.repository.question;

import static com.checkping.domain.question.QQuestion.question;
import static java.util.Optional.ofNullable;

import com.checkping.domain.question.Question;
import com.checkping.info.question.QuestionSearchInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

    private final JPAQueryFactory queryFactory;

    public QuestionCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Question> getByCondition(Long projectId,
        QuestionSearchInfo.SearchCondition searchCondition, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        // 필수 조건 : 프로젝트 ID
        builder.and(question.project.id.eq(projectId));

        // 삭제 상태 조건
        if (!searchCondition.adminSearch()) {
            builder.and(question.deletedYn.eq(Question.DeleteStatus.N));
        }

        // 검색어 조건
        if (StringUtils.hasText(searchCondition.keyword())) {
            builder.and(question.title.containsIgnoreCase(searchCondition.keyword()));
        }

        // 상태 조건
        if (searchCondition.status() != null) {
            builder.and(question.status.eq(searchCondition.status()));
        }

        // 진행 단계 조건
        if (searchCondition.progressId() != null) {
            builder.and(question.progressStep.id.eq(searchCondition.progressId()));
        }

        // 총 개수 조회
        long totalCount = ofNullable(queryFactory.select(question.count()).from(question)
            .where(builder).fetchOne()).orElse(0L);

        // 페이징된 데이터 조회
        List<Question> questions = queryFactory.selectFrom(question)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(questions, pageable, totalCount);
    }
}
