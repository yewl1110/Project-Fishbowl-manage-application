package com.example.servertest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomAdapter(arrayList : ArrayList<Column>?) : BaseAdapter() {

    private var arrayList : ArrayList<Column>? = null

    init{
        this.arrayList = arrayList
    }

    override fun getCount(): Int {
        return arrayList!!.size
    }

    override fun getItem(p0: Int): Any {
        return arrayList!!.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var _view = view
        if(_view == null){
            val inflater = parent!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            _view = inflater.inflate(R.layout.list_item, parent, false)
        }

        var column = arrayList!!.get(position)

        _view!!.findViewById<TextView>(R.id.list_item_id).text = column.id
        _view!!.findViewById<TextView>(R.id.list_item_serial).text = column.serial
        _view!!.findViewById<TextView>(R.id.list_item_name).text = column.name

        return _view
    }
}