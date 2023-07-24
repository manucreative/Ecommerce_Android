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
import com.manwiks.maggie.Models.ProductsModel;
import com.manwiks.maggie.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.PlateViewHolder> {

    private List<ProductsModel> productsModelList;
    private Context context;

    public ProductAdapter(List<ProductsModel> productsModelList, Context context) {
        this.productsModelList = productsModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_goout_pruducts,viewGroup,false);
        return new PlateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlateViewHolder holder, int position) {

        ProductsModel branchModel = productsModelList.get(position);

        Glide.with(context).load(branchModel.getProduct_img()).placeholder(R.drawable.two).into(holder.product_img);
        holder.product_name.setText(branchModel.getProduct_name());
        holder.product_price.setText(branchModel.getProduct_price());
        holder.product_weigh.setText(branchModel.getProduct_weigh());
        holder.cat_add.setText(branchModel.getCat_add());
    }

    @Override
    public int getItemCount() {
        return productsModelList.size();
    }

    public class PlateViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_img;
        private TextView product_name, product_price,product_weigh,cat_add;
        public PlateViewHolder(@NonNull View itemView) {
            super(itemView);

            product_img = (ImageView)itemView.findViewById(R.id.produ_img);
            product_name = (TextView)itemView.findViewById(R.id.produ_name);
            product_price = (TextView)itemView.findViewById(R.id.price);
            product_weigh = (TextView)itemView.findViewById(R.id.weigh);
           cat_add = (TextView)itemView.findViewById(R.id.add_cat);
        }
    }
}