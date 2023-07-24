package com.manwiks.maggie.Database.Local;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.manwiks.maggie.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {
 @Query("SELECT * FROM Cart")
Flowable<List<Cart>> getCartItems();

    @Query("SELECT * FROM Cart WHERE id =:cartItemId")
    Flowable<List<Cart>> getCartItemById(int cartItemId);

    @Query("SELECT SUM(Price) from Cart")
    float sumPrice();

    @Query("SELECT SUM(Vat) from Cart")
    float sumVat();
    @Query("SELECT SUM(PricePlusTax) from Cart")
    float sumPricePlusTax();

    @Query("DELETE FROM Cart WHERE id =:cartItemId")
    void deleteCartById(int cartItemId);

    @Query("DELETE FROM Cart")
    void emptyCart();


    @Insert
    void insertToCart(Cart...carts);

    @Update
    void updateCart(Cart...carts);

    @Delete
    void deleteCartItem(Cart carts);

}
