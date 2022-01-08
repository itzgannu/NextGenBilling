package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import mu.psi.nextgen.databinding.ActivityPasscodeUpdationBinding;

public class PasscodeUpdation extends AppCompatActivity implements View.OnClickListener {

    ActivityPasscodeUpdationBinding binding;

    Dialog dialog;
    androidx.appcompat.app.AlertDialog.Builder builder;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityPasscodeUpdationBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.passcodeCode.setError(null);

        builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(R.layout.progress_bar);
        dialog = builder.create();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        this.binding.passcodeClose.setOnClickListener(this);
        this.binding.passcodeChangeButton.setOnClickListener(this);
    }

    private void dialogBox() {
        String message, success;
        message = getString(R.string.updatedPassword);
        success = getString(R.string.success);
        String okay = getString(R.string.okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(success);
        builder.setPositiveButton(okay, (dialog, id) -> dialog.dismiss());
        builder.create();
        builder.show();
    }

    private void progress_bar(boolean show) {
        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.passcode_close) {
            Intent goToHomeMore = new Intent(PasscodeUpdation.this, HomeMore.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeMore);
            finish();
        } else if(id == R.id.passcode_change_button) {
            String code = Objects.requireNonNull(this.binding.passcodeCode.getEditText()).getText().toString();
            progress_bar(true);
            if(validateField()) {
                if (firebaseUser != null) {
                    firebaseUser.updatePassword(code)
                            .addOnCompleteListener(task -> {
                                progress_bar(false);
                                dialogBox();
                            })
                            .addOnFailureListener(e -> {
                                firebaseAuth.signOut();
                                progress_bar(false);
                                Intent goToAuth = new Intent(PasscodeUpdation.this, Authentication.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                goToAuth.putExtra("something_went_wrong", true);
                                startActivity(goToAuth);
                                finish();
                            })
                            .addOnCanceledListener(() -> {
                                firebaseAuth.signOut();
                                progress_bar(false);
                                Intent goToAuth = new Intent(PasscodeUpdation.this, Authentication.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                goToAuth.putExtra("something_went_wrong", true);
                                startActivity(goToAuth);
                                finish();
                            });
                }
                Objects.requireNonNull(this.binding.passcodeCode.getEditText()).getText().clear();
            }
        }

    }

    private boolean validateField() {
        String code = Objects.requireNonNull(this.binding.passcodeCode.getEditText()).getText().toString();
        if(code.isEmpty()) {
            this.binding.passcodeCode.setError(getString(R.string.error_field_empty));
            return false;
        } else if(code.length() < 6) {
            this.binding.passcodeCode.setError(getString(R.string.error_password_minimum_six));
            return false;
        } else {
            this.binding.passcodeCode.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToHomeMore = new Intent(PasscodeUpdation.this, HomeMore.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToHomeMore);
        finish();
    }
}