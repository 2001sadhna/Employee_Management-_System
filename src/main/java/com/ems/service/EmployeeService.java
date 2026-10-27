package com.ems.service;

import com.ems.dao.EmployeeDAO;
import com.ems.model.Employee;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class EmployeeService {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public boolean addEmployee(Employee employee) throws SQLException {
        validateEmployee(employee);
        return employeeDAO.addEmployee(employee);
    }

    public List<Employee> getAllEmployees() throws SQLException {
        return employeeDAO.getAllEmployees();
    }

    public Employee getEmployeeById(int employeeId) throws SQLException {
        if (employeeId <= 0) {
            throw new IllegalArgumentException(
                    "Employee ID must be positive.");
        }

        return employeeDAO.getEmployeeById(employeeId);
    }

    public boolean updateEmployee(Employee employee) throws SQLException {
        if (employee.getEmployeeId() <= 0) {
            throw new IllegalArgumentException(
                    "Employee ID must be positive.");
        }

        validateEmployee(employee);
        return employeeDAO.updateEmployee(employee);
    }

    public boolean deleteEmployee(int employeeId) throws SQLException {
        if (employeeId <= 0) {
            throw new IllegalArgumentException(
                    "Employee ID must be positive.");
        }

        return employeeDAO.deleteEmployee(employeeId);
    }

    public void displayEmployeesByDepartment() throws SQLException {
        employeeDAO.displayEmployeesByDepartment();
    }

    private void validateEmployee(Employee employee) {

        if (employee.getEmployeeName() == null || employee.getEmployeeName().isBlank()) {
            throw new IllegalArgumentException(
                    "Employee name cannot be empty.");
        }

        if (employee.getEmail() == null
                || !employee.getEmail().matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException(
                    "Please enter a valid email.");
        }

        BigDecimal salary = employee.getSalary();

        if (salary == null
                || salary.compareTo(BigDecimal.ZERO) <= 0
                || salary.precision() > 10
                || salary.scale() > 2) {
            throw new IllegalArgumentException(
                    "Salary must be positive with at most 2 decimal places.");
        }

        if (employee.getDepartmentId() <= 0) {
            throw new IllegalArgumentException(
                    "Department ID must be positive.");
        }
    }
}
