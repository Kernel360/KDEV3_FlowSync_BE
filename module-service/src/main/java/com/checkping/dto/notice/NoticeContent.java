package com.checkping.dto.notice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeContent {
    /*
    type : content 타입
    data : content 내용
     */

    private String type;
    private Object data;

    @JsonCreator
    public NoticeContent(
            @JsonProperty("type") String type,
            @JsonProperty("data") Object data){
        this.type = type;
        this.data = data;
    }
}