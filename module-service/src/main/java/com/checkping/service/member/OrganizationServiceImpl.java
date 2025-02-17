package com.checkping.service.member;

import com.checkping.common.dto.PageInfo;
import com.checkping.common.dto.PageMetaResponse;
import com.checkping.common.utils.FileRequest;
import com.checkping.domain.member.Organization;
import com.checkping.domain.member.projection.ProjectList;
import com.checkping.domain.project.Project;
import com.checkping.dto.*;
import com.checkping.exception.member.OrganizationAlreadyDeletedException;
import com.checkping.exception.member.OrganizationAlreadyExistEntityException;
import com.checkping.exception.member.OrganizationNotFoundEntityException;
import com.checkping.infra.repository.file.S3FileRepositoryImpl;
import com.checkping.infra.repository.member.ProjectQueryRepository;
import com.checkping.infra.repository.member.OrganizationRepository;
import com.checkping.service.member.util.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final ProjectQueryRepository projectQueryRepository;

    private final S3FileRepositoryImpl s3FileRepository;

    private final CurrentMemberUtil currentMemberUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrganizationCreate.Response createOrganization(OrganizationCreate.Request request, MultipartFile file) {

        request.setName(request.getName().strip());

        if (organizationRepository.findByNameAndType(request.getName(), request.getTypeEnum()).isPresent()) {
            throw new OrganizationAlreadyExistEntityException();
        }

        if (file != null) {
            FileRequest fileRequest = s3FileRepository.uploadFile(file);
            request.setBrCertificateUrl(fileRequest.saveName() + "|" + fileRequest.url());
        }

        Organization organization = OrganizationCreate.Request.toEntity(request);

        Organization createOrganization = organizationRepository.save(organization);

        return OrganizationCreate.Response.toDto(createOrganization);
    }

    @Transactional(readOnly = true)
    @Override
    public OrganizationListGet.Response getOrganization(Long id) {

        Optional<Organization> result = organizationRepository.findById(id);

        Organization organization = result.orElseThrow(OrganizationNotFoundEntityException::new);

        return OrganizationListGet.Response.toDto(organization);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo.Response<OrganizationListGet.Response> getListOrganization(
            String type, String status, String sortField, String sortDirection, PageInfo.Request pageRequest) {

        Pageable pageable = PageRequest.of(
                pageRequest.getCurrentPage() - 1,
                pageRequest.getPageSize(),
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));

        Organization.Type validType = checkType(type);
        Organization.Status validStatus = checkStatus(status);

        Page<Organization> result = organizationRepository.findByTypeAndStatus(
                validType,
                validStatus,
                pageRequest.getKeyword(),
                pageable);

        List<OrganizationListGet.Response> dtoList = result.getContent().stream().map(OrganizationListGet.Response::toDto).toList();
        PageMetaResponse meta = PageMetaResponse.fromPage(result);

        return PageInfo.Response.<OrganizationListGet.Response>builder()
                .dtoList(dtoList)
                .meta(meta.toMap())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrganizationUpdate.Response modifyOrganization(
            Long id,
            OrganizationUpdate.Request request,
            MultipartFile file
    ) {
        Optional<Organization> result = organizationRepository.findById(id);
        Organization organization = result.orElseThrow(OrganizationNotFoundEntityException::new);

        // 기존 파일이 있다면 삭제
        if (organization.getBrCertificateUrl() != null && !organization.getBrCertificateUrl().isEmpty()) {
            // 저장 파일명
            String saveName = organization.getBrCertificateUrl().split("\\|")[0];
            // 기존 파일 삭제
            s3FileRepository.deleteFile(saveName);
        }

        // 수정 파일 등록
        if (file != null) {
            FileRequest fileRequest = s3FileRepository.uploadFile(file);
            request.setBrCertificateUrl(fileRequest.saveName() + "|" + fileRequest.url());
        }

        organization.updateOrganization(
                request.getBrNumber(),
                request.getBrCertificateUrl(),
                request.getStreetAddress(),
                request.getDetailAddress(),
                request.getPhoneNumber()
        );

        Organization updateOrganization = organizationRepository.save(organization);

        OrganizationUpdate.Response.toDto(updateOrganization);

        return OrganizationUpdate.Response.toDto(updateOrganization);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrganizationDelete.Response removeOrganization(Long id, OrganizationDelete.Request request) {

        Optional<Organization> result = organizationRepository.findById(id);

        Organization organization = result.orElseThrow(OrganizationNotFoundEntityException::new);

        if (organization.getStatus().toString().equals("DELETED")) {
            throw new OrganizationAlreadyDeletedException();
        }

        organization.removeOrganization(request.getReason());

        Organization removeOrganization = organizationRepository.save(organization);

        return OrganizationDelete.Response.toDto(removeOrganization);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrganizationDelete.Response changeStatusOrganization(Long id) {

        Optional<Organization> result = organizationRepository.findById(id);

        Organization organization = result.orElseThrow(OrganizationNotFoundEntityException::new);

        if (organization.getStatus().toString().equals("DELETED")) {
            throw new OrganizationAlreadyDeletedException();
        }

        organization.changeStatus();

        Organization changedOrganization = organizationRepository.save(organization);

        return OrganizationDelete.Response.toDto(changedOrganization);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo.Response<ProjectListGet.Response> getListProjectByOrganization(Long id, String managementStep, PageInfo.Request pageRequest) {

        Pageable pageable = PageRequest.of(
                pageRequest.getCurrentPage() - 1,
                pageRequest.getPageSize());

        Project.ManagementStep validManagementStep = checkManagementStep(managementStep);

        Long memberId = currentMemberUtil.getCurrentMember().getRole().toString().equals("ADMIN") ?
                null : currentMemberUtil.getCurrentMember().getId();

        Page<ProjectList> result = projectQueryRepository.getProjectsByMemberAndOrganization(
                id,
                memberId,
                validManagementStep,
                pageRequest.getKeyword(),
                pageable);

        List<ProjectListGet.Response> dtoList = result.getContent().stream().map(ProjectListGet.Response::toDto).toList();
        PageMetaResponse meta = PageMetaResponse.fromPage(result);

        return PageInfo.Response.<ProjectListGet.Response>builder()
                .dtoList(dtoList)
                .meta(meta.toMap())
                .build();

    }


    private Organization.Type checkType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return null;
        }
        return Organization.Type.valueOf(type.toUpperCase());
    }

    private Organization.Status checkStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return null;
        }
        return Organization.Status.valueOf(status.toUpperCase());
    }

    private Project.ManagementStep checkManagementStep(String managementStep) {
        if (managementStep == null || managementStep.trim().isEmpty()) {
            return null;
        }
        return Project.ManagementStep.valueOf(managementStep.toUpperCase());
    }


}