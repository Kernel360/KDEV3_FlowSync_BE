package com.checkping.dto.question;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class QuestionContent {
    /*
    type : content 타입 (String)
    data : content 내용 (Object)
     */

    private String type;
    private Object data;

    @JsonCreator
    public QuestionContent(
        @JsonProperty("type") String type,
        @JsonProperty("data") Object data) {
        this.type = type;
        this.data = data;
    }
}
