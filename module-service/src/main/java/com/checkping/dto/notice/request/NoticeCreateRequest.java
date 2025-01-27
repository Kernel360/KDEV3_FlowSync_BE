package com.checkping.dto.notice.request;


import com.checkping.domain.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeCreateRequest {
    private Long adminId;

    private String title;

    private String content;

    private String category;

    private String priority;

    public Notice toEntity(){
        return Notice.builder()
                .adminId(adminId)
                .title(title)
                .content(content)
                .category(Notice.Category.valueOf(category))
                .priority(Notice.Priority.valueOf(priority))
                .build();
    }
}
