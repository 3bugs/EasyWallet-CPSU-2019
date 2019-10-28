package com.example.easywallet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easywallet.adapter.LedgerRecyclerViewAdapter;
import com.example.easywallet.db.LedgerItem;
import com.example.easywallet.db.LedgerRepository;
import com.example.easywallet.net.ApiClient;
import com.example.easywallet.net.GetLedgerResponse;
import com.example.easywallet.net.InsertLedgerResponse;
import com.example.easywallet.net.WebServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InsertActivity extends AppCompatActivity {

    private int mLedgerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        Intent intent = getIntent();
        mLedgerType = intent.getIntExtra("type", 0);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(mLedgerType == 0 ? "บันทึกรายรับ" : "บันทึกรายจ่าย");
        }

        ImageView ledgerTypeImageView = findViewById(R.id.ledger_type_image_view);
        ledgerTypeImageView.setImageResource(
                mLedgerType == 0 ? R.drawable.ic_income : R.drawable.ic_expense
        );

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText descriptionEditText = findViewById(R.id.description_edit_text);
                String description = descriptionEditText.getText().toString();

                EditText amountEditText = findViewById(R.id.amount_edit_text);
                int amount = Integer.parseInt(amountEditText.getText().toString());

                LedgerItem item = new LedgerItem(0, description, amount * (mLedgerType == 1 ? -1 : 1));

                //insertLedger(item); // กรณีเก็บข้อมูลในฐานข้อมูล SQLite บนมือถือ
                insertLedgerOnServer(item);; // กรณีเก็บข้อมูลในฐานข้อมูล MySQL (MariaDB) บน server
            }
        });
    }

    private void insertLedger(LedgerItem item) {
        LedgerRepository repo = new LedgerRepository(InsertActivity.this);
        repo.insertLedger(item, new LedgerRepository.InsertCallback() {
            @Override
            public void onInsertSuccess() {
                finish();
            }
        });
    }

    private void insertLedgerOnServer(LedgerItem item) {
        Retrofit retrofit = ApiClient.getClient();
        WebServices services = retrofit.create(WebServices.class);

        Call<InsertLedgerResponse> call = services.insertLedger(item.description, item.amount);
        call.enqueue(new Callback<InsertLedgerResponse>() {
            @Override
            public void onResponse(Call<InsertLedgerResponse> call, Response<InsertLedgerResponse> response) {
                InsertLedgerResponse result = response.body();

                if (result.errorCode == 0) {  // อ่านข้อมูลจาก MySQL สำเร็จ
                    // แสดง Success message
                    Toast.makeText(InsertActivity.this, result.errorMessage, Toast.LENGTH_LONG).show();
                    // ปิดหน้า InsertActivity เพื่อกลับไปหน้า MainActivity
                    finish();

                } else { // เกิด error ในการเพิ่มข้อมูลลง MySQL (อาจเกิดตอน connect db หรือตอนรันคำสั่ง SQL)
                    new AlertDialog.Builder(InsertActivity.this)
                            .setTitle("Error")
                            .setMessage(result.errorMessage)
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<InsertLedgerResponse> call, Throwable t) { // error เช่น มือถือไม่มีเน็ต, server ล่ม
                Log.e("InsertActivity", "Error: " + t.getMessage());

                new AlertDialog.Builder(InsertActivity.this)
                        .setTitle("Error")
                        .setMessage("เกิดข้อผิดพลาดในการเชื่อมต่อเครือข่าย")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }
}
