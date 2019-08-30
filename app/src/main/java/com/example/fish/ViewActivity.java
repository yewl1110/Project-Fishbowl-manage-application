package com.example.fish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {
    ArrayList<String> column_data = new ArrayList();
    int position;

    @Override
    public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_view);

       Intent intent = getIntent();

       ActionBar actionBar = getSupportActionBar();
       actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
       actionBar.setCustomView(R.layout.actionbar);
       actionBar.setDisplayHomeAsUpEnabled(true);
       actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_sync_24px);

       TextView textView = findViewById(R.id.name_view);
       textView.setText(intent.getStringExtra("name"));
       column_data = intent.getStringArrayListExtra("data");
       position = intent.getIntExtra("position", -1);

       ArrayList<TextView> viewArray = new ArrayList();
       viewArray.add(findViewById(R.id.id_view));
       viewArray.add(findViewById(R.id.serial_view));

       try{
           for(int i = 0 ; i < column_data.size() - 1 ; i++){
               viewArray.get(i).setText(column_data.get(i));
           }
       }catch(Exception e){}
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item == null) return false;

        switch(item.getItemId()){
            case android.R.id.home: {
                Toast.makeText(getApplicationContext(), "로딩 메뉴 선택", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.menu_update: {
                Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                intent.putExtra("name", column_data.get(Key.NAME));
                startActivityForResult(intent, IntentCodes.UPDATE_ACTIVITY);
                break;
            }

            case R.id.menu_delete: {
                Intent intent = new Intent(getApplicationContext(), DeleteActivity.class);
                intent.putExtra("id", column_data.get(Key.ID));
                startActivityForResult(intent, IntentCodes.DELETE_ACTIVITY);
                break;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case IntentCodes.UPDATE_ACTIVITY: {
                if(resultCode == Activity.RESULT_OK){
                    getIntent().putExtra("id", column_data.get(Key.ID));
                    getIntent().putExtra("name", data.getStringExtra("name"));
                    getIntent().putExtra("position", position);

                    setResult(IntentCodes.RESULT_VIEW_UPDATE, getIntent());
                    finish();
                }
            }

            case IntentCodes.DELETE_ACTIVITY: {
                if(resultCode == Activity.RESULT_OK){
                    getIntent().putExtra("id", column_data.get(Key.ID));
                    setResult(IntentCodes.RESULT_VIEW_DELETE, getIntent());
                    finish();
                }
            }
        }
    }

    ArrayList<Column> parse(String str){ //데이터 1개만 가져옴,
        ArrayList<Column> parseResult = new ArrayList<>();
        if(str == null) return parseResult;

        try{
            JSONObject jsonObject = new JSONObject(str);
            String result = jsonObject.getString("result"); // 바꽈
            JSONArray jsonArray = new JSONArray(result);

            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject subJsonObject = jsonArray.getJSONObject(i);

                /*
                val id = subJsonObject.getString("id")
                val serial = subJsonObject.getString("serial")
                val name = subJsonObject.getString("name")

                parseResult.add(Column(id, serial, name))
                 */
            }
        }
        catch(JSONException e){

        }
        finally {
            return parseResult;
        }
    }
}
