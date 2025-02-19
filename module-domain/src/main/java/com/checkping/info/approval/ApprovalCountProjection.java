package com.checkping.info.approval;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ApprovalCountProjection {
    private Long id;
    private String title;
    private String description;
    private Long count;
    private String status;
    private Integer stepOrder;
    private String color;

    @QueryProjection
    public ApprovalCountProjection(Long id, String title, String description, Long count, String status, Integer stepOrder, String color) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.count = count;
        this.status = status;
        this.stepOrder = stepOrder;
        this.color = color;
    }
}
