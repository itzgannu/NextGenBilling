package mu.psi.nextgen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import mu.psi.nextgen.databinding.ActivityHomeExtrasBinding;

public class HomeExtras extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ActivityHomeExtrasBinding binding;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeExtrasBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        bottomNavigationView = this.binding.individualBottomBar;
        bottomNavigationView.setSelectedItemId(R.id.nav_extras);
        bottomNavigationView.setOnItemSelectedListener(this);
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
}