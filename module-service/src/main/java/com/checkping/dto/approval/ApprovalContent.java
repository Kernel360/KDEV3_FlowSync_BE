package com.checkping.dto.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ApprovalContent {

    /*
    type : content 유형
    data : content 의 내용
     */
    private String type;
    private Object data;

    @JsonCreator
    public ApprovalContent(@JsonProperty("type") String type, @JsonProperty("data") Object data) {
        this.type = type;
        this.data = data;
    }
}
