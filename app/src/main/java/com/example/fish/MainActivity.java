package com.example.fish;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DBHelper dbHelper;
    SQLiteDatabase database;
    ListView listView;
    BaseAdapter adapter;

    Boolean isMenuOpen = false;
    Animation fab_open;
    Animation fab_close;
    FloatingActionButton fab_menu;
    FloatingActionButton fab_add;
    FloatingActionButton fab_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //DB에 접근하기위해 (내부저장소) 권한설정
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ) {

            } else {
                ActivityCompat.requestPermissions(this, new String[] {"Manifest.permission.READ_EXTERNAL_STORAGE"}, 1);
            }
        }

        //get serial number
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[] {"Manifest.permission.READ_PHONE_STATE"}, 1);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("목록");

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        //ListView의 아이템 클릭했을 때

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener((parent, view, position, id) -> {

            switch(listView.getChoiceMode()){

                case ListView.CHOICE_MODE_MULTIPLE: {
                    CheckBox checkbox_all_select = findViewById(R.id.checkbox_all_select);
                    if(checkbox_all_select.isChecked()){
                        checkbox_all_select.setChecked(false);
                        checkbox_all_select.invalidate();
                    }
                    break;
                }

                case ListView.CHOICE_MODE_NONE:{
                    Cursor cursor = dbHelper.select(database);
                    cursor.moveToPosition(position);

                    ArrayList<String> data = new ArrayList();
                    try{
                        for(int i = 0 ; i < cursor.getColumnCount() ; i++){
                            data.add(cursor.getString(i));
                        }

                        Intent intent = new Intent(getApplication(), ViewActivity.class);
                        intent.putExtra("name", cursor.getString(Key.NAME));
                        intent.putExtra("data", data);
                        intent.putExtra("position", position);
                        startActivityForResult(intent, IntentCodes.VIEW_ACTIVITY);
                    }
                    catch(Exception e){ }
                    break;
                }
            }
        });


    //ListView의 아이템 롱클릭했을때 - update

        listView.setOnItemLongClickListener(((parent, view, position, id) -> {
            Cursor cursor = dbHelper.select(database);
            cursor.moveToPosition(position);

            Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
            intent.putExtra("name", cursor.getString(Key.NAME));
            intent.putExtra("id", cursor.getString(Key.ID));
            intent.putExtra("position", position);
            startActivityForResult(intent, IntentCodes.UPDATE_ACTIVITY);
            return true;
        }));

        // Floating Action Button 애니메이션 등록 (추가할때 삭제할때)

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);


        // Floating Action Button 버튼 등록 (추가, 삭제, 메뉴 )

        fab_menu = findViewById(R.id.fab_menu);
        fab_add = findViewById(R.id.fab_add);
        fab_delete = findViewById(R.id.fab_delete);

        fab_menu.setOnClickListener(this);
        fab_add.setOnClickListener(this);
        fab_delete.setOnClickListener(this);

        drow(false);
    }

    @Override
    protected void onActivityResult(int requsetCode, int resultCode, Intent data){
        super.onActivityResult(requsetCode, resultCode, data);

        switch(requsetCode){

            case IntentCodes.INSERT_ACTIVITY: {
                if(resultCode == Activity.RESULT_OK){
                    String name = data.getStringExtra("name");
                    dbHelper.insert(database, new Column(String.valueOf(System.currentTimeMillis()), Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID), name));
                    Toast.makeText(this, "추가완료", Toast.LENGTH_SHORT).show();
                }

                break;
            }

            case IntentCodes.UPDATE_ACTIVITY: {
                if(resultCode == Activity.RESULT_OK){
                    try{
                        dbHelper.update(database, data.getStringExtra("id"), data.getStringExtra("name"));
                        Toast.makeText(this, "변경 완료", Toast.LENGTH_SHORT).show();
                    } catch(Exception e){ }
                }

                break;
            }

            case IntentCodes.DELETE_ACTIVITY: {
                if(resultCode == Activity.RESULT_OK){
                    try {
                        dbHelper.delete(database, data.getStringExtra("id"));
                    } catch(Exception e){}
                }

                break;
            }

            case IntentCodes.DELETE_ARRAYS_ACTIVITY: {
                if(resultCode == Activity.RESULT_OK){
                    dbHelper.delete(database, data.getStringArrayListExtra("id"));

                    listView.setChoiceMode(ListView.CHOICE_MODE_NONE);

                    fab_menu.startAnimation(fab_open);
                    fab_menu.setClickable(true);

                    findViewById(R.id.listview_top).setVisibility(View.GONE);
                    findViewById(R.id.listview_bottom).setVisibility(View.GONE);

                    drow(false);

                    Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }

                break;
            }

            case IntentCodes.VIEW_ACTIVITY: {
                if(resultCode == IntentCodes.RESULT_VIEW_UPDATE){
                    dbHelper.update(database, data.getStringExtra("id"), data.getStringExtra("name"));
                    Cursor cursor = dbHelper.select(database);
                    cursor.moveToPosition(data.getIntExtra("position", 0));

                    ArrayList<String> list = new ArrayList();

                    try{
                        for(int i = 0 ; i < cursor.getColumnCount() ; i++){
                            list.add(cursor.getString(i));
                        }
                    } catch (Exception e){}

                    Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
                    intent.putExtra("name", cursor.getString(Key.NAME));
                    intent.putExtra("data", list);
                    startActivityForResult(intent, IntentCodes.VIEW_ACTIVITY);
                }
                else if(resultCode == IntentCodes.RESULT_VIEW_DELETE){
                    dbHelper.delete(database, data.getStringExtra("id"));
                    Toast.makeText(this, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                }

                break;
            }
        }
        drow(false);
    }


    void drow(boolean isCheckable){

        ArrayList<Column> arrayColumn = new ArrayList();
        Cursor cursor = dbHelper.select(database);

        while(cursor.moveToNext()){
            arrayColumn.add(new Column(cursor.getString(Key.SERIAL), cursor.getString(Key.ID), cursor.getString(Key.NAME)));
        }

        CustomAdapter customAdapter = new CustomAdapter(arrayColumn);
        customAdapter.setCheckBoxState(isCheckable);
        adapter = customAdapter;
        listView.setAdapter(adapter);
    }

    //Floating Action Button 클릭했을 때 이벤트 처리
    @Override
    public void onClick(View v){
        int id = v.getId();

        switch(id){

            case R.id.fab_menu: {
                anim();
                drow(false);
                break;
            }

            case R.id.fab_add: {
                anim();
                Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
                startActivityForResult(intent, IntentCodes.INSERT_ACTIVITY);
                break;
            }

            case R.id.fab_delete: {
                anim();
                delete();
                break;
            }

            default: {
                fab_add.startAnimation(fab_close);
                fab_delete.startAnimation(fab_close);
                fab_add.setClickable(false);
                fab_delete.setClickable(false);
            }
        }
    }

    //Floating Action Button 애니메이션
    void anim(){
        if(isMenuOpen){
            fab_add.startAnimation(fab_close);
            fab_delete.startAnimation(fab_close);
            fab_add.setClickable(false);
            fab_delete.setClickable(false);
        }
        else{
            fab_add.startAnimation(fab_open);
            fab_delete.startAnimation(fab_open);
            fab_add.setClickable(true);
            fab_delete.setClickable(true);
        }
        isMenuOpen = !isMenuOpen;
    }

    //다중삭제 구현
    void delete(){
        if(adapter.getCount() < 1) return;

        fab_menu.startAnimation(fab_close);
        fab_menu.setClickable(false);

        findViewById(R.id.listview_top).setVisibility(View.VISIBLE);
        findViewById(R.id.listview_bottom).setVisibility(View.VISIBLE);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        CheckBox checkBox_all_select = findViewById(R.id.checkbox_all_select);
        Button cancle = findViewById(R.id.delete_cancle);
        Button yes = findViewById(R.id.delete_yes);
        checkBox_all_select.setChecked(false);

        checkBox_all_select.setOnClickListener((view)->{
            int count = adapter.getCount();

            if(count < 1) return;

            boolean state = checkBox_all_select.isChecked();
            for(int i = 0 ; i < count ; i++){
                listView.setItemChecked(i, state);
            }
        });

        yes.setOnClickListener((view)->{
            int count = adapter.getCount();
            if (count < 1 ) return;

            SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
            ArrayList<String> deleteIds = new ArrayList();
            Cursor cursor = dbHelper.select(database);

            for(int i = 0 ; i < count ; i++){
                if(checkedItems.get(i)){
                    cursor.moveToPosition(i);
                    deleteIds.add(cursor.getString(Key.ID));
                }
            }

            Intent intent = new Intent(getApplicationContext(), DeleteActivity.class);
            intent.putExtra("id", deleteIds);
            intent.putExtra("isStringArrayList", true);
            startActivityForResult(intent, IntentCodes.DELETE_ARRAYS_ACTIVITY);
        });

        cancle.setOnClickListener((view)->{
            listView.setChoiceMode(ListView.CHOICE_MODE_NONE);

            fab_menu.startAnimation(fab_open);
            fab_menu.setClickable(true);

            findViewById(R.id.listview_top).setVisibility(View.GONE);
            findViewById(R.id.listview_bottom).setVisibility(View.GONE);

            drow(false);
        });


        drow(true);
    }

}

