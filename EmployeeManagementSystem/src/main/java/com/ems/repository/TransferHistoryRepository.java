package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.entity.TransferHistory;

public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
	
	List<TransferHistory> findByEmployeeId(Long employeeId);

}
