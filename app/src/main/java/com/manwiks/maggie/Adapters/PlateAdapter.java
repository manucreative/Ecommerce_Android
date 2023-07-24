package com.manwiks.maggie.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manwiks.maggie.Models.CategoryModel;
import com.manwiks.maggie.R;

import java.util.List;

public class PlateAdapter extends RecyclerView.Adapter<PlateAdapter.PlateViewHolder> {

    private List<CategoryModel> categoryModelList;
    private Context context;

    public PlateAdapter(List<CategoryModel> categoryModelList, Context context) {
        this.categoryModelList = categoryModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_plates, viewGroup, false);
        return new PlateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlateViewHolder holder, int position) {

       CategoryModel categoryModel = categoryModelList.get(position);

        Glide.with(context).load(categoryModel.getCat_image()).placeholder(R.drawable.two).into(holder.plateImg);
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class PlateViewHolder extends RecyclerView.ViewHolder {

        private ImageView plateImg;
        public PlateViewHolder(@NonNull View itemView) {
            super(itemView);

            plateImg = (ImageView)itemView.findViewById(R.id.imageView2);
        }
    }
}
