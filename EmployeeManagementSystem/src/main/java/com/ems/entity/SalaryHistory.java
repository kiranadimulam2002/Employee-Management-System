package com.ems.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "salary_history")
public class SalaryHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;
	
	@Column(name = "previous_salary", nullable = false)
	private Double previousSalary;
	
	@Column(name = "new_salary", nullable = false)
	private Double newSalary;
	
	@Column(name = "increment_amount", nullable = false)
	private Double incrementAmount;
	
	@Column(name = "effective_date", nullable = false)
	private LocalDate effectiveDate;
	
	
	@Column(name = "reason")
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


	public Double getPreviousSalary() {
		return previousSalary;
	}


	public void setPreviousSalary(Double previousSalary) {
		this.previousSalary = previousSalary;
	}


	public Double getNewSalary() {
		return newSalary;
	}


	public void setNewSalary(Double newSalary) {
		this.newSalary = newSalary;
	}


	public Double getIncrementAmount() {
		return incrementAmount;
	}


	public void setIncrementAmount(Double incrementAmount) {
		this.incrementAmount = incrementAmount;
	}


	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}


	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}
	
	

}

