package com.checkping.common.utils;

import com.checkping.common.exception.BaseException;
import java.util.function.Supplier;

public abstract class AbstractAuthorizationValidator {

    /**
     * 유효성 검증 실행 코드 : 람다로 해당 조건을 실행하도록 하였음
     *
     * @param condition 조건 결과 : false 일 경우 예외 발생
     * @param exception 예외
     */
    protected void validate(boolean condition, Supplier<? extends BaseException> exception) {
        if (!condition) {
            throw exception.get();
        }
    }
}
