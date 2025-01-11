package com.checkping.service;

import com.checkping.domain.project.Project;
import com.checkping.dto.ProjectRequest;
import com.checkping.dto.ProjectResponse;
import com.checkping.infra.repository.project.ProjectRepository;
import com.checkping.service.project.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
public class ProjectServiceTests {
    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void resisterProject() {

        projectService.registerProject(
                ProjectRequest.ResisterDto.builder()
                        .name("이름")
                        .description("설명")
                        .detail("상세설명")
                        .status(String.valueOf(Project.Status.IN_PROGRESS))
                        .closeAt(LocalDate.parse("2025-12-30").atStartOfDay())
                        .resisterId(49283L)
                        .build());

        List<Project> projects = projectRepository.findAll();
        projects.forEach(project -> System.out.println(project));
        System.out.println(projects.size());
    }

    @Test
    public void deleteProject() {
        projectService.registerProject(
                ProjectRequest.ResisterDto.builder()
                        .name("이름")
                        .description("설명")
                        .detail("상세설명")
                        .status(String.valueOf(Project.Status.IN_PROGRESS))
                        .closeAt(LocalDate.parse("2025-12-30").atStartOfDay())
                        .resisterId(49283L)
                        .build());

        Project project = projectRepository.findById(1L).get();
        System.out.println("resisterProject : " + project);

        ProjectResponse.ProjectDto projectDto = projectService.deleteProject(1L);
        System.out.println("deleteProject : " + projectDto);
    }

}