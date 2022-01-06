package mu.psi.nextgen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import mu.psi.nextgen.databinding.ActivityHomeStoresBinding;

public class HomeStores extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ActivityHomeStoresBinding binding;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeStoresBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        bottomNavigationView = this.binding.homeBottomBar;
        bottomNavigationView.setSelectedItemId(R.id.nav_stores);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.nav_more) {
            Intent goToHomeMore = new Intent(HomeStores.this, HomeMore.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeMore);
            finish();
            return true;
        }
        return false;
    }
}