package com.example.servertest

import android.content.ContentValues
import android.net.Network
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
/*
class MainActivity : AppCompatActivity() {
    var TAG_RESULT:String?=null
    var TAG_ID:String?=null
    var TAG_SERIAL:String?=null
    var TAG_NAME:String?=null

    var INFO:JSONArray?=null

    var myJSON:String? = null
    var DataList: ArrayList<HashMap<String,String>>? = null

    var list:ListView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataList = ArrayList<HashMap<String,String>>()
        getData("117.16.17.210:15900/mysql.php")
    }

    fun showList() {
        try {
            var jsonObj: JSONObject? = null
            jsonObj = JSONObject(myJSON)
            INFO = jsonObj.getJSONArray((TAG_RESULT))

            for (i in 0..INFO!!.length()) {
                var temp: JSONObject? = null
                temp!!.getJSONObject(i as String)
                var id: String? = null
                var serial: String? = null
                var name: String? = null

                var INFOMATION: HashMap<String, String>? = null
                INFOMATION = HashMap<String, String>()

                id = temp!!.getString(TAG_ID)
                serial = temp!!.getString(TAG_SERIAL)
                name = temp!!.getString(TAG_NAME)

                INFOMATION.put(TAG_ID!!, id)
                INFOMATION.put(TAG_SERIAL!!, serial)
                INFOMATION.put(TAG_NAME!!, name)

                DataList!!.add(INFOMATION)
            }
            var Adapter: ListAdapter? = null
            Adapter = SimpleAdapter(
                applicationContext, DataList, R.layout.list_item, arrayOf(TAG_ID, TAG_SERIAL, TAG_NAME),
                intArrayOf(R.id.id, R.id.serial, R.id.name)
            )
            list = findViewById(R.id.listview)
            list!!.adapter = Adapter
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun getData(url:String) {
        class GetDataJSON: AsyncTask<String, Unit, String>() {
            override fun doInBackground(vararg p0: String?): String {
                var uri = p0[0]
                var bufferedReader: BufferedReader? = null

                try {
                    val url = URL(uri)
                    val connect = url.openConnection() as HttpURLConnection
                    var stringbuilder = StringBuilder()
                    bufferedReader = BufferedReader(InputStreamReader(connect.inputStream))
                    var json: String? = null

                    do {
                        json = bufferedReader.readLine()
                        if (json == null)
                            break
                        stringbuilder.append(json + "\n")
                    } while (true)

                    return stringbuilder.toString().trim({ it <= ' ' })
                } catch (e: Exception) {
                    return ""
                }
            }



            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                myJSON = result
                showList()
            }
        }
        var g = GetDataJSON()
        g.execute(url)
    }
}
*/

class MainActivity : AppCompatActivity() {

    var listView : ListView? = null
    var parseResult : ArrayList<Column>? = null
    var text_view : TextView? = null
    var _result : String? = null
    var text_view_time : TextView? = null
    var _result_time : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listview)
        parseResult = ArrayList<Column>()
        text_view = findViewById(R.id.text_view)
        text_view_time = findViewById(R.id.text_view_time)

        val url = "http://117.16.17.210:15900/mysql.php"
        var networkTask = NetworkTask(url, ContentValues())

        var networkTask_time = NetworkTask2("http://117.16.17.210:15900/test.php", ContentValues())
        networkTask.execute()
        networkTask_time.execute()

        Log.i("result.size",parseResult!!.size.toString())

        text_view!!.setOnClickListener {
            if(_result == null)
                return@setOnClickListener

            Toast.makeText(this, _result, Toast.LENGTH_SHORT).show()
            parseResult = parse(_result)
            listView!!.adapter = CustomAdapter(parseResult)
        }

        text_view_time!!.setOnClickListener {
            networkTask_time.cancel(true)
            networkTask_time = NetworkTask2("http://117.16.17.210:15900/test.php", ContentValues())
            networkTask_time.execute()

        }

    }

    inner class NetworkTask : AsyncTask<Unit, Unit, String> {
        var url : String? = null
        var values : ContentValues? = null

        constructor(url : String, values : ContentValues){
            this.url = url
            this.values = values
        }

        override fun doInBackground(vararg p0: Unit?): String {
            val requestHttpURLConnection = RequestHttpURLConnection()
            var result = requestHttpURLConnection.request(url!!, values!!)
            Log.i("onInBackground result", result)

            return result
        }

        override fun onPostExecute(result: String?) {  //?????????
            super.onPostExecute(result)
            text_view!!.text = result
            _result = result

            Log.i("onPostExecute result", result)
        }
    }

    inner class NetworkTask2 : AsyncTask<Unit, Unit, String> {
        var url : String? = null
        var values : ContentValues? = null

        constructor(url : String, values : ContentValues){
            this.url = url
            this.values = values
        }

        override fun doInBackground(vararg p0: Unit?): String {
            val requestHttpURLConnection = RequestHttpURLConnection()
            var result = requestHttpURLConnection.request(url!!, values!!)
            Log.i("onInBackground result", result)
            return result
        }

        override fun onPostExecute(result: String?) {  //?????????
            super.onPostExecute(result)
            text_view_time!!.text = result
            _result_time = result
            Log.i("onPostExecute result", result)
        }

        override fun onCancelled() {
            super.onCancelled()
        }
    }

    fun parse(str : String?) : ArrayList<Column>? {
        Log.i("parse","run")
        val parseResult = ArrayList<Column>()

        if(str == null)
            return parseResult

        Log.i("str",str)

        try{
            val jsonObject = JSONObject(str)
            val result = jsonObject.getString("result")
            val jsonArray = JSONArray(result)

            for(i in 0 .. jsonArray.length()){
                val subJsonObject = jsonArray.getJSONObject(i)

                val id = subJsonObject.getString("id")
                val serial = subJsonObject.getString("serial")
                val name = subJsonObject.getString("name")

                parseResult.add(Column(id, serial, name))
            }
        }
        catch(e : JSONException){
            e.printStackTrace()
        }
        finally{
            return parseResult
        }
    }
}

data class Column(var id : String, var serial : String, var name : String)