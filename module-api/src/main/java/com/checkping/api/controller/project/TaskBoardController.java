package com.checkping.api.controller.project;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.TaskBoardCommentRequest;
import com.checkping.dto.TaskBoardCommentResponse;
import com.checkping.dto.TaskBoardGetList;
import com.checkping.dto.TaskBoardGetList.Response;
import com.checkping.dto.TaskBoardRegister;
import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardRequest.SearchCondition;
import com.checkping.dto.TaskBoardResponse;
import com.checkping.dto.TaskBoardResponse.TaskBoardItemDto;
import com.checkping.dto.TaskBoardUpdate.Request;
import com.checkping.service.project.TaskBoardCommentService;
import com.checkping.service.project.TaskBoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskBoardController implements TaskBoardApi {

    private final TaskBoardService taskBoardService;
    private final TaskBoardCommentService taskBoardCommentService;

    @PostMapping(value = "/posts", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public BaseResponse<TaskBoardItemDto> register(
        @RequestPart(value = "content") TaskBoardRegister.Request request,
        @RequestPart(required = false, value = "fileList") List<MultipartFile> fileList) {

        TaskBoardItemDto taskBoardDto = taskBoardService.register(request, fileList);

        return BaseResponse.success(taskBoardDto);
    }

    @GetMapping("/posts")
    @Override
    public BaseResponse<List<Response>> getTaskBoardList(
        @RequestParam(required = false) String boardCategory,
        @RequestParam(required = false) String boardStatus) {

        // RequestParam -> SearchCondition
        TaskBoardRequest.SearchCondition searchCondition = new SearchCondition(boardCategory,
            boardStatus);

        // getTaskBoardList
        List<TaskBoardGetList.Response> taskBoardListDtoList = taskBoardService.getTaskBoardList(
            searchCondition);

        return BaseResponse.success(taskBoardListDtoList);
    }

    @GetMapping("/posts/{postId}")
    @Override
    public BaseResponse<TaskBoardItemDto> getTaskBoard(@PathVariable Long postId) {

        TaskBoardItemDto taskBoardItemDto = taskBoardService.getTaskBoardById(postId);

        return BaseResponse.success(taskBoardItemDto);
    }

    @PutMapping("/posts/{postId}")
    @Override
    public BaseResponse<TaskBoardItemDto> updateTaskBoard(@PathVariable Long postId,
        @RequestBody Request request) {

        TaskBoardResponse.TaskBoardItemDto updatedBoardDto = taskBoardService.update(postId,
            request);

        return BaseResponse.success(updatedBoardDto);
    }

    @DeleteMapping("/posts/{postId}")
    @Override
    public BaseResponse<Response> deleteSoftTaskBoard(
        @PathVariable Long postId) {

        TaskBoardGetList.Response deletedBoardDto = taskBoardService.deleteSoft(postId);

        return BaseResponse.success(deletedBoardDto);
    }

    @PostMapping("/posts/{postId}/comments")
    @Override
    public BaseResponse<TaskBoardCommentResponse.TaskBoardCommentDto> registerComment(
        @PathVariable Long postId, @RequestBody TaskBoardCommentRequest.RegisterDto request) {

        TaskBoardCommentResponse.TaskBoardCommentDto taskBoardCommentDto = taskBoardCommentService.register(
            postId, request);

        return BaseResponse.success(taskBoardCommentDto);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @Override
    public BaseResponse<TaskBoardCommentResponse.TaskBoardCommentDto> deleteSoft(
        @PathVariable Long postId, @PathVariable Long commentId) {

        TaskBoardCommentResponse.TaskBoardCommentDto deletedCommentDto = taskBoardCommentService.deleteSoft(
            postId, commentId);

        return BaseResponse.success(deletedCommentDto);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    @Override
    public BaseResponse<TaskBoardCommentResponse.TaskBoardCommentDto> updateComment(
        @PathVariable Long postId, @PathVariable Long commentId,
        @RequestBody TaskBoardCommentRequest.UpdateDto request) {

        TaskBoardCommentResponse.TaskBoardCommentDto updatedCommentDto = taskBoardCommentService.update(
            postId, commentId, request);

        return BaseResponse.success(updatedCommentDto);
    }
}
