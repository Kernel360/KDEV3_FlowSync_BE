package com.checkping.service.member;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.member.Organization;
import com.checkping.dto.OrganizationRequest;
import com.checkping.dto.OrganizationResponse;
import com.checkping.infra.repository.member.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationResponse.ReadResponse createOrganization(OrganizationRequest.CreateRequest request) {

        if (request == null) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        if (organizationRepository.findByNameAndType(request.getName(), request.getTypeEnum()).isPresent()) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        Organization organization = OrganizationRequest.CreateRequest.toEntity(request);

        Organization createOrganization = organizationRepository.save(organization);

        return OrganizationResponse.ReadResponse.toDto(createOrganization);
    }

    @Override
    public OrganizationResponse.ReadResponse getOrganization(UUID id) {

        Optional<Organization> result = organizationRepository.findById(id);

        Organization organization = result.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        return OrganizationResponse.ReadResponse.toDto(organization);
    }

    @Override
    public List<OrganizationResponse.ReadResponse> getByTypeAndStatusOrganizations(String type, String status) {
        if (status == null) {
            return organizationRepository.findByType(Organization.Type.valueOf(type.toUpperCase())).stream()
                    .map(OrganizationResponse.ReadResponse::toDto)
                    .collect(Collectors.toList());
        }
        return organizationRepository.findByTypeAndStatus(
                        Organization.Type.valueOf(type.toUpperCase()),
                        Organization.Status.valueOf(status.toUpperCase())).stream()
                .map(OrganizationResponse.ReadResponse::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrganizationResponse.ReadResponse> getAllOrganizations(String status) {
        if (status == null) {
            return organizationRepository.findAll().stream()
                    .map(OrganizationResponse.ReadResponse::toDto)
                    .collect(Collectors.toList());
        }
        return organizationRepository.findByStatus(Organization.Status.valueOf(status.toUpperCase())).stream()
                .map(OrganizationResponse.ReadResponse::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationResponse.UpdateResponse modifyOrganization(
            UUID id,
            OrganizationRequest.UpdateRequest request
    ) {
        Optional<Organization> result = organizationRepository.findById(id);
        Organization organization = result.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        organization.updateOrganization(
                request.getBrNumber(),
                request.getBrCertificateUrl(),
                request.getStreetAddress(),
                request.getDetailAddress(),
                request.getPhoneNumber()
        );

        Organization updateOrganization = organizationRepository.save(organization);

        OrganizationResponse.UpdateResponse.toDto(updateOrganization);

        return OrganizationResponse.UpdateResponse.toDto(updateOrganization);
    }

    @Override
    public OrganizationResponse.ReadResponse removeOrganization(UUID id) {

        Optional<Organization> result = organizationRepository.findById(id);

        Organization organization = result.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        organization.changeStatus();

        Organization removeOrganization = organizationRepository.save(organization);

        return OrganizationResponse.ReadResponse.toDto(removeOrganization);
    }

}