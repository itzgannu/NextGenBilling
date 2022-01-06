package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import mu.psi.nextgen.databinding.ActivityHomeBillsBinding;

public class HomeBills extends AppCompatActivity {

    ActivityHomeBillsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityHomeBillsBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
    }
}