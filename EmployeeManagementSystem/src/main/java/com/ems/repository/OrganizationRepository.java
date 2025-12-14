package com.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
