package com.bridgelabz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayRollDBService {

	private static EmployeePayRollDBService employeePayRollDBService;
	private PreparedStatement employeepayRollDataStatement;

	Connection getConnection() throws EmployeePayrollException, SQLException{
		Connection connection;
		String jdbcUrl = "jdbc:mysql://localhost:3306/payroll_service?characterEncoding=utf8";
		String userName = "root";
		String Password = "password";

		connection = DriverManager.getConnection(jdbcUrl, userName, Password);
		System.out.println("connection is successfull" + connection);
		return connection;

	}

	public static EmployeePayRollDBService getInstance() {
		if (employeePayRollDBService == null)
			employeePayRollDBService = new EmployeePayRollDBService();
		return employeePayRollDBService;
	}

	public List<EmployeePayRollData> readData() throws EmployeePayrollException {
		String sql = "select * from employee_payroll";
		return this.getEmployeePayRollDataUsingDB(sql);

	}

	private List<EmployeePayRollData> getEmployeePayRollDataUsingDB(String sql) throws EmployeePayrollException {
		List<EmployeePayRollData> employeePayROllData = new ArrayList<>();
		try {
			Connection con = this.getConnection();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			employeePayROllData = this.getEmployeePayRollData(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employeePayROllData;
	}

	private List<EmployeePayRollData> getEmployeePayRollData(ResultSet result) {
		List<EmployeePayRollData> employeePayROllData = new ArrayList<>();
		try {
			while (result.next()) {

				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				employeePayROllData.add(new EmployeePayRollData(id, name, salary));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayROllData;
	}
	
	public List<EmployeePayRollData> getEmployeePayRollData(String name) throws EmployeePayrollException {
		List<EmployeePayRollData> employeePayROllData =null;
		if(this.employeepayRollDataStatement == null)	
	      this.prepareStatementForEmployeeData();
		try {
			employeepayRollDataStatement.setString(1, name);
			ResultSet resultSet = employeepayRollDataStatement.executeQuery();
			employeePayROllData = this.getEmployeePayRollData(resultSet);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayROllData;
	}
	
	private void prepareStatementForEmployeeData() throws EmployeePayrollException {
		try {
			Connection con = this.getConnection();
			String sql = "select * from employee_payroll where name = ?";
			employeepayRollDataStatement =  con.prepareStatement(sql);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
