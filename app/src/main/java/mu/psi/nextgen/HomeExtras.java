package mu.psi.nextgen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import mu.psi.nextgen.assistant.LocalData;
import mu.psi.nextgen.databinding.ActivityHomeExtrasBinding;

public class HomeExtras extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    ActivityHomeExtrasBinding binding;
    BottomNavigationView bottomNavigationView;

    FirebaseAuth auth;

    LocalData data = new LocalData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeExtrasBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        auth = FirebaseAuth.getInstance();

        bottomNavigationView = this.binding.individualBottomBar;
        bottomNavigationView.setSelectedItemId(R.id.nav_extras);
        bottomNavigationView.setOnItemSelectedListener(this);

        this.binding.homeExtrasSignOut.setOnClickListener(this);
        this.binding.homeExtrasDashboard.setOnClickListener(this);

    }

    private void dashBoard() {
        Intent goToDashboard = new Intent(HomeExtras.this, Dashboard.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToDashboard);
        finish();
    }

    private void signOut() {
        data.setBranchName(this, "");
        auth.signOut();
        Intent goToAuth = new Intent(HomeExtras.this, Authentication.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToAuth);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            Intent goToHomeBills = new Intent(HomeExtras.this, HomeBills.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeBills);
            finish();
            return true;
        }
        else if (itemId == R.id.nav_stocks) {
            Intent goToHomeStocks = new Intent(HomeExtras.this, HomeStocks.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeStocks);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToHomeBills = new Intent(HomeExtras.this, HomeBills.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToHomeBills);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.home_extras_sign_out) {
            signOut();
        }
        else if(id == R.id.home_extras_dashboard) {
            dashBoard();
        }
    }
}