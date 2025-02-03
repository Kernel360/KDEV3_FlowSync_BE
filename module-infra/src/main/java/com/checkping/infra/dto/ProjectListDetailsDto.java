package com.checkping.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectListDetailsDto {
    private Long id;
    private String name;
    private String description;
    private String detail;
    private String status;
    private String managementStep;
    private Date regAt;
    private Date updateAt;
    private Date startAt;
    private Date closeAt;
    private String deletedYn;
    private Long devOwnerId;
    private String developerName;
    private String customerName;
    private Long clickable;
}
