package com.bridgelabz;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayRollDBService {

	private static EmployeePayRollDBService employeePayRollDBService;
	private PreparedStatement employeepayRollDataStatement;

	Connection getConnection() throws EmployeePayrollException, SQLException {
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
		String sql = "select * from payroll_service";
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
		List<EmployeePayRollData> employeePayROllData = null;
		if (this.employeepayRollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeepayRollDataStatement.setString(1, name);
			ResultSet resultSet = employeepayRollDataStatement.executeQuery();
			employeePayROllData = this.getEmployeePayRollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayROllData;
	}

	private void prepareStatementForEmployeeData() throws EmployeePayrollException {
		try {
			Connection con = this.getConnection();
			String sql = "select * from payroll_service where name = ?";
			employeepayRollDataStatement = con.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int updateEmployeeData(String name, double salary) throws EmployeePayrollException {
		return this.updateEmployeeDataUsingStatement(name, salary);
	}

	private int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollException {
		int result = 0;
		String sql = String.format("update payroll_service set salary = %.2f where name = '%s'", salary, name);
		try {
			Connection con = this.getConnection();
			java.sql.Statement stmt = con.createStatement();
			result = stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<EmployeePayRollData> getEmployeePayRollForDateRange(LocalDate startDate, LocalDate endDate)
			throws EmployeePayrollException {
		String sql = String.format(
				"select * from payroll_service where start between cast('%s' as date) AND date('%s');",
				Date.valueOf(startDate), Date.valueOf(endDate));

		return this.getEmployeePayRollDataUsingDB(sql);
	}

	public Map<String, Double> getAvgSalaryByGender() throws EmployeePayrollException {
		String sql = "select gender,avg(salary) as avgsalary from payroll_service group by gender";
		Map<String, Double> genderToAvgSalaryMap = new HashMap<>();
		try {
			Connection con = this.getConnection();
			java.sql.Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			while (result.next()) {
				String gender = result.getString("gender");
				double salary = result.getDouble("avgsalary");
				genderToAvgSalaryMap.put(gender, salary);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return genderToAvgSalaryMap;
	}

	public EmployeePayRollData addEmployeePayRoll(String name, double salary, LocalDate startDate, String gender)
			throws EmployeePayrollException {
		Connection connection = null;
		try {
			connection = this.getConnection();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		int employeeId = -1;
		EmployeePayRollData employeePayRollData = null;
		String sql = String.format(
				"Insert into employee_payroll (name,gender, salary,startDate) values" + "('%s','%s',%s,'%s')", name,
				gender, salary, Date.valueOf(startDate));
		try {

			java.sql.Statement stmt = connection.createStatement();
			connection.setAutoCommit(false);
			int rowAffected = stmt.executeUpdate(sql, stmt.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = stmt.getGeneratedKeys();
				if (resultSet.next()) {
					employeeId = resultSet.getInt(1);
				}
			}
			employeePayRollData = new EmployeePayRollData(employeeId, name, salary, startDate);
		} catch (SQLException e) {
			try {
				connection.rollback();
				return employeePayRollData;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return employeePayRollData;
	}
}
