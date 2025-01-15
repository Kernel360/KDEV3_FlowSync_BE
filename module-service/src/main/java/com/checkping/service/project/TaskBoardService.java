package com.checkping.service.project;

import com.checkping.dto.TaskBoardRegister.Request;
import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardResponse.TaskBoardItemDto;
import com.checkping.dto.TaskBoardResponse.TaskBoardListDto;
import com.checkping.dto.TaskBoardUpdate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface TaskBoardService {
    TaskBoardItemDto register(Request request, List<MultipartFile> fileList);
    List<TaskBoardListDto> getTaskBoardList(TaskBoardRequest.SearchCondition searchCondition);
    TaskBoardItemDto getTaskBoardById(Long taskBoardId);
    TaskBoardListDto deleteSoft (Long taskBoardId);
    TaskBoardListDto deleteHard(Long taskBoardId);
    TaskBoardItemDto update(Long taskBoardId, TaskBoardUpdate.Request request);
}
