package com.manwiks.maggie.Database.ModelDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Cart")
public class Cart {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "amount")
    public int amount;

    @ColumnInfo(name = "price")
    public double price;

    @ColumnInfo(name = "topUps")
    public String topUps;

    @ColumnInfo(name = "displayToPUps")
    public  String displayTopUps;

    @ColumnInfo(name = "productPrice")
    public  String productPrice;

    @ColumnInfo(name = "vat")
    public  double vat;

    @ColumnInfo(name = "pricePlusTax")
    public  double pricePlusTax;

}
