package com.manwiks.maggie.Database.DataSource;

import androidx.lifecycle.LiveData;

import com.manwiks.maggie.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface MCartDataSource {
    Flowable<List<Cart>> getCartItems();
    Flowable<List<Cart>> getCartItemById(int cartItemId);
    LiveData<List<Cart>> getCartItemsLiveData();

    void deleteCartById(int cartItemId);
    int getCountItems();

    void emptyCart();
    float sumPrice();

    float sumVat();

    float sumPricePlusTax();

    void insertToCart(Cart...carts);
    void updateCart(Cart...carts);
    void deleteCartItem(Cart carts);
}
