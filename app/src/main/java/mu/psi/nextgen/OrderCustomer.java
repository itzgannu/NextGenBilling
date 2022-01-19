package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mu.psi.nextgen.databinding.ActivityOrderCustomerBinding;
import mu.psi.nextgen.models.cart.Item;

public class OrderCustomer extends AppCompatActivity implements View.OnClickListener {

    ActivityOrderCustomerBinding binding;

    List<Item> itemList = new ArrayList<>();
    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityOrderCustomerBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        Intent i = getIntent();
        itemList = (List<Item>) i.getSerializableExtra("items");
        total = i.getDoubleExtra("total", 0);

        this.binding.orderCustomerBack.setOnClickListener(this);
        this.binding.orderCustomerButton.setOnClickListener(this);
    }

    private void button() {
        if(validateField()) {
            String name = getName();
            String email = getEMail();

            Intent goToPlaceOrder = new Intent(OrderCustomer.this, PlaceOrder.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            goToPlaceOrder.putExtra("items", (Serializable) itemList);
            goToPlaceOrder.putExtra("total", total);
            goToPlaceOrder.putExtra("name", name);
            goToPlaceOrder.putExtra("email", email);
            startActivity(goToPlaceOrder);
            finish();
        }
    }

    private boolean validateField() {
        String name = getName();
        String email = getEMail();

        String empty = getString(R.string.error_field_empty);
        String emailPattern = getString(R.string.error_invalid_email);

        nullError();

        if(name.isEmpty() && email.isEmpty()) {
            setNameError(empty);
            setEMailError(empty);
            return false;
        }
        else if(name.isEmpty()) {
            setNameError(empty);
            setEMailError(null);
            return false;
        }
        else if(email.isEmpty()) {
            setNameError(null);
            setEMailError(empty);
            return false;
        }
        else if(! Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setNameError(null);
            setEMailError(emailPattern);
            return false;
        }
        else {
            nullError();
            return true;
        }
    }

    private void nullError() {
        setNameError(null);
        setEMailError(null);
    }

    private String getName() {
        return Objects.requireNonNull(this.binding.orderCustomerName.getEditText()).getText().toString();
    }

    private String getEMail() {
        return Objects.requireNonNull(this.binding.orderCustomerEmailId.getEditText()).getText().toString();
    }

    private void setNameError(String error) {
        this.binding.orderCustomerName.setError(error);
    }

    private void setEMailError(String error) {
        this.binding.orderCustomerEmailId.setError(error);
    }

    private void goBack() {
        Intent goToOrderReview = new Intent(OrderCustomer.this, OrderReview.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToOrderReview);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.order_customer_back) {
            goBack();
        }
        else if(id == R.id.order_customer_button) {
            button();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }
}