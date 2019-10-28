package com.example.easywallet.net;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebServices {

    @GET("get_ledger")
    Call<GetLedgerResponse> getLedger(
    );

    @FormUrlEncoded
    @POST("insert_ledger")
    Call<InsertLedgerResponse> insertLedger(
            @Field("description") String description,
            @Field("amount") int amount
    );
}