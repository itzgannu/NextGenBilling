package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import mu.psi.nextgen.adapter.recycler.Stocks;
import mu.psi.nextgen.assistant.DisplayName;
import mu.psi.nextgen.assistant.LocalData;
import mu.psi.nextgen.databinding.ActivityOrderAisleBinding;
import mu.psi.nextgen.models.inventory.Stock;
import mu.psi.nextgen.viewModel.InventoryVM;

public class OrderAisle extends AppCompatActivity implements View.OnClickListener {

    ActivityOrderAisleBinding binding;

    Context context;

    FirebaseAuth auth;

    InventoryVM viewModel;
    LocalData data;
    DisplayName displayName;

    String email;
    String company_name;

    RecyclerView recyclerView;
    Stocks recyclerAdapter;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityOrderAisleBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.context = this;
        this.data = new LocalData();
        this.auth = FirebaseAuth.getInstance();
        this.email = Objects.requireNonNull(this.auth.getCurrentUser()).getEmail();

        String name = auth.getCurrentUser().getDisplayName();
        displayName = new DisplayName(name);

        company_name = displayName.getCompanyName();

        this.viewModel = InventoryVM.getInstance(getApplication());
        this.viewModel.readProducts(company_name);
        this.viewModel.stockMLD.observe(this, new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {
                startRecycler(stocks);
            }
        });

        this.binding.orderAisleClose.setOnClickListener(this);
        this.binding.orderAisleCart.setOnClickListener(this);
    }

    private void startRecycler(List<Stock> stocks) {
        this.recyclerView = this.binding.orderAisleRecyclerView;
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerAdapter = new Stocks(stocks, this, "order");
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void goBack() {
        Intent goToHomeBills = new Intent(OrderAisle.this, HomeBills.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToHomeBills);
        finish();
    }

    private void goToOrder() {
        Intent goToOrderReview = new Intent(OrderAisle.this, OrderReview.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToOrderReview);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.order_aisle_close) {
            goBack();
        }
        else if(id == R.id.order_aisle_cart) {
            goToOrder();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }
}