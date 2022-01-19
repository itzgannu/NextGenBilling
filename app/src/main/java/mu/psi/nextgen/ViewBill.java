package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import mu.psi.nextgen.adapter.recycler.Items;
import mu.psi.nextgen.databinding.ActivityViewBillBinding;
import mu.psi.nextgen.models.bills.Invoice;
import mu.psi.nextgen.models.cart.Item;

public class ViewBill extends AppCompatActivity {

    ActivityViewBillBinding binding;

    Invoice invoice = new Invoice();

    String customer_name, customer_email, order_number, order_date_time, order_total;
    List<Item> itemList = new ArrayList<>();

    //Recycler View Variables
    RecyclerView recyclerView;
    Items recyclerItemsView;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityViewBillBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        Intent i = getIntent();
        invoice = (Invoice) i.getSerializableExtra("invoice");

        assignValues();

        this.binding.viewBillClose.setOnClickListener(v -> goBack());
    }

    private void assignValues() {
        customer_name = invoice.getCustomer_name();
        customer_email = invoice.getCustomer_email();
        order_number = invoice.getOrder_number();
        order_date_time = String.valueOf(invoice.getOrder_date_time());
        order_total = String.valueOf(invoice.getOrder_total());
        itemList = invoice.getItemList();

        this.binding.viewBillCustomerName.setText(customer_name);
        this.binding.viewBillCustomerEmail.setText(customer_email);
        this.binding.viewBillTitle.setText(order_number);
        this.binding.viewBillOrderTiming.setText(order_date_time);
        this.binding.viewBillOrderAmount.setText(order_total);

        initializeItems();
    }

    private void initializeItems() {
        this.recyclerView = this.binding.viewBillRecycler;
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerItemsView = new Items(itemList, this);
        recyclerView.setAdapter(recyclerItemsView);
    }

    private void goBack() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}