package com.manwiks.maggie.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.Gson;
import com.manwiks.maggie.Database.ModelDB.Cart;
import com.manwiks.maggie.FoodDetailsActivity;
import com.manwiks.maggie.Interface.ItemClickListener;
import com.manwiks.maggie.Models.BrandsModel;
import com.manwiks.maggie.Models.NewProductsModel;
import com.manwiks.maggie.Models.TopUpsModel;
import com.manwiks.maggie.R;
import com.manwiks.maggie.RetroTwo.Common;
import com.manwiks.maggie.Retrofit.GroceryShopAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {
    Context context;
    List<NewProductsModel> newProductsModelList;
    List<BrandsModel> brandsModelList ;
    BrandsAdapter brandsAdapter ;

    List<BrandsModel> brands;
    List<String> defaultBrands;
    private BrandsModel selectedBrands;

    public ProductsAdapter(Context context, List<NewProductsModel> newProductsModelList) {
        this.context = context;
        this.newProductsModelList = newProductsModelList;
       // this.regionsModelList = regionsModelList;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.veges_item_layout,parent,false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {

        holder.txtPrice.setText(newProductsModelList.get(holder.getAdapterPosition()).product_price);
        holder.txtName.setText(newProductsModelList.get(holder.getAdapterPosition()).product_name);
        holder.btn_add_to_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAddtoCartDialog(holder.getAdapterPosition());
            }
        });
        Glide.with(context).load(newProductsModelList.get(holder.getAdapterPosition()).link).placeholder(R.drawable.two).into(holder.product_img);
        holder.setItemClickListener(new ItemClickListener(){
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
                Common.currentProduct = newProductsModelList.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, FoodDetailsActivity.class);
                context.startActivity(intent);
            }
        });

    }


    ////<---------------------Add to cart dialog starting here-------------------->///////
    private void ShowAddtoCartDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.add_to_cart_layout, null);

