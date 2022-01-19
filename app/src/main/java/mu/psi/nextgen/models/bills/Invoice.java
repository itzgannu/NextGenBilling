package mu.psi.nextgen.models.bills;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mu.psi.nextgen.models.cart.Item;

public class Invoice  implements Serializable {

    String customer_name;
    String customer_email;
    String company_name;
    String order_number;
    String order_date_time;

    double order_total;

    List<Item> itemList;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("customer_name", customer_name);
        result.put("customer_email", customer_email);
        result.put("company_name", company_name);
        result.put("order_number", order_number);
        result.put("order_date_time", order_date_time);
        result.put("order_total", order_total);
        result.put("itemList", itemList);

        return result;
    }

    public Invoice() {
    }

    public Invoice(String customer_name, String customer_email, String company_name, String order_number, String order_date_time, double order_total, List<Item> itemList) {
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.company_name = company_name;
        this.order_number = order_number;
        this.order_date_time = order_date_time;
        this.order_total = order_total;
        this.itemList = itemList;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public double getOrder_total() {
        return order_total;
    }

    public void setOrder_total(double order_total) {
        this.order_total = order_total;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
