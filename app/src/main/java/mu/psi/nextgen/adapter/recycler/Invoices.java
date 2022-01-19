package mu.psi.nextgen.adapter.recycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import mu.psi.nextgen.R;
import mu.psi.nextgen.ViewBill;
import mu.psi.nextgen.models.bills.Invoice;

public class Invoices extends RecyclerView.Adapter<Invoices.MyViewHolder> {

    List<Invoice> invoiceList;
    Context context;

    public Invoices(List<Invoice> invoiceList, Context context) {
        this.invoiceList = invoiceList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_invoices, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String order_number = invoiceList.get(position).getOrder_number();

        holder.assignValues(order_number);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invoice invoice = invoiceList.get(holder.getAdapterPosition());
                Intent goToBills = new Intent(context, ViewBill.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                goToBills.putExtra("invoice", invoice);
                context.startActivity(goToBills);
            }
        });
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView orderNumberTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_invoices);
            orderNumberTV = itemView.findViewById(R.id.card_invoices_order_id);
        }

        public void assignValues(String order_number) {
            orderNumberTV.setText(order_number);
        }
    }

    public void filteredList(List<Invoice> searchList) {
        this.invoiceList = searchList;
        notifyDataSetChanged();
    }
}
