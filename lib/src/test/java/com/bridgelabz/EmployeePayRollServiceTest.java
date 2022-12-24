package com.bridgelabz;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bridgelabz.EmployeePayRollService.IOService;

class EmployeePayRollServiceTest {

	@Test
	public void givenThreeEmployeesWritenToFile() {
		EmployeePayRollData[] empdata = { new EmployeePayRollData(1, "Ram", 1000),
				new EmployeePayRollData(2, "Shyam", 1000), new EmployeePayRollData(3, "Krishna", 1000),
				new EmployeePayRollData(4, "Parshu", 1000) };
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

}
