package mu.psi.nextgen.models.cart;

import androidx.annotation.NonNull;

import java.io.Serializable;

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

    @NonNull
    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", pack='" + pack + '\'' +
                ", pic_url='" + pic_url + '\'' +
                ", price=" + price +
                ", total=" + total +
                ", quantity=" + quantity +
                '}';
    }
}