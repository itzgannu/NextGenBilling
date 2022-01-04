package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import mu.psi.nextgen.databinding.ActivityHomeMoreBinding;

public class HomeMore extends AppCompatActivity {

    ActivityHomeMoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeMoreBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
    }
}