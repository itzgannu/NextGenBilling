package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mu.psi.nextgen.adapter.recycler.Items;
import mu.psi.nextgen.databinding.ActivityOrderReviewBinding;
import mu.psi.nextgen.models.cart.Item;
import mu.psi.nextgen.viewModel.CartVM;

public class OrderReview extends AppCompatActivity implements View.OnClickListener {

    ActivityOrderReviewBinding binding;

    Context context;

    FirebaseAuth auth;

    CartVM viewModel;
    String email;

    double total = 0;

    RecyclerView recyclerView;
    Items recyclerAdapter;
    GridLayoutManager gridLayoutManager;

    List<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityOrderReviewBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.context = this;
        this.auth = FirebaseAuth.getInstance();
        this.email = Objects.requireNonNull(this.auth.getCurrentUser()).getEmail();

        String name = auth.getCurrentUser().getDisplayName();

        this.viewModel = CartVM.getInstance(getApplication());
        this.viewModel.readItems(name);
        this.viewModel.itemMLD.observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                startRecycler(items);
            }
        });

        this.binding.orderReviewBack.setOnClickListener(this);
        this.binding.orderReviewGoToCheckoutButton.setOnClickListener(this);

    }

    private void startRecycler(List<Item> items) {
        if (items != null) {
            total = 0;
            for (int i = 0; i < items.size(); i++) {
                total = total + items.get(i).getTotal();
            }
            String text;
            total = Math.round(total*100.0)/100.0;
            if (total == 0) {
                text = "No items are added in cart";
                binding.orderReviewGoToCheckoutButton.setEnabled(false);
                binding.orderReviewTotal.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                text = "CAD " + total;
                binding.orderReviewGoToCheckoutButton.setEnabled(true);
                binding.orderReviewTotal.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
            binding.orderReviewTotal.setText(text);
            itemList = items;
        }
        this.recyclerView = this.binding.orderReviewRecycler;
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerAdapter = new Items(items, this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void goToCheckout() {
        Intent goToOrderCustomer = new Intent(OrderReview.this, OrderCustomer.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        goToOrderCustomer.putExtra("items", (Serializable) this.itemList);
        goToOrderCustomer.putExtra("total", total);
        startActivity(goToOrderCustomer);
        finish();
    }

    private void goBack() {
        Intent goToOrderAisle = new Intent(OrderReview.this, OrderAisle.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToOrderAisle);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.order_review_back) {
            goBack();
        }
        else if(id == R.id.order_review_go_to_checkout_button) {
            goToCheckout();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }
}