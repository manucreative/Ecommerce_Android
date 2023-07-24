package com.manwiks.maggie.Database.Local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.manwiks.maggie.Database.ModelDB.Cart;
import com.manwiks.maggie.Database.ModelDB.Favorite;

import io.reactivex.annotations.NonNull;

@Database(entities = {Cart.class, Favorite.class}, version = 3)
public abstract class MaggieRoomDatabase extends RoomDatabase {

    // Define your migration here
//    public static final Migration MIGRATION_2_3 = new Migration(2,3) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            // Add the new columns to the table
    //        database.execSQL("ALTER TABLE Cart ADD COLUMN topUpPrice TEXT");
//            database.execSQL("ALTER TABLE Cart ADD COLUMN pricePlusTax REAL NOT NULL default '0.0'");
//        }
//    };


    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO FavoriteDAO();
    private static MaggieRoomDatabase instance;

    public static MaggieRoomDatabase getInstance(Context context) {

        if(instance ==null)
            instance = Room.databaseBuilder(context,MaggieRoomDatabase.class,"Maggie_groceryDB")
                   // .addMigrations(MaggieRoomDatabase.MIGRATION_2_3)
                    .allowMainThreadQueries()
                    .build();
        return instance;


    }



}
