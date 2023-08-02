package com.swamy.service;

import java.util.List;

import com.swamy.entity.Employee;

public interface EmployeeService {

	Employee saveEmployee(Employee employee);

	List<Employee> getAllEmployees();

	Employee getEmployeeById(Integer employeeId);
	
	Employee updateEmployee(Integer employeeId, Employee employee);

	String deleteEmployee(Integer employeeId);

}
