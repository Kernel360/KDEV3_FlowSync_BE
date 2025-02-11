package com.checkping.service.member;

import com.checkping.common.dto.PageInfo;
import com.checkping.common.dto.PageMetaResponse;
import com.checkping.common.utils.FileRequest;
import com.checkping.domain.member.Organization;
import com.checkping.domain.member.projection.ProjectListGet;
import com.checkping.domain.project.Project;
import com.checkping.dto.*;
import com.checkping.exception.member.OrganizationAlreadyDeletedException;
import com.checkping.exception.member.OrganizationAlreadyExistEntityException;
import com.checkping.exception.member.OrganizationNotFoundEntityException;
import com.checkping.infra.repository.file.S3FileRepositoryImpl;
import com.checkping.infra.repository.member.MemberOrganizationQueryRepository;
import com.checkping.infra.repository.member.OrganizationRepository;
import com.checkping.service.member.util.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final MemberOrganizationQueryRepository memberOrganizationQueryRepository;

    private final S3FileRepositoryImpl s3FileRepository;

    private final CurrentMemberUtil currentMemberUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrganizationCreate.Response createOrganization(OrganizationCreate.Request request, MultipartFile file) {

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
    public PageInfo.Response<OrganizationListGet.Response> getListOrganization(String type, String status, PageInfo.Request pageRequest) {

        Pageable pageable = PageRequest.of(
                pageRequest.getCurrentPage() - 1,
                pageRequest.getPageSize(),
                Sort.by("id").descending());

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

        organization.removeOrganization(request.getReason());

        Organization removeOrganization = organizationRepository.save(organization);

        return OrganizationDelete.Response.toDto(removeOrganization);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrganizationDelete.Response changeStatusOrganization(Long id, OrganizationDelete.Request request) {

        Optional<Organization> result = organizationRepository.findById(id);

        Organization organization = result.orElseThrow(OrganizationNotFoundEntityException::new);

        if (organization.getStatus().toString().equals("DELETED")) {
            throw new OrganizationAlreadyDeletedException();
        }

        organization.changeStatus(request.getReason());

        Organization changedOrganization = organizationRepository.save(organization);

        return OrganizationDelete.Response.toDto(changedOrganization);
    }

    @Override
    public PageInfo.Response<MemberOrganizationProjectListGet.Response> getListProjectByOrganization(Long id, String managementStep, PageInfo.Request pageRequest) {

        Pageable pageable = PageRequest.of(
                pageRequest.getCurrentPage() - 1,
                pageRequest.getPageSize(),
                Sort.by("id").descending());

        Project.ManagementStep validManagementStep = checkManagementStep(managementStep);

        Page<ProjectListGet> result = memberOrganizationQueryRepository.getProjectsByMemberAndOrganization(
                id,
                null,
                validManagementStep,
                pageRequest.getKeyword(),
                pageable);

        List<MemberOrganizationProjectListGet.Response> dtoList = result.getContent().stream().map(MemberOrganizationProjectListGet.Response::toDto).toList();
        PageMetaResponse meta = PageMetaResponse.fromPage(result);

        return PageInfo.Response.<MemberOrganizationProjectListGet.Response>builder()
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