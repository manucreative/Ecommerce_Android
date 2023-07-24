package com.manwiks.maggie.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manwiks.maggie.Interface.ItemClickListener;
import com.manwiks.maggie.R;

public class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView product_img;
    TextView txtName, txtPrice;

    ItemClickListener itemClickListener;

    ImageView btn_add_to_cat, btn_favorite;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductsViewHolder(@NonNull View itemView) {
        super(itemView);
        product_img = (ImageView)itemView.findViewById(R.id.product_img);
        txtName = (TextView)itemView.findViewById(R.id.productName);
        txtPrice = (TextView)itemView.findViewById(R.id.productPrice);
        btn_add_to_cat = (ImageView) itemView.findViewById(R.id.productCarts);
        btn_favorite = (ImageView)itemView.findViewById(R.id.productFavorite);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v);
    }
}
