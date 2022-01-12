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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import mu.psi.nextgen.adapter.recycler.Branches;
import mu.psi.nextgen.assistant.DisplayName;
import mu.psi.nextgen.assistant.LocalData;
import mu.psi.nextgen.databinding.ActivityHomeStoresBinding;
import mu.psi.nextgen.models.company.Branch;
import mu.psi.nextgen.viewModel.CompanyVM;

public class HomeStores extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ActivityHomeStoresBinding binding;
    BottomNavigationView bottomNavigationView;

    Context context;

    FirebaseAuth auth;

    CompanyVM viewModel;
    LocalData data;
    DisplayName displayName;

    String email;
    String company_name;

    RecyclerView recyclerView;
    Branches recyclerAdapter;
    GridLayoutManager gridLayoutManager;

    List<Branch> branchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeStoresBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.context = this;
        this.data = new LocalData();
        this.auth = FirebaseAuth.getInstance();
        this.email = Objects.requireNonNull(this.auth.getCurrentUser()).getEmail();

        bottomNavigationView = this.binding.homeBottomBar;
        bottomNavigationView.setSelectedItemId(R.id.nav_stores);
        bottomNavigationView.setOnItemSelectedListener(this);

        String name = auth.getCurrentUser().getDisplayName();
        displayName = new DisplayName(name);

        company_name = displayName.getCompanyName();

        this.viewModel = CompanyVM.getInstance(getApplication());
        this.viewModel.readBranches(company_name);
        this.viewModel.branchMLD.observe(this, new Observer<List<Branch>>() {
            @Override
            public void onChanged(List<Branch> branches) {
                branchList = branches;
                startRecycler(branchList);
            }
        });
    }

    public void startRecycler(List<Branch> branches) {
        branches = removeDuplicated(branches);
        if (branches.size() > 0) {
            this.binding.homeNoStores.setVisibility(View.GONE);
        } else {
            this.binding.homeNoStores.setVisibility(View.VISIBLE);
        }
        this.recyclerView = this.binding.homeStores;
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerAdapter = new Branches(branches, this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private List<Branch> removeDuplicated(List<Branch> branches) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return branches.stream().distinct().collect(Collectors.toList());
        } else {
            return branches;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_more) {
            Intent goToHomeMore = new Intent(HomeStores.this, HomeMore.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeMore);
            finish();
            return true;
        }
        return false;
    }
}