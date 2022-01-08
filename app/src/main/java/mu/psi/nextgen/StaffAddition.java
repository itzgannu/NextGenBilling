package mu.psi.nextgen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import mu.psi.nextgen.databinding.ActivityStaffAdditionBinding;

public class StaffAddition extends AppCompatActivity implements View.OnClickListener {

    ActivityStaffAdditionBinding binding;
    FirebaseAuth auth;
    String company_name;

    Dialog dialog;
    AlertDialog.Builder builder;

    private boolean cashier = false;
    private int clicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityStaffAdditionBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        auth = FirebaseAuth.getInstance();
        String name = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
        String [] splitter = Objects.requireNonNull(name).split("/");
        company_name = splitter[0];

        builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_bar);
        dialog = builder.create();

        this.binding.staffAdditionAddButton.setOnClickListener(this);
        this.binding.staffAdditionClose.setOnClickListener(this);

        RadioGroup radioGroup = this.binding.staffAdditionRadioGroup;
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.staff_addition_cashier_radio) {
                clicked = 1;
                cashier = true;
            } else if (checkedId == R.id.staff_addition_staff_radio) {
                clicked = 1;
                cashier = false;
            }
        });
    }

    private void progress_bar(boolean show) {
        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToHomeMore = new Intent(StaffAddition.this, HomeMore.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToHomeMore);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.staff_addition_add_button) {
            progress_bar(true);
            if (validateFields()) {
                String full_name = Objects.requireNonNull(this.binding.staffAdditionFullName.getEditText()).getText().toString();
                String email = Objects.requireNonNull(this.binding.staffAdditionEmailId.getEditText()).getText().toString();
                String code = Objects.requireNonNull(this.binding.staffAdditionPasscode.getEditText()).getText().toString();

                clearFields();

                if(cashier) {
                    String name = company_name+"/"+"cashier"+"/"+full_name;
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(email, code)
                            .addOnCompleteListener(this, task -> {
                                if(task.isSuccessful()) {
                                    progress_bar(false);
                                    setDisplayName(firebaseAuth, name);
                                } else {
                                    progress_bar(false);
                                }
                            })
                            .addOnFailureListener(this, e -> progress_bar(false))
                            .addOnCanceledListener(this, () -> progress_bar(false));
                } else {
                    String name = company_name+"/"+"staff"+"/"+full_name;
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(email, code)
                            .addOnCompleteListener(this, task -> {
                                if(task.isSuccessful()) {
                                    progress_bar(false);
                                    setDisplayName(firebaseAuth, name);
                                } else {
                                    progress_bar(false);
                                }
                            })
                            .addOnFailureListener(this, e -> progress_bar(false))
                            .addOnCanceledListener(this, () -> progress_bar(false));
                }
            }
        }

        if (id == R.id.staff_addition_close) {
            Intent goToHomeMore = new Intent(StaffAddition.this, HomeMore.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeMore);
            finish();
        }
    }

    void setDisplayName(FirebaseAuth auth1, String name) {
        if(auth1.getCurrentUser() != null) {
            UserProfileChangeRequest createCompany =
                    new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
            auth1.getCurrentUser().updateProfile(createCompany);
            auth1.signOut();
            Intent goToAuth = new Intent(StaffAddition.this, Authentication.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToAuth);
            finish();
        }
    }

    private boolean validateFields() {
        String full_name = Objects.requireNonNull(this.binding.staffAdditionFullName.getEditText()).getText().toString();
        String email = Objects.requireNonNull(this.binding.staffAdditionEmailId.getEditText()).getText().toString();
        String code = Objects.requireNonNull(this.binding.staffAdditionPasscode.getEditText()).getText().toString();

        String empty_field = getString(R.string.error_field_empty);
        String name_error = getString(R.string.error_name_minimum_two);
        String email_invalid = getString(R.string.error_invalid_email);
        String code_error = getString(R.string.error_password_minimum_six);

        if (clicked == 0) {
            Snackbar.make(this.binding.staffAddition, "Select User Role", Snackbar.LENGTH_LONG).show();
            progress_bar(false);
            return false;
        } else if (full_name.isEmpty() && email.isEmpty() && code.isEmpty()) {
            this.binding.staffAdditionFullName.setError(empty_field);
            this.binding.staffAdditionEmailId.setError(empty_field);
            this.binding.staffAdditionPasscode.setError(empty_field);
            progress_bar(false);
            return false;
        } else if (full_name.isEmpty()) {
            this.binding.staffAdditionFullName.setError(empty_field);
            this.binding.staffAdditionEmailId.setError(null);
            this.binding.staffAdditionPasscode.setError(null);
            progress_bar(false);
            return false;
        } else if (email.isEmpty()) {
            this.binding.staffAdditionFullName.setError(null);
            this.binding.staffAdditionEmailId.setError(empty_field);
            this.binding.staffAdditionPasscode.setError(null);
            progress_bar(false);
            return false;
        } else if (code.isEmpty()) {
            this.binding.staffAdditionFullName.setError(null);
            this.binding.staffAdditionEmailId.setError(null);
            this.binding.staffAdditionPasscode.setError(empty_field);
            progress_bar(false);
            return false;
        } else if (full_name.length() < 2) {
            this.binding.staffAdditionFullName.setError(name_error);
            this.binding.staffAdditionEmailId.setError(null);
            this.binding.staffAdditionPasscode.setError(null);
            progress_bar(false);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.binding.staffAdditionFullName.setError(null);
            this.binding.staffAdditionEmailId.setError(email_invalid);
            this.binding.staffAdditionPasscode.setError(null);
            progress_bar(false);
            return false;
        } else if (code.length() < 6) {
            this.binding.staffAdditionFullName.setError(null);
            this.binding.staffAdditionEmailId.setError(null);
            this.binding.staffAdditionPasscode.setError(code_error);
            progress_bar(false);
            return false;
        } else {
            this.binding.staffAdditionFullName.setError(null);
            this.binding.staffAdditionEmailId.setError(null);
            this.binding.staffAdditionPasscode.setError(null);
            return true;
        }
    }

    private void clearFields() {
        Objects.requireNonNull(this.binding.staffAdditionFullName.getEditText()).getText().clear();
        Objects.requireNonNull(this.binding.staffAdditionEmailId.getEditText()).getText().clear();
        Objects.requireNonNull(this.binding.staffAdditionPasscode.getEditText()).getText().clear();
    }
}