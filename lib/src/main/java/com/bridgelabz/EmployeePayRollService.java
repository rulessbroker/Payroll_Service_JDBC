package com.bridgelabz;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayRollService {
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	public EmployeePayRollDBService employeePayRollDBService;
	public List<EmployeePayRollData> employeePayRollList = new ArrayList<EmployeePayRollData>();

	public EmployeePayRollService(List<EmployeePayRollData> list) {
		this();
		employeePayRollList = list;
	}

	public EmployeePayRollService() {
		employeePayRollDBService = EmployeePayRollDBService.getInstance();
	};

	public static void main(String[] args) {
		ArrayList<EmployeePayRollData> employeePayRollList = new ArrayList<>();
		EmployeePayRollService empPayRollService = new EmployeePayRollService(employeePayRollList);
		Scanner consoleReader = new Scanner(System.in);
		empPayRollService.readEmployeePayRollData(consoleReader);
		empPayRollService.writeEmployeePayRollData();
		empPayRollService.writeEmployeePayRollData(IOService.FILE_IO);
	}

	public void readEmployeePayRollData(Scanner consoleReader) {
		System.out.println("Enter emp id :");
		int id = consoleReader.nextInt();
		System.out.println("Enter emp name :");
		String name = consoleReader.next();
		System.out.println("Enter emp salary :");
		double salary = consoleReader.nextDouble();
		employeePayRollList.add(new EmployeePayRollData(id, name, salary));
	}

	public void writeEmployeePayRollData() {
		System.out.println("writing emp pay roll to console :" + employeePayRollList);
	}

	public void writeEmployeePayRollData(IOService ioService) {

		if (ioService.equals(ioService.CONSOLE_IO)) {
			System.out.println("writing emp pay roll to console :" + employeePayRollList);
		} else if (ioService.equals(ioService.FILE_IO)) {
			new EmployeePayRollFileIOService().writeData(employeePayRollList);

		}
	}

	public void printData(IOService ioService) {

		if (ioService.equals(ioService.FILE_IO)) {
			new EmployeePayRollFileIOService().printData();
		}

	}

	public long countEntries(IOService ioService) {
		if (ioService.equals(ioService.FILE_IO)) {
			return new EmployeePayRollFileIOService().countEntries();
		}
		return 0;
	}

	public List<EmployeePayRollData> readEmployeePayroll(IOService ioService) throws EmployeePayrollException {
		if (ioService.equals(ioService.DB_IO)) {
			this.employeePayRollList = employeePayRollDBService.readData();
		}

		return this.employeePayRollList;
	}

	public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
		int result = employeePayRollDBService.updateEmployeeData(name, salary);
		if (result == 0)
			return;
		EmployeePayRollData employeePayROllData = this.getEmployeePayROllData(name);
		if (employeePayROllData != null)
			employeePayROllData.salary = salary;
	}

	private EmployeePayRollData getEmployeePayROllData(String name) {
		return this.employeePayRollList.stream().filter(empPayROllDataItem -> empPayROllDataItem.name.equals(name))
				.findFirst().orElse(null);

	}

	public boolean checkEmployeePayRollSyncWithDataBase(String name) throws EmployeePayrollException {
		return employeePayRollList.get(0).equals(getEmployeePayROllData(name));
	}

	public List<EmployeePayRollData> readEmployeePayRollForDateRange(IOService ioService, LocalDate startDate,
			LocalDate endDate) throws EmployeePayrollException {
		if (ioService.equals(ioService.DB_IO)) {
			return employeePayRollDBService.getEmployeePayRollForDateRange(startDate, endDate);
		}

		return null;

	}

	public java.util.Map<String, Double> readAvgSalaryByGender(IOService ioService) throws EmployeePayrollException {
		if (ioService.equals(ioService.DB_IO)) {
			return employeePayRollDBService.getAvgSalaryByGender();
		}
		return null;
	}
}
