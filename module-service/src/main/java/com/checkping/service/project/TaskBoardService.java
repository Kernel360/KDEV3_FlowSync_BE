package com.checkping.service.project;

import com.checkping.dto.TaskBoardGetList.Response;
import com.checkping.dto.TaskBoardRegister.Request;
import com.checkping.dto.TaskBoardRequest.SearchCondition;
import com.checkping.dto.TaskBoardResponse.TaskBoardItemDto;
import com.checkping.dto.TaskBoardUpdate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface TaskBoardService {
    TaskBoardItemDto register(Request request, List<MultipartFile> fileList);
    List<Response> getTaskBoardList(SearchCondition searchCondition);
    TaskBoardItemDto getTaskBoardById(Long taskBoardId);
    Response deleteSoft (Long taskBoardId);
    Response deleteHard(Long taskBoardId);
    TaskBoardItemDto update(Long taskBoardId, TaskBoardUpdate.Request request);
}
