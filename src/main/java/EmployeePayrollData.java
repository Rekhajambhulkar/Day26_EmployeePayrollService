package employeepayrollservice;

public class EmployeePayrollData {

    public int id;
    public String name;
    public double salary;
    public LocalDate startDate;

    public EmployeePayrollData(Integer id, String name, Double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public EmployeePayrollData(Integer id, String name, Double salary, LocalDate startDate) {
        this(id, name, salary);
        this.startDate =startDate;
    }

    @Override
    public String toString() {
        return "EmployeePayrollData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", startDate=" + startDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayrollData that = (EmployeePayrollData) o;
        return id == that.id &&
                Double.compare(that.salary, salary) == 0 &&
                name.equals(that.name);
    }
}
