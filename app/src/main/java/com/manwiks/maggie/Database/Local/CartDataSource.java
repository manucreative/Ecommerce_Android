package com.manwiks.maggie.Database.Local;

import androidx.lifecycle.LiveData;

import com.manwiks.maggie.Database.DataSource.MCartDataSource;
import com.manwiks.maggie.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSource implements MCartDataSource {

    private CartDAO cartDAO;
    private  static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO)
    {
        if (instance == null)
            instance = new CartDataSource(cartDAO);
        return instance;
    }
    @Override
    public Flowable<List<Cart>> getCartItems() {
        return cartDAO.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return cartDAO.getCartItemById(cartItemId);
    }
    public LiveData<List<Cart>> getCartItemsLiveData(){
        return cartDAO.getCartItemsLiveData();
    }
    @Override
    public void deleteCartById(int cartItemId) {
        cartDAO.deleteCartById(cartItemId);
    }

    @Override
    public void emptyCart() {
         cartDAO.emptyCart();
    }
    public int getCountItems(){
       return cartDAO.getCountItems();
    }

    @Override
    public float sumPrice() {
        return cartDAO.sumPrice();
    }

    @Override
    public float sumVat() {return  cartDAO.sumVat();}

    public float sumPricePlusTax(){return cartDAO.sumPricePlusTax();}

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
         cartDAO.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart carts) {
           cartDAO.deleteCartItem(carts);
    }
}
