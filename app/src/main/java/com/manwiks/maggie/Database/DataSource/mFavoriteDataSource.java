package com.manwiks.maggie.Database.DataSource;

import com.manwiks.maggie.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface mFavoriteDataSource {

    Flowable<List<Favorite>> getFavItems();

    int isFavorite(int itemId);

    void insertFav(Favorite...favorites);

    void delete(Favorite favorite);
}