// View
        ImageView img_cart_product = (ImageView) itemView.findViewById(R.id.img_cart_product);
        ElegantNumberButton txt_count = (ElegantNumberButton) itemView.findViewById(R.id.txt_count);
        TextView txt_product_dialog = (TextView) itemView.findViewById(R.id.txt_cart_product);
        TextView txt_product_price = (TextView) itemView.findViewById(R.id.cart_price);

        Button btnFindBrands = (Button) itemView.findViewById(R.id.btnFindBrands);


        CompositeDisposable compositeDisposable;
        GroceryShopAPI mService;
        mService = Common.getAPI();
        compositeDisposable = new CompositeDisposable();

        Glide.with(context).load(newProductsModelList.get(position).link).placeholder(R.drawable.two).into(img_cart_product);
        txt_product_dialog.setText(newProductsModelList.get(position).product_name);
        txt_product_price.setText(new StringBuilder("Ksh ").append(newProductsModelList.get(position).product_price));


        txt_count.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                // update the total price based on the new value of the ElegantNumberButton
                double newTotalPrice = Double.parseDouble(newProductsModelList.get(position).product_price) * newValue;
                txt_product_price.setText(new StringBuilder("Ksh ").append(newTotalPrice));
            }
        });

        List<TopUpsModel> topUpsModelList = new ArrayList<>();
        TopUpsAdapter adapter = new TopUpsAdapter(topUpsModelList);

        RecyclerView recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerTopUps);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mService.getTopUps(newProductsModelList.get(position).product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<TopUpsModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<TopUpsModel> topUpsModels) {
                        // Update your adapter with the received data
                        topUpsModelList.clear();
                        topUpsModelList.addAll(topUpsModels);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        final ArrayList<BrandsModel> selectedBrandss = null;
        btnFindBrands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<TopUpsModel> selectedTopUps = adapter.getSelectedProducts();
                List<String> selectedTopUpIds = new ArrayList<>();

                String topUpId = "";
                boolean isChecked = false;
                for (TopUpsModel topUpsModel : selectedTopUps) {
                    if(topUpsModel.isChecked()){
                        isChecked = true;
                        selectedTopUpIds.add(topUpsModel.getTop_up_id());
                    }
                }
                if(isChecked){
                    AlertDialog.Builder brandBuilder = new AlertDialog.Builder(context);
                    View spinnerView = LayoutInflater.from(context).inflate(R.layout.brand_list,null);
                    RecyclerView recycleBrands = (RecyclerView) spinnerView.findViewById(R.id.recyclerBrands);
                    TextView selected_brands = (TextView) itemView.findViewById(R.id.selected_brands);

                    brandsModelList = new ArrayList<>();
                    brandsAdapter = new BrandsAdapter(brandsModelList);

                    recycleBrands.setAdapter(brandsAdapter);
                    recycleBrands.setLayoutManager(new LinearLayoutManager(context));

                    compositeDisposable.add(mService.getTopUpBrands(selectedTopUpIds)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<List<BrandsModel>>() {
                                @Override
                                public void accept(List<BrandsModel> brandsModels) throws Exception {
                                    brandsModelList.clear();
                                    brandsModelList.addAll(brandsModels);
                                    brandsAdapter.notifyDataSetChanged();
                                }
                            }));



                    brandBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ArrayList<BrandsModel> selectedBrandss = brandsAdapter.getSelectedBrands();

                            String brandList = "";
                            boolean brandChecked = false;
                            for (BrandsModel myBrands : selectedBrandss){
                                String brandName = myBrands.getBrand_name();
                                brandChecked = myBrands.isChecked();
                                String brandAmount = String.valueOf(myBrands.getBrand_amount());

                                brandList += brandAmount + " " + brandName +"\n";

                            }
                            if(brandChecked) {
                                selected_brands.setVisibility(View.VISIBLE);
                                selected_brands.setText(new StringBuilder("Your brands on your top-ups \n").append(brandList));
                            }else {
                                selected_brands.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                    brandBuilder.setView(spinnerView);
                    AlertDialog dialog2 = brandBuilder.create();
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.show();
                }
            }
        });

        builder.setView(itemView);
        builder.setPositiveButton("ADD TO CART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<TopUpsModel> selectedProducts = adapter.getSelectedProducts();
              ArrayList<BrandsModel> selectedBrands = brandsAdapter.getSelectedBrands();

                ShowConfirmedDialog(position, txt_count.getNumber(), selectedProducts, selectedBrands);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        // builder.show();
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }




    ///////// ////////////////<-----Confirm dialog starts here----------->////////////////////////////////
    private void ShowConfirmedDialog(int position, String number, ArrayList<TopUpsModel> selectedProducts, ArrayList<BrandsModel> selectedBrands) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.confirm_add_to_cart_layout, null);

        //view
        ImageView img_product = (ImageView) itemView.findViewById(R.id.img_product);
        TextView txt_cart_product_name = (TextView) itemView.findViewById(R.id.txt_cart_product_name);
        TextView txt_product_price = (TextView) itemView.findViewById(R.id.txt_cart_product_price);
        TextView txt_top_ups = (TextView) itemView.findViewById(R.id.txt_top_ups);
        TextView txt_qty = (TextView) itemView.findViewById(R.id.txt_qty);
        TextView total_final_price = (TextView)itemView.findViewById(R.id.total_price);
        TextView item_price = (TextView)itemView.findViewById(R.id.item_price);
        TextView top_up_price = (TextView)itemView.findViewById(R.id.top_up_price);
        TextView tax_price = (TextView)itemView.findViewById(R.id.tax_price);
        TextView brand_list = (TextView) itemView.findViewById(R.id.brand_list);
        TextView top_up_list = (TextView) itemView.findViewById(R.id.top_up_list);


        //setting data
        Glide.with(context).load(newProductsModelList.get(position).link).placeholder(R.drawable.two).into(img_product);
        txt_cart_product_name.setText(newProductsModelList.get(position).product_name);
        txt_qty.setText(new StringBuilder("x ").append(number));

        ArrayList<String> topUpsList = new ArrayList<>();
        ArrayList<String> displayList = new ArrayList<>();
        double totalTopUpPrice = 0.0; // initialize total price to 0
        String topUpListText = "";

        // Convert TopUpsModel objects to JSON and add to ArrayList
        Gson gson = new Gson();
        for (TopUpsModel topUps : selectedProducts) {
            String topUpId = topUps.getTop_up_id();
            String topUpName = topUps.getTop_up_name();
            String topUpPrice = topUps.getTop_up_price();
            String topUpNumber = String.valueOf(topUps.getBtnNumber());


            // Convert TopUpsModel object to JSON and add to ArrayList
            String topUpsJson = gson.toJson(new TopUpsModel(topUpId, topUpName, topUpPrice, true, topUpNumber));
            topUpsList.add(topUpsJson);

            double totalPrice = Double.parseDouble(topUpPrice) * Double.parseDouble(topUpNumber);

           // String amount = "";
            String names ="";
            for (BrandsModel myBrands : selectedBrands) {
               String brandName = myBrands.getBrand_name();
                boolean brandChecked = myBrands.isChecked();
                String brandAmount = String.valueOf(myBrands.getBrand_amount());
             //   amount += brandAmount;
                names += brandAmount + " " + brandName +"/" ;
            }

                topUpListText += topUpNumber + " " + topUpName + " : Ksh " + String.format("%.2f", totalPrice) + " having: " + names  +"\n";
                totalTopUpPrice += totalPrice;


            String displayTopUps = topUpNumber + " " + topUpName + " For Ksh " + totalPrice +"\n";
            displayList.add(displayTopUps);


        }
        top_up_list.setText(topUpListText);
        txt_top_ups.setText(new StringBuilder("Your Food Top Ups"));

