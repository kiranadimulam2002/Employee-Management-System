package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.entity.SalaryHistory;

public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory, Long>{
	
	List<SalaryHistory> findByEmployeeIdOrderByEffectiveDateDesc(Long employeeId);

}
