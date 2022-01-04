package mu.psi.nextgen.models.inventory;

import java.io.Serializable;

public class Stock implements Serializable {
    String name, description, pack, pic_url;
    double price;
    int inStock, soldStock;

    public Stock() {
    }

    public Stock(String name, String description, String pack, String pic_url, double price, int inStock, int soldStock) {
        this.name = name;
        this.description = description;
        this.pack = pack;
        this.pic_url = pic_url;
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

    @Override
    public String toString() {
        return "Stock{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pack='" + pack + '\'' +
                ", pic_url='" + pic_url + '\'' +
                ", price=" + price +
                ", inStock=" + inStock +
                ", soldStock=" + soldStock +
                '}';
    }
}