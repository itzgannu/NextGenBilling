package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import mu.psi.nextgen.databinding.ActivityHomeStoresBinding;

public class HomeStores extends AppCompatActivity {

    ActivityHomeStoresBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeStoresBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
    }
}