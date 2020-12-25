package employeepayrollservice;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class EmployeePayrollServiceTest {
    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEnteries(){
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
                new EmployeePayrollData(2, "Bill Gates", 200000.0),
                new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0)
        };
        EmployeePayrollService employeePayrollService;
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long enteries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        Assert.assertEquals(3, enteries);
    }

    @Test
    public void givenEmployeePayrollDBWhenRetrivedShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployeeWhenUpdatedShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        LocalDate startDate = LocalDate.of(2018, 01, 01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData =
                employeePayrollService.readEmployeePayrollForDateRange(EmployeePayrollService
                        .IOService.DB_IO, startDate, endDate);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    @Test
    public void givenPayrollDataWhenAverageSalaryRetrievedByGenderShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Map<String, Double> averageSalaryByGender = employeePayrollService
                .readAverageSalaryByGender(EmployeePayrollService.IOService.DB_IO);
        Assert.assertTrue(averageSalaryByGender.get("M").equals(2000000.00) &&
                averageSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    //uc7
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() {
	EmployeePayrollService employeepayrollService = new EmployeePayrollService();
	employeepayrollService.readEmployeePayrollData(DB_IO);
	employeepayrollService.addEmployeeToPayroll("MAHI", 60000.00, LocalDate.now(), "F");
	boolean result = employeepayrollService.checkEmployeePayrollInSyncWithDB("MAHI");
	Assert.assertTrue(result);
    }

    @Test
    //UC9
    public void givenEmployees_WhenAddedToDB_ShouldMatchEmployeeEntries() {
	EmployeePayrollData[] arrayOfEmps = {
			new EmployeePayrollData(0, "shweta", "F", 10000.0, LocalDate.now()),
			new EmployeePayrollData(0, "Neha", "F", 20000.0, LocalDate.now()),
			new EmployeePayrollData(0, "Riyansh", "M", 40000, LocalDate.now()),
			new EmployeePayrollData(0, "Sahil", "M", 35000.0, LocalDate.now()),
			new EmployeePayrollData(0, "Nupoor", "F", 60000.0, LocalDate.now()),
    };
		EmployeePayrollService employeepayrollService = new EmployeePayrollService();
		employeepayrollService.readEmployeePayrollData(DB_IO);
		Instant start = Instant.now();
		employeepayrollService.addEmployeeToPayroll(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		System.out.println("Duration without thread:" + Duration.between(start, end));
		Instant threadStart = Instant.now();
		employeepayrollService.addEmployeePayrollWithThread(Arrays.asList(arrayOfEmps));
		Instant threadEnd = Instant.now();
		System.out.println("Duration with thread" + Duration.between(threadStart, threadEnd));
		employeepayrollService.printData(DB_IO);
		Assert.assertEquals(12, employeepayrollService.countEntries(DB_IO));
	}

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    public EmployeePayrollData[] getEmployeeList() {
        Response response = RestAssured.get("/employee_payroll");
        System.out.println("Employee Payroll entries in jsonServer:\n" + response.asString());
        EmployeePayrollData[] arrayOfEmps = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
        return arrayOfEmps;
    }

    @Test
    public void givenEmployeeDataInJsonServer_WhenRetrieved_ShouldMatchTheCount() {
        EmployeePayrollData[] arrayOfEmps = getEmployeeList();
        EmployeePayrollService employeePayrollService;
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
        Assert.assertEquals(2, entries);
    }

}
