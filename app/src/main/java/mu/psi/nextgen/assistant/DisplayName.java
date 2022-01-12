package mu.psi.nextgen.assistant;

public class DisplayName {
    String display_name;
    String company_name;
    String employee_role;
    String employee_name;

    public DisplayName(String display_name) {
        this.display_name = display_name;
        String [] splitter = new String[0];
        if (display_name != null) {
            splitter = display_name.split("/");
        }
        company_name = splitter[0];
        employee_role = splitter[1];
        employee_name = splitter[2];
    }

    public String getCompanyName() {
        return company_name;
    }

    public String getEmployeeRole() {
        return employee_role;
    }

    public String getEmployeeName() {
        return employee_name;
    }
}
