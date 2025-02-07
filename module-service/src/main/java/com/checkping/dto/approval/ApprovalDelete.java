package com.checkping.dto.approval;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalDelete {

    @Getter
    public static class Response {
        /*
        id : 삭제된 결재 ID
         */
        private Long id;
    }
}
