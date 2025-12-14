package com.ems.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

import com.ems.entity.Employee;
import com.ems.entity.Organization;
import com.ems.entity.TransferHistory;
import com.ems.repository.EmployeeRepository;
import com.ems.repository.OrganizationRepository;
import com.ems.repository.TransferHistoryRepository;

@Service
public class EmployeeServiceImplementation implements EmployeeService {

    private final TransferHistoryRepository transferHistoryRepository;

    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;

    public EmployeeServiceImplementation(EmployeeRepository employeeRepository,
                                         OrganizationRepository organizationRepository, TransferHistoryRepository transferHistoryRepository) {
        this.employeeRepository = employeeRepository;
        this.organizationRepository = organizationRepository;
        this.transferHistoryRepository = transferHistoryRepository;
    }

    // ---------------------- CREATE EMPLOYEE ------------------------
    @Override
    public Employee createEmployee(Employee employee) {

        // 1. Fetch organization
        Organization org = organizationRepository.findById(employee.getOrganization().getId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        // 2. Increment employee counter
        org.setEmployeeCounter(org.getEmployeeCounter() + 1);
        organizationRepository.save(org);

        // 3. Generate employee ID
        String generatedId = String.format("%s-EMP-%05d",
                org.getPrefix(),
                org.getEmployeeCounter());

        employee.setEmployeeId(generatedId);
        employee.setOrganization(org);

        return employeeRepository.save(employee);
    }

    // ---------------------- GET ALL EMPLOYEES ------------------------
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // ---------------------- GET EMPLOYEE BY ID ------------------------
    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));
    }

    @Override
    public Employee updateEmployee(Long id, Employee updatedEmployee) {

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        Long oldOrgId = existing.getOrganization().getId();
        Long newOrgId = updatedEmployee.getOrganization().getId();

        boolean orgChanged = !oldOrgId.equals(newOrgId);

        // ------------------ HANDLE ORGANIZATION CHANGE ------------------
        if (orgChanged) {

            Organization oldOrg = existing.getOrganization();

            Organization newOrg = organizationRepository.findById(newOrgId)
                    .orElseThrow(() -> new RuntimeException("New Organization not found"));

            // Increment new org's counter
            newOrg.setEmployeeCounter(newOrg.getEmployeeCounter() + 1);
            organizationRepository.save(newOrg);

            // Generate NEW employee ID
            String newEmpId = String.format("%s-EMP-%05d",
                    newOrg.getPrefix(),
                    newOrg.getEmployeeCounter());

            // Save TRANSFER HISTORY
            TransferHistory history = new TransferHistory();
            history.setEmployee(existing);
            history.setOldOrganization(oldOrg);
            history.setNewOrganization(newOrg);
            history.setTransferDate(LocalDate.now());
            history.setReason("Organization Transfer");

            transferHistoryRepository.save(history);

            // Apply org change on employee
            existing.setEmployeeId(newEmpId);
            existing.setOrganization(newOrg);
        }

        // ------------------ UPDATE REGULAR FIELDS ------------------
        existing.setFullname(updatedEmployee.getFullname());
        existing.setEmail(updatedEmployee.getEmail());
        existing.setPhone(updatedEmployee.getPhone());
        existing.setDepartment(updatedEmployee.getDepartment());
        existing.setRole(updatedEmployee.getRole());
        existing.setJoiningDate(updatedEmployee.getJoiningDate());
        existing.setSalary(updatedEmployee.getSalary());

        return employeeRepository.save(existing);
    }
    
    // ------ Delete Employee By ID ---- //

	@Override
	public void deleteEmployee(Long id) {
		
		Employee emp = employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee Not Found"));
		employeeRepository.delete(emp);
	}

    
}
