package mu.psi.nextgen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import mu.psi.nextgen.databinding.ActivityRegistrationBinding;
import mu.psi.nextgen.models.company.Admin;
import mu.psi.nextgen.model.CompanyVM;

public class Registration extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    ActivityRegistrationBinding binding;

    private FirebaseAuth auth; CompanyVM companyVM;

    Dialog dialog;
    AlertDialog.Builder builder;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        auth = FirebaseAuth.getInstance();
        companyVM = CompanyVM.getInstance(getApplication());

        builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_bar);
        dialog = builder.create();

        this.binding.registrationFullName.setError(null);
        this.binding.registrationEmailId.setError(null);
        this.binding.registrationPasscode.setError(null);
        this.binding.registrationCompanyName.setError(null);

        this.binding.registrationScreen.setOnTouchListener(this);
        this.binding.registrationRegisterButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null) {
            //some one already logged in
            Intent splashIntent = new Intent(Registration.this, Initializing.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(splashIntent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.binding.registrationFullName.setError(null);
        this.binding.registrationEmailId.setError(null);
        this.binding.registrationPasscode.setError(null);
        this.binding.registrationCompanyName.setError(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        this.binding.registrationFullName.setError(null);
        this.binding.registrationEmailId.setError(null);
        this.binding.registrationPasscode.setError(null);
        this.binding.registrationCompanyName.setError(null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.registration_register_button) {
            registerUser();
        }
    }

    void registerUser() {
        progress_bar(true);
        if (registrationFieldValidations()) {
            String full_name = Objects.requireNonNull(this.binding.registrationFullName.getEditText()).getText().toString();
            String email = Objects.requireNonNull(this.binding.registrationEmailId.getEditText()).getText().toString();
            String code = Objects.requireNonNull(this.binding.registrationPasscode.getEditText()).getText().toString();
            String company_name = Objects.requireNonNull(this.binding.registrationCompanyName.getEditText()).getText().toString();
            Admin admin = new Admin(email,full_name,company_name);

            auth.createUserWithEmailAndPassword(email, code)
                    .addOnCompleteListener(this, task -> {
                        if(task.isSuccessful()) {
                            progress_bar(false);
                            companyVM.writeAdminToCFS(admin);
                            informing_user(company_name);
                        } else {
                            progress_bar(false);
                        }
                    })
                    .addOnFailureListener(this, e -> progress_bar(false))
                    .addOnCanceledListener(this, () -> progress_bar(false));
            clearTextFields();
        }
    }

    void setCompanyName(String company_name) {
        if(auth.getCurrentUser() != null) {
            UserProfileChangeRequest createCompany =
                    new UserProfileChangeRequest.Builder()
                            .setDisplayName(company_name)
                            .build();
            auth.getCurrentUser().updateProfile(createCompany);
        }
    }

    void informing_user(String company_name) {
        setCompanyName(company_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String registered = getString(R.string.registered);
        String success = getString(R.string.success);
        String okay = getString(R.string.okay);
        builder.setMessage(registered)
                .setTitle(success);
        builder.setPositiveButton(okay, (dialog, id) -> dialog.dismiss());
        builder.create();
        builder.show();
    }

    void progress_bar(boolean show) {
        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    boolean registrationFieldValidations() {
        String full_name = Objects.requireNonNull(this.binding.registrationFullName.getEditText()).getText().toString();
        String email = Objects.requireNonNull(this.binding.registrationEmailId.getEditText()).getText().toString();
        String code = Objects.requireNonNull(this.binding.registrationPasscode.getEditText()).getText().toString();
        String company_name = Objects.requireNonNull(this.binding.registrationCompanyName.getEditText()).getText().toString();
        String empty_field = getString(R.string.error_field_empty);
        String name_error = getString(R.string.error_name_minimum_two);
        String email_invalid = getString(R.string.error_invalid_email);
        String code_error = getString(R.string.error_password_minimum_six);
        String company_error = getString(R.string.error_company_minimum_two);
        if (full_name.isEmpty() && email.isEmpty() && code.isEmpty() && company_name.isEmpty()) {
            this.binding.registrationFullName.setError(empty_field);
            this.binding.registrationEmailId.setError(empty_field);
            this.binding.registrationPasscode.setError(empty_field);
            this.binding.registrationCompanyName.setError(empty_field);
            return false;
        } else if (full_name.isEmpty()) {
            this.binding.registrationFullName.setError(empty_field);
            this.binding.registrationEmailId.setError(null);
            this.binding.registrationPasscode.setError(null);
            this.binding.registrationCompanyName.setError(null);
            return false;
        } else if (email.isEmpty()) {
            this.binding.registrationFullName.setError(null);
            this.binding.registrationEmailId.setError(empty_field);
            this.binding.registrationPasscode.setError(null);
            this.binding.registrationCompanyName.setError(null);
            return false;
        } else if (code.isEmpty()) {
            this.binding.registrationFullName.setError(null);
            this.binding.registrationEmailId.setError(null);
            this.binding.registrationPasscode.setError(empty_field);
            this.binding.registrationCompanyName.setError(null);
            return false;
        } else if (company_name.isEmpty()) {
            this.binding.registrationFullName.setError(null);
            this.binding.registrationEmailId.setError(null);
            this.binding.registrationPasscode.setError(null);
            this.binding.registrationCompanyName.setError(empty_field);
            return false;
        } else if (full_name.length() < 2) {
            this.binding.registrationFullName.setError(name_error);
            this.binding.registrationEmailId.setError(null);
            this.binding.registrationPasscode.setError(null);
            this.binding.registrationCompanyName.setError(null);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.binding.registrationFullName.setError(null);
            this.binding.registrationEmailId.setError(email_invalid);
            this.binding.registrationPasscode.setError(null);
            this.binding.registrationCompanyName.setError(null);
            return false;
        } else if (code.length() < 6) {
            this.binding.registrationFullName.setError(null);
            this.binding.registrationEmailId.setError(null);
            this.binding.registrationPasscode.setError(code_error);
            this.binding.registrationCompanyName.setError(null);
            return false;
        } else if (company_name.length() < 2) {
            this.binding.registrationFullName.setError(null);
            this.binding.registrationEmailId.setError(null);
            this.binding.registrationPasscode.setError(null);
            this.binding.registrationCompanyName.setError(company_error);
            return false;
        } else {
            this.binding.registrationFullName.setError(null);
            this.binding.registrationEmailId.setError(null);
            this.binding.registrationPasscode.setError(null);
            this.binding.registrationCompanyName.setError(null);
            return true;
        }
    }

    void clearTextFields() {
        Objects.requireNonNull(this.binding.registrationFullName.getEditText()).getText().clear();
        Objects.requireNonNull(this.binding.registrationEmailId.getEditText()).getText().clear();
        Objects.requireNonNull(this.binding.registrationPasscode.getEditText()).getText().clear();
        Objects.requireNonNull(this.binding.registrationCompanyName.getEditText()).getText().clear();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.registration_screen) {
            InputMethodManager hideSoftKeyBoard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            hideSoftKeyBoard.hideSoftInputFromWindow(v.getWindowToken(), 0);
            this.binding.registrationFullName.clearFocus();
            this.binding.registrationEmailId.clearFocus();
            this.binding.registrationPasscode.clearFocus();
            this.binding.registrationCompanyName.clearFocus();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent splashIntent = new Intent(Registration.this, Authentication.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(splashIntent);
        finish();
    }
}