package mu.psi.nextgen.models.company;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cashier implements Serializable {
    String email, full_name, company_name;
    final String role = "cashier";

    public Cashier() {
    }

    public Cashier(String email, String full_name, String company_name) {
        this.email = email;
        this.full_name = full_name;
        this.company_name = company_name;
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
