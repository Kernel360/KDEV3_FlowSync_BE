package com.checkping.info.approval;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ApprovalCountProjection {
    private Long id;
    private String title;
    private String value;
    private Long count;
    private String status;
    private Integer stepOrder;
    private String color;

    @QueryProjection
    public ApprovalCountProjection(Long id, String title, String value, Long count, String status, Integer stepOrder, String color) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.count = count;
        this.status = status;
        this.stepOrder = stepOrder;
        this.color = color;
    }
}
