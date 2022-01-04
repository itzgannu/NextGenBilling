package mu.psi.nextgen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import mu.psi.nextgen.databinding.ActivityAuthenticationBinding;

public class Authentication extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    ActivityAuthenticationBinding binding;

    private FirebaseAuth auth;

    Dialog dialog;
    AlertDialog.Builder builder;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        auth = FirebaseAuth.getInstance();

        builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_bar);
        dialog = builder.create();

        this.binding.authenticationScreenLogo.animate().alpha(1).setDuration(1000);

        this.binding.authenticationScreenPasscode.setError(null);
        this.binding.authenticationScreenEmailId.setError(null);

        this.binding.authenticationScreenSignUpButton.setOnClickListener(this);
        this.binding.authenticationScreenSignInButton.setOnClickListener(this);
        this.binding.authenticationScreenForgotPasswordButton.setOnClickListener(this);
        this.binding.authenticationScreen.setOnTouchListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null) {
            //some one already logged in
            redirecting_to_place_holder();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.binding.authenticationScreenPasscode.setError(null);
        this.binding.authenticationScreenEmailId.setError(null);

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null) {
            //some one already logged in
            redirecting_to_place_holder();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        this.binding.authenticationScreenPasscode.setError(null);
        this.binding.authenticationScreenEmailId.setError(null);

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null) {
            //some one already logged in
            redirecting_to_place_holder();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.authentication_screen_sign_up_button) {
            onClickSignUp();
        } else if (id == R.id.authentication_screen_sign_in_button) {
            onClickSignIn();
        } else if (id == R.id.authentication_screen_forgot_password_button) {
            onClickForgotPassword();
        }
    }

    void progress_bar(boolean show) {
        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    void redirecting_to_place_holder() {
        Intent splashIntent = new Intent(Authentication.this, Initializing.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(splashIntent);
        finish();
    }

    void onClickSignUp() {
        Intent splashIntent = new Intent(Authentication.this, Registration.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(splashIntent);
        finish();
    }

    void onClickSignIn() {
        progress_bar(true);
        this.binding.authenticationScreenEmailId.clearFocus();
        this.binding.authenticationScreenPasscode.clearFocus();

        String email = Objects.requireNonNull(this.binding.authenticationScreenEmailId.getEditText()).getText().toString();
        String code = Objects.requireNonNull(this.binding.authenticationScreenPasscode.getEditText()).getText().toString();

        if (authenticationFieldValidations()) {
            auth.signInWithEmailAndPassword(email, code)
                    .addOnCompleteListener(this, task -> {
                        if(task.isSuccessful()) {
                            progress_bar(false);
                            redirecting_to_place_holder();
                        } else {
                            progress_bar(false);
                        }
                    })
                    .addOnFailureListener(this, e -> progress_bar(false))
                    .addOnCanceledListener(this, () -> progress_bar(false));
        }
    }

    void onClickForgotPassword() {
        //firebase auth forgot password
        //show dialog after sending email
    }

    boolean authenticationFieldValidations() {
        String email = Objects.requireNonNull(this.binding.authenticationScreenEmailId.getEditText()).getText().toString();
        String code = Objects.requireNonNull(this.binding.authenticationScreenPasscode.getEditText()).getText().toString();
        String empty_field = getString(R.string.error_field_empty);
        if (email.isEmpty() && code.isEmpty()) {
            this.binding.authenticationScreenEmailId.setError(empty_field);
            this.binding.authenticationScreenPasscode.setError(empty_field);
            return false;
        } else if (email.isEmpty()) {
            this.binding.authenticationScreenEmailId.setError(empty_field);
            this.binding.authenticationScreenPasscode.setError(null);
            return false;
        } else if (code.isEmpty()) {
            this.binding.authenticationScreenPasscode.setError(empty_field);
            this.binding.authenticationScreenEmailId.setError(null);
            return false;
        } else {
            this.binding.authenticationScreenPasscode.setError(null);
            this.binding.authenticationScreenEmailId.setError(null);
            return true;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.authentication_screen) {
            InputMethodManager hideSoftKeyBoard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            hideSoftKeyBoard.hideSoftInputFromWindow(v.getWindowToken(), 0);
            this.binding.authenticationScreenEmailId.clearFocus();
            this.binding.authenticationScreenPasscode.clearFocus();
            return true;
        }
        return false;
    }
}