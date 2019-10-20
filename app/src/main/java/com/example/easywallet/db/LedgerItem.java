package com.example.easywallet.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ledger")
public class LedgerItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "amount")
    public int amount;

    public LedgerItem(int id, String description, int amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }
}
