package com.example.easywallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.easywallet.adapter.LedgerRecyclerViewAdapter;
import com.example.easywallet.db.AppDatabase;
import com.example.easywallet.db.LedgerItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reloadData();

        Button incomeButton = findViewById(R.id.income_button);
        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LedgerItem item = new LedgerItem(0, "คุณแม่ให้เงิน", 5000);
                InsertTask task = new InsertTask(MainActivity.this, new InsertCallback() {
                    @Override
                    public void onInsertSuccess() {
                        reloadData();
                    }
                });
                task.execute(item);
            }
        });
    }

    private void reloadData() {
        GetTask getTask = new GetTask(MainActivity.this, new Callback() {
            @Override
            public void onGetLedger(List<LedgerItem> itemList) {
                RecyclerView recyclerView = findViewById(R.id.ledger_recycler_view);
                LedgerRecyclerViewAdapter adapter = new LedgerRecyclerViewAdapter(
                        MainActivity.this,
                        R.layout.item_ledger,
                        itemList
                );
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(adapter);
            }
        });
        getTask.execute();
    }

    private static class GetTask extends AsyncTask<Void, Void, List<LedgerItem>> {

        private Context mContext;
        private Callback mCallback;

        public GetTask(Context context, Callback callback) {
            this.mContext = context;
            this.mCallback = callback;
        }

        @Override
        protected List<LedgerItem> doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.getInstance(mContext);
            List<LedgerItem> itemList = db.ledgerDao().getAll();
            return itemList;
        }

        @Override
        protected void onPostExecute(List<LedgerItem> ledgerItemList) {
            super.onPostExecute(ledgerItemList);

            mCallback.onGetLedger(ledgerItemList);
        }
    } // ปิด GetTask

    private interface Callback {
        void onGetLedger(List<LedgerItem> itemList);
    }

    private static class InsertTask extends AsyncTask<LedgerItem, Void, Void> {

        private Context mContext;
        private InsertCallback mCallback;

        public InsertTask(Context context, InsertCallback callback) {
            this.mContext = context;
            this.mCallback = callback;
        }

        @Override
        protected Void doInBackground(LedgerItem... ledgerItems) {
            AppDatabase db = AppDatabase.getInstance(mContext);
            db.ledgerDao().insert(ledgerItems[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCallback.onInsertSuccess();
        }
    } // ปิด InsertTask

    private interface InsertCallback {
        void onInsertSuccess();
    }
}
