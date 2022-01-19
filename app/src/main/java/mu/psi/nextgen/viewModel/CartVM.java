package mu.psi.nextgen.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import mu.psi.nextgen.firebase.CartFirestore;
import mu.psi.nextgen.models.cart.Item;

public class CartVM extends AndroidViewModel {
    CartFirestore cartFirestore = new CartFirestore();

    public List<Item> itemList = new ArrayList<>();
    public MutableLiveData<List<Item>> itemMLD = new MutableLiveData<>();
    public MutableLiveData<Item> singleItemMLD = new MutableLiveData<>();

    private static CartVM instance;

    public static CartVM getInstance(Application application) {
        if(instance == null) {
            instance =new CartVM(application);
        }
        return instance;
    }

    public CartVM(@NonNull Application application) {
        super(application);
        this.itemMLD = this.cartFirestore.itemMLD;
        this.singleItemMLD = this.cartFirestore.singleItemMLD;
    }

    public void updateCart(String display_name, Item updatedItem) {
        cartFirestore.updateCart(display_name, updatedItem);
    }

    public void deleteExistingItem(String display_name, String product_name) {
        cartFirestore.deleteCart(display_name, product_name);
    }

    public void deleteAllItems(String display_name) {
        cartFirestore.emptyCart(display_name);
    }

    public void readItems(String display_name) {
        cartFirestore.getItems(display_name);
        this.itemMLD = this.cartFirestore.itemMLD;
        this.itemList = this.itemMLD.getValue();
    }

    public void readItem(String display_name, String item_name) {
        cartFirestore.getItem(display_name, item_name);
        this.singleItemMLD = this.cartFirestore.singleItemMLD;
    }
}
