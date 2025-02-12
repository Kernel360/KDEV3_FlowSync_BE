package com.checkping.dto.project;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.project.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
public class ProjectSearchRequest {
    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("id", "name", "reg_at", "deadline_at", "close_at");

    @Schema(description = "검색 키워드")
    private String keyword;

    @Schema(description = "관리 단계")
    private String managementStep;

    @Schema(description = "현재 페이지", defaultValue = "1")
    private int currentPage = 1;

    @Schema(description = "페이지 사이즈", defaultValue = "10")
    private int pageSize = 10;

    @Schema(description = "정렬 필드", defaultValue = "id")
    private String sort = "id";

    @Schema(description = "정렬 순서 (asc, desc)", defaultValue = "desc")
    private String order = "desc";

    public ProjectSearchRequest(String keyword, String managementStep, int currentPage, int pageSize, String sort, String order) {
        this.keyword = keyword;
        this.currentPage = validatePage(currentPage)-1;
        this.pageSize = validatePage(pageSize);
        this.managementStep = validateManagementStep(managementStep);
        this.sort = validateSort(sort);
        this.order = validateOrder(order);

    }

    private int validatePage(int value) {
        if (value < 0) {
            throw new BaseException("페이지 값은 1 이상이어야 합니다.", ErrorCode.BAD_REQUEST);
        }
        return value;
    }

    private String validateManagementStep(String managementStep) {
        if (managementStep != null && !managementStep.trim().isEmpty()) {
            try {
                Project.ManagementStep stepEnum = Project.ManagementStep.valueOf(managementStep.toUpperCase());
                return stepEnum.name();
            } catch (IllegalArgumentException e) {
                throw new BaseException("유효하지 않은 관리 단계입니다.", ErrorCode.BAD_REQUEST);
            }
        }
        return managementStep;
    }

    private String validateSort(String sort) {
        if (sort != null && !sort.trim().isEmpty()) {
            if (!ALLOWED_SORT_FIELDS.contains(sort)) {
                throw new BaseException("유효하지 않은 정렬 필드입니다.", ErrorCode.BAD_REQUEST);
            }
            return sort;
        }
        return "id";
    }

    private String validateOrder(String order) {
        if (order != null && !order.trim().isEmpty()) {
            String orderUpper = order.toUpperCase();
            if (!("ASC".equals(orderUpper) || "DESC".equals(orderUpper))) {
                throw new BaseException("유효하지 않은 정렬 순서입니다. (asc, desc만 허용됩니다.)", ErrorCode.BAD_REQUEST);
            }
            return orderUpper;
        }
        return "DESC";
    }

}
