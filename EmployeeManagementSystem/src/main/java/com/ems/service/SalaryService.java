package com.ems.service;

import java.util.List;

import com.ems.dto.SalaryIncrementRequest;
import com.ems.entity.SalaryHistory;

public interface SalaryService {
	
	List<SalaryHistory> getSalaryHistoryForEmployee(Long employeeId);
	SalaryHistory applySalaryIncrement(Long employeeId, SalaryIncrementRequest request);

}
