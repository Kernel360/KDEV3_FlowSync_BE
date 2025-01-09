package com.checkping.dto;

import com.checkping.domain.project.Project;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProjectRequest {

    @Getter
    @ToString
    @Builder
    public static class ProjecResisterDto {

        /*
        id : 프로젝트 아이디
        name : 프로젝트 이름
        description : 프로젝트 설명
        detail : 프로젝트 세부 설명
        status : 프로젝트 상태 * IN_PROGRESS(진행중), PAUSED(일시 중단), COMPLETED(완료), DELETED(삭제)
        closeAt : 프로젝트 종료 일시
        resisterId : 등록자 아이디
        */

        private String name;
        private String description;
        private String detail;
        private String status;
        private LocalDateTime regAt;
        private LocalDateTime closeAt;
        private Long resisterId;

        public static Project toEntity(ProjecResisterDto resisterDto){
            return Project.builder()
                    .name(resisterDto.getName())
                    .description(resisterDto.getDescription())
                    .detail(resisterDto.getDetail())
                    .status(Project.Status.IN_PROGRESS)
                    .regAt(LocalDateTime.now())
                    .closeAt(resisterDto.getCloseAt())
                    .resisterId(resisterDto.getResisterId())
                    .build();
        }
    }
}
