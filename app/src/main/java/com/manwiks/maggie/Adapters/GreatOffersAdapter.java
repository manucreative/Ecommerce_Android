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
import com.manwiks.maggie.Models.GreatOffersModel;
import com.manwiks.maggie.R;

import java.util.List;

public class GreatOffersAdapter extends RecyclerView.Adapter<GreatOffersAdapter.GreatOfferViewHolder> {

    List<GreatOffersModel> greatOffersModelList;
     Context context;

    public GreatOffersAdapter(List<GreatOffersModel> greatOffersModelList, Context context) {
        this.greatOffersModelList = greatOffersModelList;
        this.context = context;

    }

    @NonNull
    @Override
    public GreatOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_horizontal_great_offers,parent,false);
        return new GreatOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GreatOfferViewHolder holder, int position) {

        GreatOffersModel greatOffersModel = greatOffersModelList.get(position);

        Glide.with(context).load(greatOffersModel.getShop_img()).placeholder(R.drawable.two).into(holder.shop_img);
        holder.shop_name.setText(greatOffersModel.getShop_name());
        holder.time.setText(greatOffersModel.getTime());
        holder.discount.setText(greatOffersModel.getDiscount());
        holder.rating.setText(greatOffersModel.getRating());
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class GreatOfferViewHolder extends RecyclerView.ViewHolder {
        private ImageView shop_img;
        private TextView shop_name, time,discount,rating;

        public GreatOfferViewHolder(@NonNull View itemView) {
            super(itemView);

            shop_img = (ImageView)itemView.findViewById(R.id.imageView7);
            shop_name = (TextView)itemView.findViewById(R.id.textView10);
            time= (TextView)itemView.findViewById(R.id.textView11);
            discount= (TextView)itemView.findViewById(R.id.textView12);
            rating = (TextView)itemView.findViewById(R.id.textView13);
        }
    }
}
