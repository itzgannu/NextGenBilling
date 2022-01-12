package mu.psi.nextgen.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import mu.psi.nextgen.firebase.InventoryFirestore;
import mu.psi.nextgen.models.inventory.Stock;
import mu.psi.nextgen.models.inventory.StockNoURL;
import mu.psi.nextgen.models.inventory.StockUpdateNoURL;

public class InventoryVM extends AndroidViewModel {

    InventoryFirestore inventoryFirestore = new InventoryFirestore();

    public List<Stock> stockList = new ArrayList<>();
    public MutableLiveData<List<Stock>> stockMLD = new MutableLiveData<>();

    private static InventoryVM instance;

    public static InventoryVM getInstance(Application application) {
        if(instance == null) {
            instance =new InventoryVM(application);
        }
        return instance;
    }
    public InventoryVM(@NonNull Application application) {
        super(application);
        this.stockMLD = this.inventoryFirestore.stockMLD;
    }

    public void writeNewProduct(String company_name, byte[] uploadByte, StockNoURL product) {
        inventoryFirestore.addNewStock(company_name, uploadByte, product);
    }

    public void updateExistingProduct(String company_name, byte[] uploadByte, StockUpdateNoURL product) {
        inventoryFirestore.updateStock(company_name, uploadByte, product);
    }

    public void deleteExistingProduct(String company_name, String product_name) {
        inventoryFirestore.deleteStock(company_name, product_name);
    }

    public void readProducts(String company_name) {
        inventoryFirestore.getAllStocks(company_name);
        this.stockMLD = this.inventoryFirestore.stockMLD;
        this.stockList = this.stockMLD.getValue();
    }
}
