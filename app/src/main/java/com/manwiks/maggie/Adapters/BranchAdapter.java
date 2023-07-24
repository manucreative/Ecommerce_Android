package com.manwiks.maggie.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manwiks.maggie.Models.BranchModel;
import com.manwiks.maggie.R;

import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    private List<BranchModel> branchModelList;
    private Context context;

    public BranchAdapter(List<BranchModel> branchModelList, Context context) {
        this.branchModelList = branchModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_goout_branches,viewGroup,false);
        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {

        BranchModel branchModel = branchModelList.get(position);

        Glide.with(context).load(branchModel.getBranch_img()).placeholder(R.drawable.two).into(holder.branch_img);
        holder.branch_name.setText(branchModel.getBranch_name());
        holder.branch_email.setText(branchModel.getBranch_email());
        holder.branch_phone.setText(branchModel.getBranch_phone());
        holder.branch_address.setText(branchModel.getBranch_address());
        holder.town.setText(branchModel.getTown());
    }

    @Override
    public int getItemCount() {
           return branchModelList.size();
    }

    public class BranchViewHolder extends RecyclerView.ViewHolder {

        private ImageView branch_img;
        private TextView branch_name, branch_email, branch_phone,branch_address,town;
        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);

            branch_img = (ImageView)itemView.findViewById(R.id.branch_image);
            branch_name = (TextView)itemView.findViewById(R.id.shopName);
            branch_email = (TextView)itemView.findViewById(R.id.mails);
            branch_phone = (TextView)itemView.findViewById(R.id.phones);
            branch_address = (TextView)itemView.findViewById(R.id.address);
            town = (TextView)itemView.findViewById(R.id.city);
        }
    }
}