//        String brandList = "";
//
//        brand_list.setText(new StringBuilder("Your brands \n").append(brandList));


        Double price = (Double.parseDouble(newProductsModelList.get(position).product_price) * Double.parseDouble(number));

        txt_product_price.setText(new StringBuilder("Ksh ").append(price));
        item_price.setText(new StringBuilder("Item price: Ksh. ").append(price));
        top_up_price.setText(new StringBuilder("Top-up price: Ksh. ").append(totalTopUpPrice));
        Double fullPrice = price + totalTopUpPrice;
         //8% tax calculation
        Double tax = fullPrice * Double.parseDouble("8") / Double.parseDouble("100");
        Double TotalPlusTax = fullPrice + tax;

        tax_price.setText(new StringBuilder("VAT: Ksh ").append(tax));
        total_final_price.setText(new StringBuilder("Total: Ksh ").append(TotalPlusTax));

        String displayTopUps = TextUtils.join(" ", displayList);
        String topUpsString = String.valueOf(topUpsList);
        //final double TotalPlusTax = price;
        String finalTopUpListText = topUpListText;
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //add to SQlite
                //creation of new cart item
                try {
                    Cart cartItem = new Cart();
                    cartItem.name = txt_cart_product_name.getText().toString();
                    cartItem.amount = Integer.parseInt(number);
                    cartItem.price = fullPrice;
                    cartItem.displayTopUps = displayTopUps;
                    cartItem.productPrice = newProductsModelList.get(position).product_price;
                    cartItem.pricePlusTax = TotalPlusTax;
                    cartItem.vat = tax;
                    cartItem.topUps = topUpsString ;
                    cartItem.link = newProductsModelList.get(position).link;

                    Common.cartRepository.insertToCart(cartItem);
                    Log.d("Maggie_Debug", new Gson().toJson(cartItem));
                    Toast.makeText(context, "Save Item to cart succeeded", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        builder.setView(itemView);
       // builder.show();
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    @Override
    public int getItemCount() {
        return newProductsModelList.size();
    }
}

