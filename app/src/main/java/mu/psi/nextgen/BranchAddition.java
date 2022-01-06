package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import mu.psi.nextgen.databinding.ActivityBranchAdditionBinding;

public class BranchAddition extends AppCompatActivity {

    ActivityBranchAdditionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityBranchAdditionBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
    }
}