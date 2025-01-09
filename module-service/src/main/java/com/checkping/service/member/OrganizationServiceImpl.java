package com.checkping.service.member;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.CustomException;
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
    public void createOrganization(OrganizationRequest.OrganizationCreateRequest request) {

        if (request == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        if (organizationRepository.findByNameAndType(request.getName(), request.getTypeEnum()).isPresent()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        Organization organization = OrganizationRequest.OrganizationCreateRequest.toEntity(request);

        organizationRepository.save(organization);
    }

    @Override
    public OrganizationResponse.OrganizationReadResponse getOrganization(UUID id) {

        Optional<Organization> result = organizationRepository.findById(id);

        Organization organization = result.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        return OrganizationResponse.OrganizationReadResponse.toDto(organization);
    }

    @Override
    public List<OrganizationResponse.OrganizationReadResponse> getByTypeOrganizations(String type) {
        return organizationRepository.findByType(Organization.Type.valueOf(type)).stream()
                .map(OrganizationResponse.OrganizationReadResponse::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<OrganizationResponse.OrganizationReadResponse> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(OrganizationResponse.OrganizationReadResponse::toDto)
                .collect(Collectors.toList());
    }

}