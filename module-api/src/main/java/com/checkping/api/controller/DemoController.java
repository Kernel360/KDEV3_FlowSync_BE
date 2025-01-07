package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final DemoService demoService;

    @GetMapping("/member")
    public BaseResponse<String> getMember() {
        return BaseResponse.success("merber", "멤버 조회 성공");
    }

    @GetMapping("/save")
    public String save() {
        return demoService.save();
    }

    @GetMapping("/find")
    public String find() {
        return demoService.find();
    }

    @GetMapping("/exception")
    public String exception() throws Exception {
        return demoService.exception();
    }

    @GetMapping("/customException")
    public String customException() throws Exception {
        return demoService.customException();
    }
}
