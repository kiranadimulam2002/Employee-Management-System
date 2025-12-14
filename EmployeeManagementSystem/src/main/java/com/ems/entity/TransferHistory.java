package com.ems.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TransferHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;
	
	@ManyToOne
	@JoinColumn(name = "old_org_id")
	private Organization oldOrganization;
	
	@ManyToOne
	@JoinColumn(name = "new_org_id")
	private Organization newOrganization;
	
	private LocalDate transferDate;
	
	private String reason;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Organization getOldOrganization() {
		return oldOrganization;
	}

	public void setOldOrganization(Organization oldOrganization) {
		this.oldOrganization = oldOrganization;
	}

	public Organization getNewOrganization() {
		return newOrganization;
	}

	public void setNewOrganization(Organization newOrganization) {
		this.newOrganization = newOrganization;
	}

	public LocalDate getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(LocalDate transferDate) {
		this.transferDate = transferDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	

}
