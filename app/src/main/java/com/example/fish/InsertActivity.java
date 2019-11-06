package com.example.fish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InsertActivity extends AppCompatActivity {
    final int MAPS_ACTIVITY = 500;
    final int MAPS_OK = 510;

    Button popup_ok;
    Button popup_cancle;
    ImageButton search;

    double latitude;
    double longitude;

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

        search = findViewById(R.id.search);

        search.setOnClickListener((view)->{
            Intent intent = new Intent(this, MapsActivity.class);
            startActivityForResult(intent, MAPS_ACTIVITY);
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requsetCode, int resultCode, Intent data) {
        super.onActivityResult(requsetCode, resultCode, data);

        if(requsetCode == MAPS_ACTIVITY){
            if(resultCode == MAPS_OK){
                data.getDoubleExtra("latitude", 0);
                data.getDoubleExtra("longitude", 0);
            }
        }
    }
}
