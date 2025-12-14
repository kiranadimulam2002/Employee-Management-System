package com.ems.service;

import java.util.List;

import com.ems.entity.Employee;

public interface EmployeeService {
	
	Employee createEmployee(Employee employee);
	List<Employee> getAllEmployees();
	Employee getEmployeeById(Long id);
	Employee updateEmployee(Long id, Employee updatedEmployee);
	void deleteEmployee(Long id);

}
