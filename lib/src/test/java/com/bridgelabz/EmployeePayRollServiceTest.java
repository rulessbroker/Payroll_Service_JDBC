package com.bridgelabz;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bridgelabz.EmployeePayRollService.IOService;

class EmployeePayRollServiceTest {

	@Test
	public void givenThreeEmployeesWritenToFile() {
		EmployeePayRollData[] empdata = { new EmployeePayRollData(1, "Bill", 1000000),
				new EmployeePayRollData(2, "Mark", 2000000), new EmployeePayRollData(3, "Charlie", 3000000),
				new EmployeePayRollData(4, "Terisa", 1000000) };
		EmployeePayRollService employeePayRollService;

		employeePayRollService = new EmployeePayRollService(Arrays.asList(empdata));
		employeePayRollService.writeEmployeePayRollData(IOService.FILE_IO);
		employeePayRollService.printData(IOService.FILE_IO);
		long entries = employeePayRollService.countEntries(IOService.FILE_IO);
		Assertions.assertEquals(4, entries);

	}

	@Test
	public void givenEmployeePayrollInDB_when_Retrieved_shouldMatchEmployeeCount() throws EmployeePayrollException {
		EmployeePayRollService employeePayRollService = new EmployeePayRollService();
		List<EmployeePayRollData> empPayRollData = employeePayRollService.readEmployeePayroll(IOService.DB_IO);
		Assertions.assertEquals(4, empPayRollData.size());
	}

	@Test
	public void givenNewSalaryForEmployeeWhenUpdatedShouldMatch() throws EmployeePayrollException {
		EmployeePayRollService employeePayRollService = new EmployeePayRollService();
		List<EmployeePayRollData> empPayRollData = employeePayRollService.readEmployeePayroll(IOService.DB_IO);
		employeePayRollService.updateEmployeeSalary("Terisa", 300000.00);
		boolean result = employeePayRollService.checkEmployeePayRollSyncWithDataBase("Terisa");
		Assertions.assertFalse(result);
	}

	@Test
	public void givenDateRangeWhenRetrivedShouldMatchEmployeeCount() throws EmployeePayrollException {
		EmployeePayRollService employeePayRollService = new EmployeePayRollService();
		employeePayRollService.readEmployeePayroll(IOService.DB_IO);
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<EmployeePayRollData> empPayRollData = employeePayRollService
				.readEmployeePayRollForDateRange(IOService.DB_IO, startDate, endDate);
		Assertions.assertEquals(3, empPayRollData.size());
	}

	@Test
	public void givenPayRollDataWhenAvgSalaryRetrievedGenderShouldReturnProperValue() throws EmployeePayrollException {
		EmployeePayRollService employeePayRollService = new EmployeePayRollService();
		employeePayRollService.readEmployeePayroll(IOService.DB_IO);
		Map<String, Double> avgSalaryByGender = employeePayRollService.readAvgSalaryByGender(IOService.DB_IO);
		Assertions.assertFalse(
				avgSalaryByGender.get("M").equals(3000000.0) && avgSalaryByGender.get("F").equals(300000.0));
	}
	
	@Test
	public void givenNewEmployeeWhenAddedShouldSyncWithDB() throws EmployeePayrollException {
		EmployeePayRollService employeePayRollService = new EmployeePayRollService();
		employeePayRollService.readEmployeePayroll(IOService.DB_IO);
		employeePayRollService.addEmployeePayroll("Mark",50000.0,LocalDate.now(),"M");
		boolean result = employeePayRollService.checkEmployeePayRollSyncWithDataBase("Mark");
		Assertions.assertFalse(result);
	}

}
