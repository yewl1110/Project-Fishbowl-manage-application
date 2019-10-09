package com.example.servertest

import android.content.ContentValues
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class RequestHttpURLConnection {
    fun request(_url : String, _params : ContentValues) : String {
        var urlConn : HttpURLConnection? = null
        var sbParams = StringBuffer()


        if(_params == null)
            sbParams.append("")
        else{
            var isAnd = false
            var key : String? = null
            var value : String? = null

            var parameter:Map.Entry<String, Object>? = null

            while(true){
                if(_params != null && _params.valueSet().size < 1)
                    break

                key = parameter!!.key
                value = parameter!!.value.toString()

                if(isAnd)
                    sbParams.append("&")

                sbParams.append(key).append("=").append(value)

                if(!isAnd)
                    if(_params.size() >= 2)
                        isAnd = true
            }
        }

        try{
            val url = URL(_url)
            urlConn = url.openConnection() as HttpURLConnection
            urlConn.run{
                requestMethod = "POST"
                setRequestProperty("Accept-Charset", "UTF-8")
                setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8")
            }

            val strParams = sbParams.toString()
            val os = urlConn.outputStream
            os.run {
                write(strParams.toByteArray())
                flush()
                close()
            }

            if(urlConn.responseCode != HttpURLConnection.HTTP_OK)
                return ""

            val reader = BufferedReader(InputStreamReader(urlConn.inputStream, "UTF-8"))

            var line : String? = null
            var  page = ""

            while(true) {
                line = reader.readLine()
                if(line == null)
                    break
                page += line
            }

            return page

        }catch(e : MalformedURLException){
            e.printStackTrace()
        }catch(e : IOException){
            e.printStackTrace()
        }finally{
            if(urlConn != null)
                urlConn.disconnect()
        }

        return ""
    }
}