package mu.psi.nextgen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import mu.psi.nextgen.adapter.recycler.Stocks;
import mu.psi.nextgen.assistant.DisplayName;
import mu.psi.nextgen.assistant.LocalData;
import mu.psi.nextgen.databinding.ActivityHomeStocksBinding;
import mu.psi.nextgen.models.inventory.Stock;
import mu.psi.nextgen.viewModel.InventoryVM;

public class HomeStocks extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    ActivityHomeStocksBinding binding;
    BottomNavigationView bottomNavigationView;

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

        this.binding = ActivityHomeStocksBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.context = this;
        this.data = new LocalData();
        this.auth = FirebaseAuth.getInstance();
        this.email = Objects.requireNonNull(this.auth.getCurrentUser()).getEmail();

        bottomNavigationView = this.binding.individualBottomBar;
        bottomNavigationView.setSelectedItemId(R.id.nav_stocks);
        bottomNavigationView.setOnItemSelectedListener(this);

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

        this.binding.homeStocksAdd.setOnClickListener(this);
    }

    private void startRecycler(List<Stock> stocks) {
        this.recyclerView = this.binding.homeStocksRecyclerView;
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerAdapter = new Stocks(stocks, this, "update");
        recyclerView.setAdapter(recyclerAdapter);
    }

    private List<Stock> removeDuplicated(List<Stock> stocks) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return stocks.stream().distinct().collect(Collectors.toList());
        } else {
            return stocks;
        }
    }

    private void goToStockAddition() {
        Intent goToStockAdditionScreen = new Intent(HomeStocks.this, StockAddition.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToStockAdditionScreen);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            Intent goToHomeBills = new Intent(HomeStocks.this, HomeBills.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeBills);
            finish();
            return true;
        }
        else if (itemId == R.id.nav_extras) {
            Intent goToHomeExtras = new Intent(HomeStocks.this, HomeExtras.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeExtras);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToHomeBills = new Intent(HomeStocks.this, HomeBills.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToHomeBills);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.home_stocks_add) {
            goToStockAddition();
        }
    }
}