
package com.ems.app;

import com.ems.model.Employee;
import com.ems.service.EmployeeService;
import com.ems.util.DatabaseInitializer;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class EmployeeManagementApp {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final EmployeeService SERVICE = new EmployeeService();

    public static void main(String[] args) {

        try {
            DatabaseInitializer.initialize();
            runMenu();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            System.out.println("Check MySQL, database name, username and password.");
        } finally {
            SCANNER.close();
        }
    }

    private static void runMenu() {

        boolean running = true;

        while (running) {
            System.out.println("\n===== EMPLOYEE MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee By ID");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. View Employees Department-wise");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            String choice = SCANNER.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> addEmployee();
                    case "2" -> viewAllEmployees();
                    case "3" -> searchEmployee();
                    case "4" -> updateEmployee();
                    case "5" -> deleteEmployee();
                    case "6" -> SERVICE.displayEmployeesByDepartment();
                    case "7" -> {
                        running = false;
                        System.out.println("Application closed.");
                    }
                    default -> System.out.println(
                            "Invalid choice. Please enter a valid operation.");
                }
            } catch (SQLException e) {
                System.out.println("Database operation failed: "
                        + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    private static void addEmployee() throws SQLException {

        Employee employee = readEmployeeDetails();

        if (SERVICE.addEmployee(employee)) {
            System.out.println("Employee added successfully.");
        } else {
            System.out.println("Employee was not added.");
        }
    }

    private static void viewAllEmployees() throws SQLException {

        List<Employee> employees = SERVICE.getAllEmployees();

        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println("ID | Name | Email | Salary | Dept ID");

        for (Employee employee : employees) {
            printEmployee(employee);
        }
    }

    private static void searchEmployee() throws SQLException {

        int employeeId = readInt("Enter employee ID: ");

        Employee employee = SERVICE.getEmployeeById(employeeId);

        if (employee == null) {
            System.out.println("Employee not found.");
        } else {
            printEmployee(employee);
        }
    }

    private static void updateEmployee() throws SQLException {

        int employeeId = readInt("Enter employee ID to update: ");

        Employee existing = SERVICE.getEmployeeById(employeeId);

        if (existing == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("Enter the new employee details:");
        Employee updated = readEmployeeDetails();
        updated.setEmployeeId(employeeId);

        if (SERVICE.updateEmployee(updated)) {
            System.out.println("Employee updated successfully.");
        } else {
            System.out.println("Employee was not updated.");
        }
    }

    private static void deleteEmployee() throws SQLException {

        int employeeId = readInt("Enter employee ID to delete: ");

        Employee existing = SERVICE.getEmployeeById(employeeId);

        if (existing == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.print("Confirm deletion? (yes/no): ");
        String confirmation = SCANNER.nextLine().trim();

        if (confirmation.equalsIgnoreCase("yes")) {
            if (SERVICE.deleteEmployee(employeeId)) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Employee was not deleted.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static Employee readEmployeeDetails() {

        System.out.print("Enter employee name: ");
        String name = SCANNER.nextLine().trim();

        System.out.print("Enter email: ");
        String email = SCANNER.nextLine().trim();

        BigDecimal salary = readSalary("Enter salary: ");

        System.out.println("Departments: 1. IT  2. HR  3. Finance");
        int departmentId = readInt("Enter department ID: ");

        if (departmentId < 1 || departmentId > 3) {
            throw new IllegalArgumentException(
                    "Choose department ID 1, 2 or 3.");
        }

        return new Employee(name, email, salary, departmentId);
    }

    private static int readInt(String message) {

        System.out.print(message);
        String input = SCANNER.nextLine().trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Please enter a valid whole number.");
        }
    }

    private static BigDecimal readSalary(String message) {

        System.out.print(message);
        String input = SCANNER.nextLine().trim();

        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Please enter a valid salary.");
        }
    }

    private static void printEmployee(Employee employee) {

        System.out.println("ID: "+ employee.getEmployeeId()+
                ", Name: " + employee.getEmployeeName()+
                ", Email: " + employee.getEmail()+
                ", Salary: " + employee.getSalary()+
                ", Dept ID:" + employee.getDepartmentId());
    }
}
