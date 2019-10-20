package com.example.easywallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.easywallet.db.AppDatabase;
import com.example.easywallet.db.LedgerItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        LedgerItem item = new LedgerItem(0, "คุณแม่ให้เงิน", 5000);
        InsertTask task = new InsertTask(MainActivity.this);
        task.execute(item);
    }

    private static class InsertTask extends AsyncTask<LedgerItem, Void, Void> {

        private Context mContext;

        public InsertTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected Void doInBackground(LedgerItem... ledgerItems) {
            AppDatabase db = AppDatabase.getInstance(mContext);
            db.ledgerDao().insert(ledgerItems[0]);
            return null;
        }
    }
}
