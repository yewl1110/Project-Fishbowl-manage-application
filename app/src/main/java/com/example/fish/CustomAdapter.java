package com.example.fish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<Column> arrayList;
    int cnt;
    boolean mState;

    public CustomAdapter(ArrayList<Column> arrayList){
        this.arrayList = arrayList;
        cnt = arrayList.size();
    }

    public void setCheckBoxState(boolean state){
        mState = state;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {return cnt;}

    @Override
    public Object getItem(int position) {return null;}

    @Override
    public long getItemId(int position) {return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            Context context = parent.getContext();

            if(inflater == null){
                inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.record, parent, false);
        }
        TextView name_edit = convertView.findViewById(R.id.name_edit);
        name_edit.setText(arrayList.get(position).name);

        CheckBox checkbox = convertView.findViewById(R.id.checkbox);

        if(mState) checkbox.setVisibility(View.VISIBLE);
        else checkbox.setVisibility(View.GONE);

        return convertView;
    }
}
