package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import mu.psi.nextgen.databinding.ActivityOrderProductBinding;
import mu.psi.nextgen.models.cart.Item;
import mu.psi.nextgen.models.inventory.Stock;
import mu.psi.nextgen.viewModel.CartVM;

public class OrderProduct extends AppCompatActivity implements View.OnClickListener {

    ActivityOrderProductBinding binding;

    FirebaseAuth auth;
    CartVM viewModel;

    String displayName;
    String itemName;

    Stock newStock;
    Item newItem;

    int count; double total; boolean initialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityOrderProductBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        assignValues();

        initialized = true;

        this.viewModel = CartVM.getInstance(getApplication());
        this.viewModel.readItem(displayName, itemName);
        this.viewModel.singleItemMLD.observe(this, new Observer<Item>() {
            @Override
            public void onChanged(Item item) {
                if(item != null) {
                    count = item.getQuantity();
                    newItem.setTotal(item.getTotal());
                    newItem.setQuantity(count);
                    if(count >= 1) {
                        if(initialized) {
                            initialized = false;
                            binding.orderProductStepper.setVisibility(View.VISIBLE);
                            binding.orderProductStepper.setEnabled(true);
                            binding.orderProductAddToCartButton.setEnabled(false);
                            binding.orderProductCount.setText(String.valueOf(count));
                        }
                    }
                }
            }
        });

        this.binding.orderProductClose.setOnClickListener(this);
        this.binding.orderProductAddToCartButton.setOnClickListener(this);
        this.binding.orderProductAdd.setOnClickListener(this);
        this.binding.orderProductMinus.setOnClickListener(this);
    }

    private void assignValues() {
        count = 1;
        total = 0;

        auth = FirebaseAuth.getInstance();
        displayName = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();

        Intent i = getIntent();
        newStock = (Stock) i.getSerializableExtra("stock");
        newItem = newStock.toItem();
        itemName = newItem.getName();

        String sPrice = "CAD - " +newStock.getPrice();

        Glide.with(this)
                .load(newStock.getPic_url())
                .placeholder(R.drawable.basket)
                .into(this.binding.orderProductPic);
        this.binding.orderProductTitle.setText(newStock.getName());
        this.binding.orderProductPrice.setText(sPrice);
        this.binding.orderProductPack.setText(newStock.getPack());
        this.binding.orderProductDescription.setText(newStock.getDescription());
    }

    private void add() {
        count = count + 1;
        double price = newItem.getPrice();
        total = price * count;
        newItem.setQuantity(count);
        newItem.setTotal(total);
        this.binding.orderProductCount.setText(String.valueOf(count));
        this.viewModel.updateCart(displayName, newItem);
    }

    private void minus() {
        if(count == 1) {
            this.binding.orderProductStepper.setVisibility(View.INVISIBLE);
            this.binding.orderProductStepper.setEnabled(false);
            this.binding.orderProductAddToCartButton.setEnabled(true);
            String name = newItem.getName();
            this.viewModel.deleteExistingItem(displayName, name);
        }
        else {
            count = count - 1;
            double price = newItem.getPrice();
            total = price * count;
            newItem.setQuantity(count);
            newItem.setTotal(total);
            this.binding.orderProductCount.setText(String.valueOf(count));
            this.viewModel.updateCart(displayName, newItem);
        }
    }

    private void cart() {
        count = 1;
        double price = newItem.getPrice();
        total = price * count;
        newItem.setQuantity(count);
        newItem.setTotal(total);
        this.binding.orderProductStepper.setVisibility(View.VISIBLE);
        this.binding.orderProductStepper.setEnabled(true);
        this.binding.orderProductAddToCartButton.setEnabled(false);
        this.binding.orderProductCount.setText(String.valueOf(count));
        this.viewModel.updateCart(displayName, newItem);
    }

    private void goBack() {
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.order_product_close) {
            goBack();
        }
        else if(id == R.id.order_product_add) {
            add();
        }
        else if(id == R.id.order_product_minus) {
            minus();
        }
        else if(id == R.id.order_product_add_to_cart_button) {
            cart();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }
}