package mu.psi.nextgen.models.cart;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Item implements Serializable {
    String name, pack, pic_url;
    double price, total;
    int quantity;

    public Item() {
    }

    public Item(String name, String pack, String pic_url, double price, double total, int quantity) {
        this.name = name;
        this.pack = pack;
        this.pic_url = pic_url;
        this.price = price;
        this.total = total;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("pack", pack);
        result.put("pic_url", pic_url);
        result.put("price", price);
        result.put("total", total);
        result.put("quantity", quantity);

        return result;
    }
}