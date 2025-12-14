package com.ems.service;

import com.ems.repository.EmployeeRepository;
import com.ems.repository.SalaryHistoryRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ems.dto.SalaryIncrementRequest;
import com.ems.entity.Employee;
import com.ems.entity.SalaryHistory;

@Service
public class SalaryServiceImplementation implements SalaryService{
	
	private final EmployeeRepository employeeRepository;
	
	private final SalaryHistoryRepository salaryHistoryRepository;
	
	public SalaryServiceImplementation(EmployeeRepository employeeRepository, SalaryHistoryRepository salaryHistoryRepository) {
		this.employeeRepository = employeeRepository;
		this.salaryHistoryRepository = salaryHistoryRepository;
	}
	
	
	@Override
	public List<SalaryHistory> getSalaryHistoryForEmployee(Long employeeId) {
		
		return salaryHistoryRepository.findByEmployeeIdOrderByEffectiveDateDesc(employeeId);
	}
	
	@Override
	@Transactional
	public SalaryHistory applySalaryIncrement(Long employeeId, SalaryIncrementRequest request) {
		
		Employee emp = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee Not Found"));
		
		Double previousSalary = emp.getSalary() == null ? 0.0 : emp.getSalary();
		Double newSalary = request.getNewSalary();
		
		if (newSalary == null) {
			throw new IllegalArgumentException("newSalary is required");
		}
		
		Double increment = newSalary - previousSalary;
		
		emp.setSalary(newSalary);
		employeeRepository.save(emp);
		
		SalaryHistory history = new SalaryHistory();
		history.setEmployee(emp);
		history.setPreviousSalary(previousSalary);
		history.setNewSalary(newSalary);
		history.setIncrementAmount(increment);
		history.setEffectiveDate(request.getEffectiveDate() == null ? LocalDate.now() : request.getEffectiveDate());
		history.setReason(request.getReason());
		
		return salaryHistoryRepository.save(history);
	}

}
