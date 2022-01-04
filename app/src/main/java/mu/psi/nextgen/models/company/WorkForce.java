package mu.psi.nextgen.models.company;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WorkForce implements Serializable {
    String email, full_name, company_name, role;

    public WorkForce() {
    }

    public WorkForce(String email, String full_name, String company_name, String role) {
        this.email = email;
        this.full_name = full_name;
        this.company_name = company_name;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("email", email);
        result.put("full_name", full_name);
        result.put("company_name", company_name);
        result.put("role", role);

        return result;
    }
}