package com.manwiks.maggie.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.Gson;
import com.manwiks.maggie.Database.ModelDB.Cart;
import com.manwiks.maggie.R;
import com.manwiks.maggie.RetroTwo.Common;

import java.util.List;

public class ICartAdapter extends RecyclerView.Adapter<ICartAdapter.ICartViewHolder>
{
    Context context;
    List<Cart> cartList;
    Gson gson;

    public ICartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
        gson = new Gson();
    }

    @NonNull
    @Override
    public ICartAdapter.ICartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false);
        return new ICartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ICartAdapter.ICartViewHolder holder, int position) {

        Glide.with(context)
                .load(cartList.get(position).link)
                .into(holder.img_product);
        holder.txt_amount.setNumber(String.valueOf(cartList.get(position).amount));
        holder.full_price.setText(new StringBuilder("Total: Ksh ").append(cartList.get(position).pricePlusTax));
        String productNamePrice = "1 " + cartList.get(holder.getAdapterPosition()).name + " = Ksh " + cartList.get(holder.getAdapterPosition()).productPrice +" x " +cartList.get(position).amount;
        holder.txt_product_name.setText(productNamePrice);
        holder.item_total_price.setText(new StringBuilder("Item + Top-ups: Ksh. ").append(cartList.get(position).price));



        holder.txt_top_up.setText(new StringBuilder("top-ups:\n").append(cartList.get(holder.getAdapterPosition()).displayTopUps));
        holder.txt_vat.setText(new StringBuilder("VAT: Ksh. ").append(cartList.get(holder.getAdapterPosition()).vat));

        holder.btn_delete_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int cartId = cartList.get(holder.getAdapterPosition()).id;
                Common.cartRepository.deleteCartById(cartId);
            }
        });
        //auto save items when user change amount
        holder.txt_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart = cartList.get(holder.getAdapterPosition());
               cart.amount = newValue;

                Common.cartRepository.updateCart(cart);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ICartViewHolder extends RecyclerView.ViewHolder {

        ImageView img_product;

        TextView txt_product_name, txt_top_up, full_price, txt_vat,item_total_price;
        ElegantNumberButton txt_amount;
        Button btn_delete_cart;

        public ICartViewHolder(@NonNull View itemView) {
            super(itemView);

           img_product = (ImageView)itemView.findViewById(R.id.prod_img);
           txt_product_name = (TextView)itemView.findViewById(R.id.txt_product_name);
            txt_top_up = (TextView)itemView.findViewById(R.id.txt_top_up);
            full_price = (TextView)itemView.findViewById(R.id.txt_full_price);
            txt_amount = (ElegantNumberButton) itemView.findViewById(R.id.txt_amount);
            txt_vat = (TextView) itemView.findViewById(R.id.txt_vat);
            btn_delete_cart = (Button) itemView.findViewById(R.id.btn_delete_cart);
            item_total_price = (TextView) itemView.findViewById(R.id.item_total_price);
        }
    }
}
