package com.example.easywallet.db;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class LedgerRepository {

    private Context mContext;

    public LedgerRepository(Context mContext) {
        this.mContext = mContext;
    }

    public void getLedger(Callback callback) {
        GetTask getTask = new GetTask(mContext, callback);
        getTask.execute();
    }

    public void insertLedger(LedgerItem item, InsertCallback callback) {
        InsertTask insertTask = new InsertTask(mContext, callback);
        insertTask.execute(item);
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

    public interface Callback {
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

    public interface InsertCallback {
        void onInsertSuccess();
    }
}
