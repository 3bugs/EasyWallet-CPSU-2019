package com.example.easywallet.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ledger")
public class LedgerItem {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    public String description;

    @ColumnInfo(name = "amount")
    @SerializedName("amount")
    public int amount;

    public LedgerItem(int id, String description, int amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }
}
