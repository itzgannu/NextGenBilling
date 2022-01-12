package mu.psi.nextgen.adapter.recycler;

import android.content.Context;
import android.content.Intent;
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

import mu.psi.nextgen.OrderProduct;
import mu.psi.nextgen.R;
import mu.psi.nextgen.StockUpdation;
import mu.psi.nextgen.models.inventory.Stock;

public class Stocks extends RecyclerView.Adapter<Stocks.MyViewHolder> {

    List<Stock> stockList;
    Context context;
    String toWhere;

    public Stocks(List<Stock> stockList, Context context, String toWhere) {
        this.stockList = stockList;
        this.context = context;
        this.toWhere = toWhere;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_stocks, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name, price, url;
        name = stockList.get(position).getName();
        price = String.valueOf(stockList.get(position).getPrice());
        url = stockList.get(position).getPic_url();

        holder.assignValues(name, price, url);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stock stock = stockList.get(holder.getAdapterPosition());
                if (toWhere.equalsIgnoreCase("update")) {
                    Intent goToUpdate = new Intent(context, StockUpdation.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    goToUpdate.putExtra("stock", stock);
                    context.startActivity(goToUpdate);
                } else if (toWhere.equalsIgnoreCase("order")) {
                    Intent goToOrder = new Intent(context, OrderProduct.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    goToOrder.putExtra("stock", stock);
                    context.startActivity(goToOrder);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView productIV;
        TextView productNameTV, productPriceTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_stocks);
            productIV = itemView.findViewById(R.id.card_stocks_image);
            productNameTV = itemView.findViewById(R.id.card_stocks_name);
            productPriceTV = itemView.findViewById(R.id.card_stocks_price);
        }

        public void assignValues(String name, String price, String url) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.basket)
                    .into(productIV);
            productNameTV.setText(name);
            productPriceTV.setText(price);
        }
    }
}