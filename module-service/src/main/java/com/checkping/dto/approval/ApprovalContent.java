package com.checkping.dto.approval;

import com.checkping.exception.approval.ApprovalContentParsingException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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


    /**
     * content 를 List<ApprovalContent> 로 변환하는 메서드
     *
     * @param content content
     * @return List<ApprovalContent>
     */
    public static List<ApprovalContent> toContentList(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(content, new TypeReference<List<ApprovalContent>>() {
            });
        } catch (Exception e) {
            throw new ApprovalContentParsingException();
        }
    }
}
