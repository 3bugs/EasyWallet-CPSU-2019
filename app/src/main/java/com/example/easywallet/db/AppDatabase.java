package com.example.easywallet.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {LedgerItem.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "wallet.db";

    public abstract LedgerDao ledgerDao();

    private static AppDatabase mInstance;

    public static synchronized AppDatabase getInstance(final Context context) {
        if (mInstance == null) {
            mInstance = Room
                    .databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DB_NAME
                    )
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mInstance.ledgerDao().insert(
                                            new LedgerItem(
                                                    0, "ทดสอบ", 123456
                                            )
                                    );
                                }
                            });
                        }

                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);
                        }
                    })
                    .build();
        }
        return mInstance;
    }
}
