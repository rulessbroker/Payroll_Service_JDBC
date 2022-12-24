package com.bridgelabz;

import java.sql.SQLException;

public class EmployeePayRollService {
	public static void main(String[] args) throws SQLException {
		EmployeePayRollDBService employeePayRollDBService = new EmployeePayRollDBService();
		
		employeePayRollDBService.getConnection();
	}
	
}
