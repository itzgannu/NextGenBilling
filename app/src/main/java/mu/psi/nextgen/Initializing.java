package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mu.psi.nextgen.databinding.ActivityInitializingBinding;

public class Initializing extends AppCompatActivity {

    ActivityInitializingBinding binding;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityInitializingBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
    }
}