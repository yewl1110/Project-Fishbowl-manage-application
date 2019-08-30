package com.example.fish;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        Button popup_ok = findViewById(R.id.popup_delete_ok);
        Button popup_cancle = findViewById(R.id.popup_delete_cancle);

        Intent intent = getIntent();

        if(intent.getBooleanExtra("isStringArrayList", false)){
            intent.putExtra("id", intent.getStringArrayListExtra("id"));
        }
        else{
            intent.putExtra("id", intent.getStringExtra("id"));
        }

        popup_ok.setOnClickListener((view)-> {
                    intent.putExtra("position", intent.getIntExtra("position", -1));
                    setResult(RESULT_OK, intent);
                    finish();
                });

        popup_cancle.setOnClickListener((view)->{
            finish();
        });
    }
}
