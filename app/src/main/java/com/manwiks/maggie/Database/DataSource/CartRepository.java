package com.manwiks.maggie.Database.DataSource;

import androidx.lifecycle.LiveData;

import com.manwiks.maggie.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements MCartDataSource{

    private MCartDataSource mCartDataSource;

    public CartRepository(MCartDataSource mCartDataSource) {
        this.mCartDataSource = mCartDataSource;
    }

    private static CartRepository instance;
    public static CartRepository getInstance(MCartDataSource mCartDataSource){
        if(instance == null)
            instance = new CartRepository(mCartDataSource);
        return instance;
    }
    @Override
    public Flowable<List<Cart>> getCartItems() {
        return mCartDataSource.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return mCartDataSource.getCartItemById(cartItemId);
    }

    @Override
    public LiveData<List<Cart>> getCartItemsLiveData() {
        return mCartDataSource.getCartItemsLiveData();
    }

    @Override
    public void emptyCart() {
       mCartDataSource.emptyCart();
    }
    @Override
    public void deleteCartById(int cartItemId){
        mCartDataSource.deleteCartById(cartItemId);
    }

    @Override
    public int getCountItems() {
        return mCartDataSource.getCountItems();

    }

    @Override
    public float sumPrice() {
        return mCartDataSource.sumPrice();
    }

    @Override
    public  float sumVat(){
        return mCartDataSource.sumVat();
    }
    @Override
    public  float sumPricePlusTax(){
        return mCartDataSource.sumPricePlusTax();
    }

    @Override
    public void insertToCart(Cart... carts) {
       mCartDataSource.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
       mCartDataSource.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart carts) {
       mCartDataSource.deleteCartItem(carts);
    }
}
