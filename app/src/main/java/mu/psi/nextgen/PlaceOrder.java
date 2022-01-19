package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import mu.psi.nextgen.assistant.DisplayName;
import mu.psi.nextgen.databinding.ActivityPlaceOrderBinding;
import mu.psi.nextgen.models.bills.Invoice;
import mu.psi.nextgen.models.cart.Item;
import mu.psi.nextgen.viewModel.BillsVM;
import mu.psi.nextgen.viewModel.CartVM;

public class PlaceOrder extends AppCompatActivity {

    ActivityPlaceOrderBinding binding;

    FirebaseAuth auth;
    DisplayName displayName;
    String company_name;

    Context context;

    BillsVM viewModel;
    CartVM cartModel;

    Invoice newInvoice;
    List<Item> itemList = new ArrayList<>();
    double total;
    String customer_name, customer_email, order_number;

    Handler handler = new Handler(); Runnable runnable; int delay = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityPlaceOrderBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        context = this;

        auth = FirebaseAuth.getInstance();
        String display_name = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
        displayName = new DisplayName(display_name);
        company_name = displayName.getCompanyName();

        this.viewModel = BillsVM.getInstance(getApplication());
        this.cartModel = CartVM.getInstance(getApplication());

        Intent i = getIntent();
        itemList = (List<Item>) i.getSerializableExtra("items");
        total = i.getDoubleExtra("total", 0);
        customer_name = i.getStringExtra("name");
        customer_email = i.getStringExtra("email");

        createInvoiceObject();

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                afterCreatingInvoice();
            }
        }, delay);
    }

    private void createInvoiceObject() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss", Locale.getDefault());
        String date = simpleDateFormat.format(new Date());
        Log.d("Hero", date);
        order_number = customer_name+" on "+date;
        newInvoice = new Invoice(customer_name, customer_email, company_name, order_number, date, total, itemList);

        this.viewModel.writeNewInvoice(newInvoice);
    }

    private void afterCreatingInvoice() {
        this.cartModel.deleteAllItems(displayName.getDisplayName());
        this.viewModel.readInvoices(displayName.getDisplayName());
        this.viewModel.invoiceMLD.observe(this, new Observer<List<Invoice>>() {
            @Override
            public void onChanged(List<Invoice> invoices) {
                for(Invoice invoice : invoices) {
                    String order_num = invoice.getOrder_number();
                    if(order_num.equalsIgnoreCase(order_number)) {
                        viewModel.writeNewBuyerInvoice(newInvoice);
                        Intent insertIntent = new Intent(context, OrderPlaced.class);
                        insertIntent.putExtra("order_number", order_number);
                        insertIntent.putExtra("newInvoice", newInvoice);
                        startActivity(insertIntent);
                        finish();
                    }
                }
            }
        });
        handler.removeCallbacks(runnable);//stop loop
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}