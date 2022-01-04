package mu.psi.nextgen.models.inventory;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class StockNoURL implements Serializable {
    String name, description, pack;
    double price;
    int inStock, soldStock;

    public StockNoURL() {
    }

    public StockNoURL(String name, String description, String pack, double price, int inStock, int soldStock) {
        this.name = name;
        this.description = description;
        this.pack = pack;
        this.price = price;
        this.inStock = inStock;
        this.soldStock = soldStock;
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

    public int getSoldStock() {
        return soldStock;
    }

    public void setSoldStock(int soldStock) {
        this.soldStock = soldStock;
    }

    @NonNull
    @Override
    public String toString() {
        return "StockNoURL{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pack='" + pack + '\'' +
                ", price=" + price +
                ", inStock=" + inStock +
                ", soldStock=" + soldStock +
                '}';
    }
}