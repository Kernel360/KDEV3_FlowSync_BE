package com.checkping.infra;

import com.checkping.domain.project.Project;
import com.checkping.infra.repository.project.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class ProjectRepositoryTests {
    @Autowired
    private ProjectRepository projectRepository;


    @Test
    public void init(){
        for (int i = 0; i < 10; i++) {
            Project project = Project.builder()
                    .name("이름" + Integer.toString(i))
                    .description("설명")
                    .detail("상세설명")
                    .status(Project.Status.IN_PROGRESS)
                    .regAt(LocalDateTime.now())
                    .closeAt(LocalDate.parse("2025-12-30").atStartOfDay())
                    .resisterId(49283L)
                    .build();
            projectRepository.save(project);
        }

        List<Project> projects = projectRepository.findAll();
        projects.forEach(project -> System.out.println(project));
        System.out.println(projects.size());
    }
}