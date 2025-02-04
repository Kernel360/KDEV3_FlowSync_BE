package com.checkping.infra.repository.project;

import com.checkping.domain.project.Project;

public interface ProjectReader {

    Project getById(Long projectId);

    boolean isCustomerOwner(Long projectId, Long customerId);
}
