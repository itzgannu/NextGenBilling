package mu.psi.nextgen.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import mu.psi.nextgen.firebase.BillsFirestore;
import mu.psi.nextgen.firebase.CartFirestore;
import mu.psi.nextgen.models.bills.Invoice;
import mu.psi.nextgen.models.cart.Item;

public class BillsVM extends AndroidViewModel {

    BillsFirestore billsFirestore = new BillsFirestore();

    public List<Invoice> invoiceList = new ArrayList<>();
    public MutableLiveData<List<Invoice>> invoiceMLD = new MutableLiveData<>();

    private static BillsVM instance;

    public static BillsVM getInstance(Application application) {
        if(instance == null) {
            instance =new BillsVM(application);
        }
        return instance;
    }

    public BillsVM(@NonNull Application application) {
        super(application);
        this.invoiceMLD = this.billsFirestore.invoiceMLD;
    }

    public void writeNewInvoice(Invoice newInvoice) {
        this.billsFirestore.addInvoice(newInvoice);
    }

    public void writeNewBuyerInvoice(Invoice newInvoice) {
        this.billsFirestore.addBuyerBill(newInvoice);
    }

    public void writeShareBuyerInvoice(Invoice newInvoice, String email) {
        this.billsFirestore.shareBuyerBill(newInvoice, email);
    }

    public void readInvoices(String display_name) {
        this.billsFirestore.getAllBills(display_name);
        this.invoiceMLD = this.billsFirestore.invoiceMLD;
        this.invoiceList = this.invoiceMLD.getValue();
    }
}