package com.checkping.service.member;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.CustomException;
import com.checkping.domain.member.Organization;
import com.checkping.dto.OrganizationRequest;
import com.checkping.infra.repository.member.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public boolean createOrganization(OrganizationRequest.OrganizationSignUpRequest request) {

        if (request == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        if (organizationRepository.findByNameAndType(request.getName(), request.getTypeEnum()).isPresent()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        Organization organization = OrganizationRequest.OrganizationSignUpRequest.toEntity(request);

        organizationRepository.save(organization);

        return true;
    }
}
