package com.manwiks.maggie.Adapters;

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
import com.manwiks.maggie.Interface.ItemClickListener;
import com.manwiks.maggie.Models.CategoryModel;
import com.manwiks.maggie.R;
import com.manwiks.maggie.RetroTwo.Common;
import com.manwiks.maggie.ProductsActivity;

import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.PlateViewHolder> {

    private List<CategoryModel> categoryModelList;
    private Context context;

    public CatAdapter(List<CategoryModel> categoryModelList, Context context) {
        this.categoryModelList = categoryModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_category,viewGroup,false);
        return new PlateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlateViewHolder holder, int position) {

        CategoryModel categoryModel = categoryModelList.get(holder.getAdapterPosition());

        Glide.with(context).load(categoryModel.getCat_image()).placeholder(R.drawable.my_logo).into(holder.cat_image);
        holder.cat_title.setText(categoryModel.getCat_title());


        holder.setItemClickListener(new ItemClickListener(){
            @Override
            public void onClick(View view) {

                Common.currentCategory = categoryModelList.get(holder.getAdapterPosition());
                //starting new activity
//                context.startActivity(new Intent(context, VegetableActivity.class));
                Intent intent = new Intent(context, ProductsActivity.class);
                intent.putExtra("cat_id", categoryModel.getCat_id());
                intent.putExtra("cat_image", categoryModel.getCat_image());
                intent.putExtra("cat_title", categoryModel.getCat_title());
                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }


    public class PlateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView cat_image;
        private TextView cat_title;

        ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public PlateViewHolder(@NonNull View itemView) {
            super(itemView);

           cat_image = (ImageView)itemView.findViewById(R.id.imageView4);
           cat_title = (TextView)itemView.findViewById(R.id.textView3);

           itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v);
        }
    }
}