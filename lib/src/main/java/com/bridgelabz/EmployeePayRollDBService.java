package com.bridgelabz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EmployeePayRollDBService {
	Connection getConnection() throws SQLException {
		Connection connection;
		String jdbcUrl = "jdbc:mysql://localhost:3306/payroll_services?characterEncoding=utf8";
		String userName = "root";
		String Password = "password";
		connection = DriverManager.getConnection(jdbcUrl, userName, Password);
		System.out.println("connection is successfull" + connection);
		return connection;
	}
}
