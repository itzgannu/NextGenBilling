package mu.psi.nextgen.models.inventory;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StockUpdateNoURL implements Serializable {

    String name, description, pack;
    double price;
    int inStock;

    public StockUpdateNoURL() {
    }

    public StockUpdateNoURL(String name, String description, String pack, double price, int inStock) {
        this.name = name;
        this.description = description;
        this.pack = pack;
        this.price = price;
        this.inStock = inStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("description", description);
        result.put("pack", pack);
        result.put("price", price);
        result.put("inStock", inStock);

        return result;
    }
}
