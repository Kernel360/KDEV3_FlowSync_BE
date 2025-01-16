package com.checkping.service.project;

import com.checkping.dto.TaskBoardGetItem;
import com.checkping.dto.TaskBoardGetList;
import com.checkping.dto.TaskBoardRegister.Request;
import com.checkping.dto.TaskBoardRequest.SearchCondition;
import com.checkping.dto.TaskBoardUpdate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface TaskBoardService {
    TaskBoardGetItem.Response register(Request request, List<MultipartFile> fileList);
    List<TaskBoardGetList.Response> getTaskBoardList(SearchCondition searchCondition);
    TaskBoardGetItem.Response getTaskBoardById(Long taskBoardId);
    TaskBoardGetList.Response deleteSoft (Long taskBoardId);
    TaskBoardGetList.Response deleteHard(Long taskBoardId);
    TaskBoardGetItem.Response update(Long taskBoardId, TaskBoardUpdate.Request request);
}
