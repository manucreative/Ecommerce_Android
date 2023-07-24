package com.manwiks.maggie.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.manwiks.maggie.Models.BrandsModel;
import com.manwiks.maggie.Models.TopUpsModel;
import com.manwiks.maggie.R;

import java.util.ArrayList;
import java.util.List;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.BrandsViewHolder>{

    private List<BrandsModel> brandsModelList;
    private List<BrandsModel> checkedBrands;

    public BrandsAdapter(List<BrandsModel> brandsModelList) {
        this.brandsModelList = brandsModelList;
        checkedBrands = new ArrayList<>();
    }

    @NonNull
    @Override
    public BrandsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brands_layout,parent,false);
        return new BrandsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandsViewHolder holder, int position) {
        holder.brandName.setText(brandsModelList.get(holder.getAdapterPosition()).getBrand_name());
        holder.btnBrands.setNumber(String.valueOf(brandsModelList.get(holder.getAdapterPosition()).getBrand_amount()));

        //setting up the checkbox
        holder.brandCheckbox.setOnCheckedChangeListener(null);
        holder.brandCheckbox.setChecked(brandsModelList.get(holder.getAdapterPosition()).isChecked());

        holder.brandCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                brandsModelList.get(holder.getAdapterPosition()).setChecked(isChecked);
                if(isChecked){
                    checkedBrands.add(brandsModelList.get(holder.getAdapterPosition()));
                } else{
                    checkedBrands.remove(brandsModelList.get(holder.getAdapterPosition()));
                }

            }
        });

        int btnNumber = Integer.parseInt(holder.btnBrands.getNumber());
        brandsModelList.get(holder.getAdapterPosition()).setBrand_amount(btnNumber);
        holder.btnBrands.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                brandsModelList.get(holder.getAdapterPosition()).setBrand_amount(newValue);
            }
        });

    }

    public void setBrandList(List<BrandsModel> brandList) {
        this.brandsModelList = brandList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return brandsModelList.size();
    }
    public List<BrandsModel> getSelected(){
        return checkedBrands;
    }
    public ArrayList<BrandsModel> getSelectedBrands() {
        ArrayList<BrandsModel> selectedBrands = new ArrayList<>();

        for (BrandsModel item : brandsModelList) {
            if (item.isChecked()) {
                selectedBrands.add(item);
            }
        }

        return selectedBrands;
    }

    public class BrandsViewHolder extends RecyclerView.ViewHolder {

        TextView brandName;
        CheckBox brandCheckbox;
        ElegantNumberButton btnBrands;

        public BrandsViewHolder(@NonNull View itemView) {
            super(itemView);
            brandName = itemView.findViewById(R.id.txt_brand_name);
            brandCheckbox = itemView.findViewById(R.id.brandCheckbox);
            btnBrands = itemView.findViewById(R.id.btnBrands);
        }

    }
}
