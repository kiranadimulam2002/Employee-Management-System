package com.ems.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.dto.SalaryIncrementRequest;
import com.ems.entity.SalaryHistory;
import com.ems.service.SalaryService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/employees")
public class SalaryController {
	
	private final SalaryService salaryService;
	
	public SalaryController(SalaryService salaryService) {
		this.salaryService = salaryService;
	}
	
    @GetMapping("/{id}/salary-history")
    public ResponseEntity<List<SalaryHistory>> getSalaryHistory(@PathVariable("id") Long id) {
        List<SalaryHistory> history = salaryService.getSalaryHistoryForEmployee(id);
        return ResponseEntity.ok(history);
    }
	
	@PostMapping("/{id}/salary/increment")
	public ResponseEntity<SalaryHistory> incrementSalary(@PathVariable("id") Long id, @RequestBody SalaryIncrementRequest request) {
		SalaryHistory saved = salaryService.applySalaryIncrement(id, request);
		return ResponseEntity.ok(saved);
	}
	

}
