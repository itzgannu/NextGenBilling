package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import mu.psi.nextgen.databinding.ActivityStaffAdditionBinding;

public class StaffAddition extends AppCompatActivity {

    ActivityStaffAdditionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityStaffAdditionBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
    }
}