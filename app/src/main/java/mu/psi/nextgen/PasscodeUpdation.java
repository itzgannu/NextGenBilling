package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import mu.psi.nextgen.databinding.ActivityPasscodeUpdationBinding;

public class PasscodeUpdation extends AppCompatActivity {

    ActivityPasscodeUpdationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityPasscodeUpdationBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
    }
}