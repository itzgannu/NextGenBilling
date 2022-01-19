package mu.psi.nextgen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import mu.psi.nextgen.adapter.recycler.Invoices;
import mu.psi.nextgen.assistant.DisplayName;
import mu.psi.nextgen.assistant.LocalData;
import mu.psi.nextgen.databinding.ActivityHomeBillsBinding;
import mu.psi.nextgen.models.bills.Invoice;
import mu.psi.nextgen.viewModel.BillsVM;

public class HomeBills extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    ActivityHomeBillsBinding binding;
    BottomNavigationView bottomNavigationView;
    String branch_name;

    Context context;
    FirebaseAuth auth;

    LocalData data = new LocalData();
    DisplayName displayName;

    BillsVM viewModel;
    String email, company_name;

    RecyclerView recyclerView;
    Invoices recyclerAdapter;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeBillsBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.context = this;
        this.data = new LocalData();
        this.auth = FirebaseAuth.getInstance();
        this.email = Objects.requireNonNull(this.auth.getCurrentUser()).getEmail();

        String name = auth.getCurrentUser().getDisplayName();
        displayName = new DisplayName(name);
        company_name = displayName.getCompanyName();

        Intent i = getIntent();
        if(i.hasExtra("branch_name")) {
            branch_name = i.getStringExtra("branch_name");
            data.setBranchName(this, branch_name);
        } else {
            branch_name = data.getBranchName(this);
        }
        this.binding.homeBillsTitle.setText(branch_name);

        this.viewModel = BillsVM.getInstance(getApplication());
        this.viewModel.readInvoices(displayName.getDisplayName());
        this.viewModel.invoiceMLD.observe(this, new Observer<List<Invoice>>() {
            @Override
            public void onChanged(List<Invoice> invoices) {
                startRecycler(invoices);
            }
        });

        bottomNavigationView = this.binding.individualBottomBar;
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(this);

        this.binding.homeBillsCart.setOnClickListener(this);
    }

    private void startRecycler(List<Invoice> invoiceList) {
        this.recyclerView = this.binding.homeBillsRecyclerView;
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerAdapter = new Invoices(invoiceList, this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void goToCart() {
        Intent goToOrderAisle = new Intent(HomeBills.this, OrderAisle.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToOrderAisle);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        data.setBranchName(this, "");
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_stocks) {
            Intent goToHomeStocks = new Intent(HomeBills.this, HomeStocks.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeStocks);
            finish();
            return true;
        }
        else if (itemId == R.id.nav_extras) {
            Intent goToHomeExtras = new Intent(HomeBills.this, HomeExtras.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeExtras);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.home_bills_cart) {
            goToCart();
        }
    }
}