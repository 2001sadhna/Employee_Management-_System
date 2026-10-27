
package com.ems.dao;

import com.ems.model.Employee;
import com.ems.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // 1. Add employee
    public boolean addEmployee(Employee employee) throws SQLException {

        String sql = """
                INSERT INTO employees
                    (employee_name, email, salary, department_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, employee.getEmployeeName());
            ps.setString(2, employee.getEmail());
            ps.setBigDecimal(3, employee.getSalary());
            ps.setInt(4, employee.getDepartmentId());

            return ps.executeUpdate() > 0;
        }
    }

    // 2. View all employees
    public List<Employee> getAllEmployees() throws SQLException {

        String sql = """
                SELECT employee_id, employee_name, email,
                       salary, department_id
                FROM employees
                ORDER BY employee_id
                """;

        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                employees.add(mapEmployee(rs));
            }
        }

        return employees;
    }

    // 3. Search employee by ID
    public Employee getEmployeeById(int employeeId) throws SQLException {

        String sql = """
                SELECT employee_id, employee_name, email,
                       salary, department_id
                FROM employees
                WHERE employee_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapEmployee(rs);
                }
            }
        }

        return null;
    }

    // 4. Update employee details
    public boolean updateEmployee(Employee employee) throws SQLException {

        String sql = """
                UPDATE employees
                SET employee_name = ?,
                    email = ?,
                    salary = ?,
                    department_id = ?
                WHERE employee_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, employee.getEmployeeName());
            ps.setString(2, employee.getEmail());
            ps.setBigDecimal(3, employee.getSalary());
            ps.setInt(4, employee.getDepartmentId());
            ps.setInt(5, employee.getEmployeeId());

            return ps.executeUpdate() > 0;
        }
    }

    // 5. Delete employee
    public boolean deleteEmployee(int employeeId) throws SQLException {

        String sql = "DELETE FROM employees WHERE employee_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            return ps.executeUpdate() > 0;
        }
    }

    // 6. Display employees department-wise using INNER JOIN
    public void displayEmployeesByDepartment() throws SQLException {

        String sql = """
                SELECT d.department_name,
                       e.employee_id,
                       e.employee_name,
                       e.email,
                       e.salary
                FROM departments d
                INNER JOIN employees e
                    ON d.department_id = e.department_id
                ORDER BY d.department_name, e.employee_name
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-15s %-5s %-20s %-30s %12s%n",
                    "Department", "ID", "Name", "Email", "Salary");

            System.out.println(
                    "--------------------------------------------------------------------------");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.printf("%-15s %-5d %-20s %-30s %12.2f%n",
                        rs.getString("department_name"),
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("email"),
                        rs.getBigDecimal("salary"));
            }

            if (!found) {
                System.out.println("No employees found.");
            }
        }
    }

    // Convert one ResultSet row into an Employee object
    private Employee mapEmployee(ResultSet rs) throws SQLException {

        return new Employee(
                rs.getInt("employee_id"),
                rs.getString("employee_name"),
                rs.getString("email"),
                rs.getBigDecimal("salary"),
                rs.getInt("department_id")
        );
    }
}
