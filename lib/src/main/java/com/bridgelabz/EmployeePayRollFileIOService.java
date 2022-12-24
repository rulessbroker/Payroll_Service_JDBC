package com.bridgelabz;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EmployeePayRollFileIOService {
	public static String PAY_ROLL_FILE = "F:\\Languages\\Java\\PayRoll_Service_JDBC\\lib\\src\\main\\resources\\EmpInfo.txt";

	public void writeData(List<EmployeePayRollData> employeePayRollList) {
		StringBuffer empBuffer = new StringBuffer();
		employeePayRollList.forEach(employee -> {
			String empDataString = employee.toString().concat("\n");
			empBuffer.append(empDataString);

		});
		try {
			Files.write(Paths.get(PAY_ROLL_FILE), empBuffer.toString().getBytes());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void printData() {
		try {
			Files.lines(new File(PAY_ROLL_FILE).toPath()).forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public long countEntries() {
		long entries = 0;
		try {
			entries = Files.lines(new File(PAY_ROLL_FILE).toPath()).count();
		} catch (IOException e) {
			e.printStackTrace();

		}
		return entries;

	}
}
