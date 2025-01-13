package com.checkping.service;

import com.checkping.domain.member.Organization;
import com.checkping.domain.project.Project;
import com.checkping.dto.ProjectRequest;
import com.checkping.dto.ProjectResponse;
import com.checkping.infra.repository.member.OrganizationRepository;
import com.checkping.infra.repository.project.ProjectRepository;
import com.checkping.service.project.ProjectServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
public class ProjectServiceTests {
    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

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

    @Test
    public void updateProject() {

        LocalDateTime defaultCloseAt = LocalDate.parse("2025-12-30").atStartOfDay();
        Long resisterId = 49283L;

        for(int i=0; i<5; i++){
            ProjectResponse.ProjectDto resisterDto = projectService.registerProject(
                    ProjectRequest.ResisterDto.builder()
                            .name("이름" + i)
                            .description("설명")
                            .detail("상세설명")
                            .status(String.valueOf(Project.Status.IN_PROGRESS))
                            .closeAt(defaultCloseAt)
                            .resisterId(resisterId)
                            .build());

            System.out.println("resisterProject : " + resisterDto);
        }

        projectService.updateProject(2L,
                ProjectRequest.UpdateDto.builder()
                        .name("프로젝트이름")
                        .description("설명123")
                        .detail("상세설명")
                        .status(String.valueOf(Project.Status.PAUSED))
                        .closeAt(LocalDate.parse("2025-06-30").atStartOfDay())
                        .updaterId(resisterId)
                        .build());

        List<Project> list = projectRepository.findAll();

        for (int i = 0; i < list.size(); i++) {
            System.out.println("updateProject : " + list.get(i));
        }

    }

    @Test
    @Transactional
    public void findAllProject(){

        // 테스트 업체 추가
        Organization dev_organization = Organization.builder()
                .name("DEV_kernel")
                .type(Organization.Type.DEVELOPER)
                .status(Organization.Status.ACTIVE)
                .regAt(LocalDateTime.now())
                .build();
        organizationRepository.save(dev_organization);

        Organization cust_organization = Organization.builder()
                .name("CUST_kernel2")
                .type(Organization.Type.CUSTOMER)
                .status(Organization.Status.ACTIVE)
                .regAt(LocalDateTime.now())
                .build();
        organizationRepository.save(cust_organization);
        List<Organization> organizations = new ArrayList<>();

        // 테스트 프로젝트 추가
        Project project = Project.builder()
                .name("이름")
                .description("설명")
                .detail("상세설명")
                .status(Project.Status.IN_PROGRESS)
                .regAt(LocalDateTime.now())
                .closeAt(LocalDate.parse("2025-12-30").atStartOfDay())
                .resisterId(49283L)
                .organizations(organizations)
                .build();

        project.getOrganizations().add(dev_organization);
        project.getOrganizations().add(cust_organization);

        projectRepository.save(project);

        List<Project> projects = projectRepository.findAll();
        System.out.println("p : " + projects.get(0));

        List<ProjectResponse.ProjectDto> list = projectService.findAllProjects("이름","IN_PROGRESS");
        System.out.println(list);

    }
}