package mu.psi.nextgen.adapter.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import mu.psi.nextgen.R;
import mu.psi.nextgen.models.cart.Item;

public class Items extends RecyclerView.Adapter<Items.MyViewHolder> {

    List<Item> itemList;
    Context context;

    public Items(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items, parent, false);
        return new Items.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String quantity, url, name, pack, price;

        quantity = String.valueOf(itemList.get(position).getQuantity());
        url = itemList.get(position).getPic_url();
        name = itemList.get(position).getName();
        pack = itemList.get(position).getPack();
        price = String.valueOf(itemList.get(position).getTotal());

        holder.assignValues(quantity, url, name, pack, price);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView productIV;
        TextView quantityTV, nameTV, packTV, priceTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_items);
            productIV = itemView.findViewById(R.id.card_items_image);
            quantityTV = itemView.findViewById(R.id.card_items_quantity);
            nameTV = itemView.findViewById(R.id.card_items_name);
            packTV = itemView.findViewById(R.id.card_items_pack);
            priceTV = itemView.findViewById(R.id.card_items_price);
        }

        public void assignValues(String quantity, String url, String name, String pack, String price) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.basket)
                    .into(productIV);

            quantityTV.setText(quantity);
            nameTV.setText(name);
            packTV.setText(pack);
            priceTV.setText(price);
        }
    }
}
