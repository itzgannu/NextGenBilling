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

import mu.psi.nextgen.HomeBills;
import mu.psi.nextgen.HomeStores;
import mu.psi.nextgen.R;
import mu.psi.nextgen.models.company.Branch;

public class Branches extends RecyclerView.Adapter<Branches.MyViewHolder>{

    List<Branch> branchList;
    Context context;

    public Branches(List<Branch> branchList, Context context) {
        this.branchList = branchList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_stores,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String branch_name, branch_location;
        branch_name = branchList.get(position).getBranch_name();
        branch_location = branchList.get(position).getBranch_location();

        holder.assignValues(branch_name, branch_location);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToBills = new Intent(context, HomeBills.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                goToBills.putExtra("branch_name", branch_name);
                context.startActivity(goToBills);
            }
        });
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView branchNameTV, branchLocationTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            branchNameTV = itemView.findViewById(R.id.card_stores_branch_name);
            branchLocationTV = itemView.findViewById(R.id.card_stores_branch_location);
        }

        public void assignValues(String branch_name, String branch_location) {
            branchNameTV.setText(branch_name);
            branchLocationTV.setText(branch_location);
        }
    }
}
