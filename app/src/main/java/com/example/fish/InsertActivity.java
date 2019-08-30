package com.example.fish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InsertActivity extends AppCompatActivity {
    Button popup_ok;
    Button popup_cancle;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        popup_ok = findViewById(R.id.popup_insert_ok);
        popup_cancle = findViewById(R.id.popup_insert_cancle);

        popup_ok.setOnClickListener((view)->{
            EditText editText = (EditText)findViewById(R.id.popup_name_edit);
            String name = editText.getText().toString().trim();

            if(name.length() == 0){
                Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent();
                intent.putExtra("name", name);
                setResult(Activity.RESULT_OK, intent);
                this.finish();
            }

        });

        popup_cancle.setOnClickListener((view)->{
            this.finish();
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
