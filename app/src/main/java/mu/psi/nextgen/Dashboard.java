package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import mu.psi.nextgen.databinding.ActivityDashboardBinding;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.dashboardBack.setOnClickListener(this);
    }

    private void goBack() {
        Intent goToBack = new Intent(Dashboard.this, HomeExtras.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToBack);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.dashboard_back) {
            goBack();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }
}