package mu.psi.nextgen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import mu.psi.nextgen.assistant.LocalData;
import mu.psi.nextgen.databinding.ActivityHomeBillsBinding;

public class HomeBills extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ActivityHomeBillsBinding binding;
    BottomNavigationView bottomNavigationView;
    String branch_name;

    LocalData data = new LocalData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeBillsBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        Intent i = getIntent();
        if(i.hasExtra("branch_name")) {
            branch_name = i.getStringExtra("branch_name");
            data.setBranchName(this, branch_name);
        } else {
            branch_name = data.getBranchName(this);
        }
        this.binding.homeBillsTitle.setText(branch_name);

        bottomNavigationView = this.binding.individualBottomBar;
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(this);

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
}