package com.checkping.service.member;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.member.Organization;
import com.checkping.dto.*;
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
    public OrganizationCreate.Response createOrganization(OrganizationCreate.Request request) {

        if (request == null) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        if (organizationRepository.findByNameAndType(request.getName(), request.getTypeEnum()).isPresent()) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        Organization organization = OrganizationCreate.Request.toEntity(request);

        Organization createOrganization = organizationRepository.save(organization);

        return OrganizationCreate.Response.toDto(createOrganization);
    }

    @Override
    public OrganizationGet.Response getOrganization(UUID id) {

        Optional<Organization> result = organizationRepository.findById(id);

        Organization organization = result.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        return OrganizationGet.Response.toDto(organization);
    }

    @Override
    public List<OrganizationGet.Response> getByTypeAndStatusOrganizations(String type, String status) {
        if (status == null) {
            return organizationRepository.findByType(Organization.Type.valueOf(type.toUpperCase())).stream()
                    .map(OrganizationGet.Response::toDto)
                    .collect(Collectors.toList());
        }
        return organizationRepository.findByTypeAndStatus(
                        Organization.Type.valueOf(type.toUpperCase()),
                        Organization.Status.valueOf(status.toUpperCase())).stream()
                .map(OrganizationGet.Response::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrganizationGet.Response> getAllOrganizations(String status) {
        if (status == null) {
            return organizationRepository.findAll().stream()
                    .map(OrganizationGet.Response::toDto)
                    .collect(Collectors.toList());
        }
        return organizationRepository.findByStatus(Organization.Status.valueOf(status.toUpperCase())).stream()
                .map(OrganizationGet.Response::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationUpdate.Response modifyOrganization(
            UUID id,
            OrganizationUpdate.Request request
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