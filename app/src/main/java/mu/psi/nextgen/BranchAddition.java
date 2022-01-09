package mu.psi.nextgen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import mu.psi.nextgen.databinding.ActivityBranchAdditionBinding;
import mu.psi.nextgen.models.company.Branch;
import mu.psi.nextgen.viewModel.CompanyVM;

public class BranchAddition extends AppCompatActivity implements View.OnClickListener {

    ActivityBranchAdditionBinding binding;
    FirebaseAuth auth;
    String company_name;

    Dialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityBranchAdditionBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        auth = FirebaseAuth.getInstance();
        String name = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
        String [] splitter = Objects.requireNonNull(name).split("/");
        company_name = splitter[0];

        builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_bar);
        dialog = builder.create();

        this.binding.branchAdditionBranchName.setError(null);
        this.binding.branchAdditionBranchLocation.setError(null);

        this.binding.branchAdditionClose.setOnClickListener(this);
        this.binding.branchAdditionAddButton.setOnClickListener(this);
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
        Intent goToHomeMore = new Intent(BranchAddition.this, HomeMore.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToHomeMore);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.branch_addition_add_button) {
            progress_bar(true);
            if (validateFields()) {
                String branch_name = Objects.requireNonNull(this.binding.branchAdditionBranchName.getEditText()).getText().toString();
                String branch_location = Objects.requireNonNull(this.binding.branchAdditionBranchLocation.getEditText()).getText().toString();
                Branch branch = new Branch(company_name, branch_name, branch_location);
                CompanyVM companyVM = CompanyVM.getInstance(getApplication());
                companyVM.writeBranchToCFS(branch);
                clearFields();
                progress_bar(false);
            }
        } else if (id == R.id.branch_addition_close) {
            Intent goToHomeMore = new Intent(BranchAddition.this, HomeMore.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeMore);
            finish();
        }
    }

    private boolean validateFields() {
        String branch_name = Objects.requireNonNull(this.binding.branchAdditionBranchName.getEditText()).getText().toString();
        String branch_location = Objects.requireNonNull(this.binding.branchAdditionBranchLocation.getEditText()).getText().toString();

        this.binding.branchAdditionBranchName.setError(null);
        this.binding.branchAdditionBranchLocation.setError(null);

        String empty = getString(R.string.error_field_empty);
        String minimum = getString(R.string.error_field_minimum_three);

        if (branch_name.isEmpty() && branch_location.isEmpty()) {
            this.binding.branchAdditionBranchName.setError(empty);
            this.binding.branchAdditionBranchLocation.setError(empty);
            progress_bar(false);
            return false;
        } else if (branch_name.isEmpty()) {
            this.binding.branchAdditionBranchLocation.setError(null);
            this.binding.branchAdditionBranchName.setError(empty);
            progress_bar(false);
            return false;
        } else if (branch_location.isEmpty()) {
            this.binding.branchAdditionBranchName.setError(null);
            this.binding.branchAdditionBranchLocation.setError(empty);
            progress_bar(false);
            return false;
        } else if (branch_location.length() < 3 && branch_name.length() < 3) {
            this.binding.branchAdditionBranchName.setError(minimum);
            this.binding.branchAdditionBranchLocation.setError(minimum);
            progress_bar(false);
            return false;
        } else if (branch_name.length() < 3) {
            this.binding.branchAdditionBranchLocation.setError(null);
            this.binding.branchAdditionBranchName.setError(minimum);
            progress_bar(false);
            return false;
        } else if (branch_location.length() < 3) {
            this.binding.branchAdditionBranchName.setError(null);
            this.binding.branchAdditionBranchLocation.setError(minimum);
            progress_bar(false);
            return false;
        } else {
            this.binding.branchAdditionBranchName.setError(null);
            this.binding.branchAdditionBranchLocation.setError(null);
            return true;
        }
    }

    private void clearFields() {
        Objects.requireNonNull(this.binding.branchAdditionBranchName.getEditText()).getText().clear();
        Objects.requireNonNull(this.binding.branchAdditionBranchLocation.getEditText()).getText().clear();
    }
}