package com.ems.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() throws SQLException {

        String createDepartmentTable = """
                CREATE TABLE IF NOT EXISTS departments (
                    department_id INT PRIMARY KEY,
                    department_name VARCHAR(100) NOT NULL UNIQUE
                )
                """;

        String createEmployeeTable = """
                CREATE TABLE IF NOT EXISTS employees (
                    employee_id INT PRIMARY KEY AUTO_INCREMENT,
                    employee_name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    salary DECIMAL(10,2) NOT NULL,
                    department_id INT NOT NULL,
                    CONSTRAINT fk_employee_department
                        FOREIGN KEY (department_id)
                        REFERENCES departments(department_id)
                )
                """;

        String insertDepartments = """
                INSERT IGNORE INTO departments
                    (department_id, department_name)
                VALUES
                    (1, 'IT'),
                    (2, 'HR'),
                    (3, 'Finance')
                """;

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(createDepartmentTable);
            statement.executeUpdate(createEmployeeTable);
            statement.executeUpdate(insertDepartments);

            System.out.println("Database initialized successfully.");
        }
    }
}
