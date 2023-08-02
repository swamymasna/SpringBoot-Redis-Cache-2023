package com.swamy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.swamy.entity.Employee;
import com.swamy.exception.ResourceNotFoundException;
import com.swamy.props.AppProperties;
import com.swamy.repository.EmployeeRepository;
import com.swamy.service.EmployeeService;
import com.swamy.utils.AppConstants;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private AppProperties appProperties;

	@Override
	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	@Cacheable(value = "employee", key = "#employeeId")
	public Employee getEmployeeById(Integer employeeId) {

		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				appProperties.getMessages().get(AppConstants.EMPOYEE_NOT_FOUND_BY_ID)));

		return employee;
	}

	@Override
	@CachePut(value = "employee", key = "#employeeId")
	public Employee updateEmployee(Integer employeeId, Employee employee) {

		Employee empObj = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				appProperties.getMessages().get(AppConstants.EMPOYEE_NOT_FOUND_BY_ID)));

		empObj.setEmployeeName(employee.getEmployeeName());
		empObj.setEmployeeSalary(employee.getEmployeeSalary());
		empObj.setEmployeeAddress(employee.getEmployeeAddress());

		Employee updatedEmployee = employeeRepository.save(empObj);

		return updatedEmployee;
	}

	@Override
//	@CacheEvict(value = "employee",key = "#employeeId")
	@CacheEvict(value = "employee", allEntries = true)
	public String deleteEmployee(Integer employeeId) {

		Employee empObj = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				appProperties.getMessages().get(AppConstants.EMPOYEE_NOT_FOUND_BY_ID)));

		employeeRepository.delete(empObj);

		return appProperties.getMessages().get(AppConstants.EMPLOYEE_DELETION_SUCCEEDED);
	}
}
