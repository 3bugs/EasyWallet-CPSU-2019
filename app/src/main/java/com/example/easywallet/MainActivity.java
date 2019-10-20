package com.example.easywallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.easywallet.adapter.LedgerRecyclerViewAdapter;
import com.example.easywallet.db.AppDatabase;
import com.example.easywallet.db.LedgerItem;
import com.example.easywallet.db.LedgerRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button incomeButton = findViewById(R.id.income_button);
        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });

        Button expenseButton = findViewById(R.id.expense_button);
        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    private void reloadData() {
        LedgerRepository repo = new LedgerRepository(MainActivity.this);

        repo.getLedger(new LedgerRepository.Callback() {
            @Override
            public void onGetLedger(List<LedgerItem> itemList) {

                int totalAmount = 0;

                for (LedgerItem item : itemList) {
                    totalAmount += item.amount;
                }

                TextView balanceTextView = findViewById(R.id.balance_text_view);
                balanceTextView.setText("คงเหลือ ".concat(String.valueOf(totalAmount)).concat(" บาท"));

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
    }


}
