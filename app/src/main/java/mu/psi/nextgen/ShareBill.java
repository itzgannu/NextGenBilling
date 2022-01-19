package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import java.util.Objects;

import mu.psi.nextgen.databinding.ActivityShareBillBinding;
import mu.psi.nextgen.models.bills.Invoice;
import mu.psi.nextgen.viewModel.BillsVM;

public class ShareBill extends AppCompatActivity implements View.OnClickListener {

    ActivityShareBillBinding binding;

    Invoice shareInvoice;
    BillsVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityShareBillBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        Intent i = getIntent();
        shareInvoice = (Invoice) i.getSerializableExtra("shareInvoice");

        this.viewModel = BillsVM.getInstance(getApplication());

        this.binding.shareBillButton.setOnClickListener(this);
        this.binding.shareBillClose.setOnClickListener(this);
    }

    private void goBack() {
        Intent goToHomeBills = new Intent(ShareBill.this, HomeBills.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToHomeBills);
        finish();
    }

    private void shareButton() {
        this.binding.shareBillEmailId.setError(null);
        String email = Objects.requireNonNull(this.binding.shareBillEmailId.getEditText()).getText().toString();
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.viewModel.writeShareBuyerInvoice(shareInvoice, email);
            this.binding.shareBillEmailId.getEditText().getText().clear();
        } else {
            this.binding.shareBillEmailId.setError(getString(R.string.error_invalid_email));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.share_bill_button) {
            shareButton();
        }
        else if(id == R.id.share_bill_close) {
            goBack();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }
}