package mu.psi.nextgen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import mu.psi.nextgen.databinding.ActivityHomeMoreBinding;

public class HomeMore extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnItemSelectedListener {

    ActivityHomeMoreBinding binding;
    BottomNavigationView bottomNavigationView;

    FirebaseAuth auth;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeMoreBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        auth = FirebaseAuth.getInstance();

        bottomNavigationView = this.binding.homeBottomBar;
        bottomNavigationView.setSelectedItemId(R.id.nav_more);
        bottomNavigationView.setOnItemSelectedListener(this);

        String name = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
        String [] splitter = new String[0];
        if (name != null) {
            splitter = name.split("/");
        }
        role = splitter[1];

        if(role.equalsIgnoreCase("admin")){
            this.binding.homeMoreAddBranchButton.setVisibility(View.VISIBLE);
            this.binding.homeMoreAddEmployeeButton.setVisibility(View.VISIBLE);

            this.binding.homeMoreAddEmployeeButton.setOnClickListener(this);
            this.binding.homeMoreAddBranchButton.setOnClickListener(this);
        } else {
            this.binding.homeMoreAddBranchButton.setVisibility(View.GONE);
            this.binding.homeMoreAddEmployeeButton.setVisibility(View.GONE);
        }
        this.binding.homeMoreUpdatePasswordButton.setOnClickListener(this);
        this.binding.homeMoreAboutUsButton.setOnClickListener(this);
        this.binding.homeMoreSignOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.home_more_add_employee_button) {
            Intent goToStaffAddition = new Intent(HomeMore.this, StaffAddition.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToStaffAddition);
            finish();
        } else if(id == R.id.home_more_add_branch_button) {
            Intent goToBranchAddition = new Intent(HomeMore.this, BranchAddition.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToBranchAddition);
            finish();
        } else if(id == R.id.home_more_update_password_button) {
            Intent goToPasscodeUpdate = new Intent(HomeMore.this, PasscodeUpdation.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToPasscodeUpdate);
            finish();
        } else if(id == R.id.home_more_about_us_button) {
            Intent goToAboutUs = new Intent(HomeMore.this, AboutUs.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToAboutUs);
            finish();
        } else if(id == R.id.home_more_sign_out_button) {
            auth.signOut();
            Intent goToAuth = new Intent(HomeMore.this, Authentication.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToAuth);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.nav_stores) {
            Intent goToHomeStores = new Intent(HomeMore.this, HomeStores.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeStores);
            finish();
            return true;
        }
        return false;
    }
}