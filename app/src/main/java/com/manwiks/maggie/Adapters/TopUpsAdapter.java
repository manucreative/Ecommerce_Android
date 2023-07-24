package com.manwiks.maggie.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.manwiks.maggie.Models.TopUpsModel;
import com.manwiks.maggie.R;

import java.util.ArrayList;
import java.util.List;

public class TopUpsAdapter extends RecyclerView.Adapter<TopUpsAdapter.TopUpsViewHolder> {
        private List<TopUpsModel> topUpsModelList;
        private List<TopUpsModel> checkedItems;

    public TopUpsAdapter(List<TopUpsModel> topUpsModelList) {
        this.topUpsModelList = topUpsModelList;
        checkedItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public TopUpsAdapter.TopUpsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_checkbox_item, parent, false);
        return new TopUpsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopUpsAdapter.TopUpsViewHolder holder, int position) {

        holder.top_up_title.setText(topUpsModelList.get(holder.getAdapterPosition()).getTop_up_name());
        holder.top_up_price.setText(new StringBuilder("+Ksh ").append(topUpsModelList.get(holder.getAdapterPosition()).getTop_up_price()));
        holder.numberButton.setNumber(String.valueOf(topUpsModelList.get(position).getBtnNumber()));

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(topUpsModelList.get(holder.getAdapterPosition()).isChecked());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                topUpsModelList.get(holder.getAdapterPosition()).setChecked(isChecked);
                if(isChecked){
                    checkedItems.add(topUpsModelList.get(holder.getAdapterPosition()));
                }else {
                    checkedItems.remove(topUpsModelList.get(holder.getAdapterPosition()));
                }
            }
        });
        int numBtn = Integer.parseInt(holder.numberButton.getNumber());
        topUpsModelList.get(holder.getAdapterPosition()).setBtnNumber(numBtn);
        holder.numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                topUpsModelList.get(holder.getAdapterPosition()).setBtnNumber(newValue);
                double price = Double.parseDouble(topUpsModelList.get(holder.getAdapterPosition()).getTop_up_price());
                 double newPrice = price * newValue;
                holder.top_up_price.setText(new StringBuilder("+Ksh ").append(newPrice));
            }
        });
    }

    @Override
    public int getItemCount() {
        return topUpsModelList.size();
    }

        public List<TopUpsModel> getCheckedItems(){
         return checkedItems;
        }

    public ArrayList<TopUpsModel> getSelectedProducts() {
        ArrayList<TopUpsModel> selectedProducts = new ArrayList<>();

        for (TopUpsModel item : topUpsModelList) {
            if (item.isChecked()) {
                selectedProducts.add(item);
            }
        }

        return selectedProducts;
    }

    public class TopUpsViewHolder extends RecyclerView.ViewHolder {

        TextView top_up_title, top_up_price;
        CheckBox checkBox;
        ElegantNumberButton numberButton;
        public TopUpsViewHolder(@NonNull View itemView) {
            super(itemView);
            top_up_title = (TextView)itemView.findViewById(R.id.top_up_title);
            top_up_price = (TextView)itemView.findViewById(R.id.top_up_price);
            numberButton = (ElegantNumberButton) itemView.findViewById(R.id.numButton);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkTopUp);
        }


    }
}
