package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import mu.psi.nextgen.databinding.ActivityOrderPlacedBinding;
import mu.psi.nextgen.models.bills.Invoice;

public class OrderPlaced extends AppCompatActivity implements View.OnClickListener {

    ActivityOrderPlacedBinding binding;

    String order_number;
    Invoice newInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityOrderPlacedBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        Intent i = getIntent();
        order_number = i.getStringExtra("order_number");
        newInvoice = (Invoice) i.getSerializableExtra("newInvoice");

        this.binding.orderPlacedOrderNumber.setText(order_number);

        this.binding.orderPlacedShareBillButton.setOnClickListener(this);
        this.binding.orderPlacedClose.setOnClickListener(this);
    }

    private void goBack() {
        Intent goToHomeBills = new Intent(OrderPlaced.this, HomeBills.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToHomeBills);
        finish();
    }

    private void shareBill() {
        Intent goToShareBill = new Intent(OrderPlaced.this, ShareBill.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        goToShareBill.putExtra("shareInvoice", newInvoice);
        startActivity(goToShareBill);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.order_placed_close) {
            goBack();
        }
        else if(id == R.id.order_placed_share_bill_button) {
            shareBill();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }
}