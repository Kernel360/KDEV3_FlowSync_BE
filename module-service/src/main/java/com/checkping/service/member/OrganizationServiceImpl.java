package com.checkping.service.member;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.common.utils.FileRequest;
import com.checkping.domain.member.Organization;
import com.checkping.dto.OrganizationCreate;
import com.checkping.dto.OrganizationGet;
import com.checkping.dto.OrganizationUpdate;
import com.checkping.infra.repository.file.S3FileRepositoryImpl;
import com.checkping.infra.repository.member.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final S3FileRepositoryImpl s3FileRepository;
    private final S3FileRepositoryImpl s3FileRepositoryImpl;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrganizationCreate.Response createOrganization(OrganizationCreate.Request request, MultipartFile file) {

        if (request == null) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        if (organizationRepository.findByNameAndType(request.getName(), request.getTypeEnum()).isPresent()) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        if (file != null ) {
            FileRequest fileRequest = s3FileRepository.uploadFile(file);
            request.setBrCertificateUrl(fileRequest.saveName()+"|"+fileRequest.url());
        }

        Organization organization = OrganizationCreate.Request.toEntity(request);

        Organization createOrganization = organizationRepository.save(organization);

        return OrganizationCreate.Response.toDto(createOrganization);
    }

    @Transactional(readOnly = true)
    @Override
    public OrganizationGet.Response getOrganization(UUID id) {

        Optional<Organization> result = organizationRepository.findById(id);

        Organization organization = result.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        return OrganizationGet.Response.toDto(organization);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrganizationGet.Response> getAllByTypeAndStatusOrganizations(String type, String status) {

        // 전체 조회
        if (type == null && status == null) {
            return organizationRepository.findAll().stream()
                    .map(OrganizationGet.Response::toDto)
                    .collect(Collectors.toList());
        }
        // 전체 조회 (상태별)
        else if (type == null) {
            return organizationRepository.findByStatus(Organization.Status.valueOf(status.toUpperCase())).stream()
                    .map(OrganizationGet.Response::toDto)
                    .collect(Collectors.toList());
        }
        // 타입별 전체 조회
        else if (status == null) {
            return organizationRepository.findByType(Organization.Type.valueOf(type.toUpperCase())).stream()
                    .map(OrganizationGet.Response::toDto)
                    .collect(Collectors.toList());
        }
        // 타입별 전체 조회 (상태별)
        else {
            return organizationRepository.findByTypeAndStatus(
                    Organization.Type.valueOf(type.toUpperCase()),
                    Organization.Status.valueOf(status.toUpperCase())).stream()
                    .map(OrganizationGet.Response::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrganizationUpdate.Response modifyOrganization(
            UUID id,
            OrganizationUpdate.Request request,
            MultipartFile file
    ) {
        Optional<Organization> result = organizationRepository.findById(id);
        Organization organization = result.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        // 저장 파일명
        String saveName = organization.getBrCertificateUrl().split("\\|")[0];

        // 수정 파일 등록
        if (file != null) {
            FileRequest fileRequest = s3FileRepository.uploadFile(file);
            request.setBrCertificateUrl(fileRequest.saveName()+"|"+fileRequest.url());
        }

        organization.updateOrganization(
                request.getBrNumber(),
                request.getBrCertificateUrl(),
                request.getStreetAddress(),
                request.getDetailAddress(),
                request.getPhoneNumber()
        );

        Organization updateOrganization = organizationRepository.save(organization);

        // 기존 파일 삭제
        s3FileRepositoryImpl.deleteFile(saveName);

        OrganizationUpdate.Response.toDto(updateOrganization);

        return OrganizationUpdate.Response.toDto(updateOrganization);
    }

    @Override
    public OrganizationGet.Response removeOrganization(UUID id) {

        Optional<Organization> result = organizationRepository.findById(id);

        Organization organization = result.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        organization.changeStatus();

        Organization removeOrganization = organizationRepository.save(organization);

        return OrganizationGet.Response.toDto(removeOrganization);
    }

}