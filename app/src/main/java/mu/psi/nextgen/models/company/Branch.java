package mu.psi.nextgen.models.company;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Branch implements Serializable {
    String company_name, branch_name, branch_location;

    public Branch() {
    }

    public Branch(String company_name, String branch_name, String branch_location) {
        this.company_name = company_name;
        this.branch_name = branch_name;
        this.branch_location = branch_location;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBranch_location() {
        return branch_location;
    }

    public void setBranch_location(String branch_location) {
        this.branch_location = branch_location;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("branch_name", branch_name);
        result.put("branch_location", branch_location);
        result.put("company_name", company_name);

        return result;
    }
}