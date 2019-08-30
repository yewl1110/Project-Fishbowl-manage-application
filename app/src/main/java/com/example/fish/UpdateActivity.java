package com.example.fish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        EditText editText = findViewById(R.id.popup_name_edit);
        editText.setText(getIntent().getStringExtra("name"));

        Button popup_ok = findViewById(R.id.popup_insert_ok);
        Button popup_cancle = findViewById(R.id.popup_insert_cancle);

        popup_ok.setOnClickListener((view) -> {
                String name = editText.getText().toString().trim();

                if(name.length() == 0)
                    Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = getIntent();
                    intent.putExtra("name", name);
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    intent.putExtra("position", getIntent().getIntExtra("position", -1));
                    setResult(Activity.RESULT_OK, intent);
                    this.finish();
                }
        });

        popup_cancle.setOnClickListener((view) -> {
                finish();
        });
    }
}
