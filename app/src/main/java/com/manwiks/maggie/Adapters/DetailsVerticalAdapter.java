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
import com.manwiks.maggie.Models.DetailsVerticalModel;
import com.manwiks.maggie.R;

import java.util.List;

public class DetailsVerticalAdapter extends RecyclerView.Adapter<DetailsVerticalAdapter.PlateViewHolder> {

    private List<DetailsVerticalModel> detailsVerticalModelList;
    private Context context;

    public DetailsVerticalAdapter(List<DetailsVerticalModel> detailsVerticalModelList, Context context) {
        this.detailsVerticalModelList = detailsVerticalModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vertical_slider,viewGroup,false);
        return new PlateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlateViewHolder holder, int position) {

        DetailsVerticalModel detailsVerticalModel = detailsVerticalModelList.get(position);

        Glide.with(context).load(detailsVerticalModel.getPro_img()).placeholder(R.drawable.two).into(holder.pro_img);
        holder.pro_title.setText(detailsVerticalModel.getSimple_title());
        holder.pro_description.setText(detailsVerticalModel.getSimple_description());
        holder.pro_quantity.setText(detailsVerticalModel.getSimple_quantity());
        holder.pro_coupon.setText(detailsVerticalModel.getSimple_coupon());
        holder.pro_status.setText(detailsVerticalModel.getSimple_status());
        holder.pro_rating.setText(detailsVerticalModel.getSimple_rating());
    }

    @Override
    public int getItemCount() {
        return detailsVerticalModelList.size();
    }

    public class PlateViewHolder extends RecyclerView.ViewHolder {

        private ImageView pro_img;
        private TextView pro_title, pro_description,pro_coupon,pro_quantity,pro_status,pro_rating;
        public PlateViewHolder(@NonNull View itemView) {
            super(itemView);

            pro_img = (ImageView)itemView.findViewById(R.id.imageView5);
            pro_title = (TextView)itemView.findViewById(R.id.textView2);
            pro_description = (TextView)itemView.findViewById(R.id.textView4);
            pro_quantity = (TextView)itemView.findViewById(R.id.textView5);
            pro_coupon = (TextView)itemView.findViewById(R.id.textView6);
            pro_status = (TextView)itemView.findViewById(R.id.textView7);
            pro_rating = (TextView)itemView.findViewById(R.id.textView8);
        }
    }
}
