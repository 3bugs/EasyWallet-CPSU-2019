package com.example.easywallet.net;

import com.example.easywallet.db.LedgerItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLedgerResponse extends BaseResponse {

    @SerializedName("data_list")
    public List<LedgerItem> ledgerItemList;

}