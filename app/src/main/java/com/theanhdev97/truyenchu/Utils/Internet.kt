package com.theanhdev97.truyenhot.Utils

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.DefaultRetryPolicy
import com.theanhdev97.truyenhchu.Utils.Const
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


/**
 * Created by DELL on 05/07/2017.
 */
class Internet() {
    companion object {
        @Throws(IOException::class)
        fun downloadContent(url: String): String {
            var `is`: InputStream? = null
            var conn: HttpURLConnection? = null
            val length = 500

            try {
                val url = URL(url)
                conn = url.openConnection() as HttpURLConnection
                conn.readTimeout = 10000
                conn.connectTimeout = 15000
                conn.requestMethod = "GET"
                conn.doInput = true
                conn.connect()
                //            int response = conn.getResponseCode();
                `is` = conn.inputStream
                val br = BufferedReader(InputStreamReader(`is`!!, "UTF-8") as Reader?)
                var line = br.readLine()
                val builder = StringBuilder()
                while (line != null) {
                    builder.append(line)
                    line = br.readLine()
                }
                return builder.toString()
            } finally {
                if (`is` != null) {
                    `is`.close()
                }
                conn!!.disconnect()
            }
        }

        fun getContentStringFromInternetURL(url: String, context: Context, callback: getContentCallback) {
            val queue = Volley.newRequestQueue(context)
            var content: String = ""
            val stringRequest = StringRequest(
                    Request.Method.GET,
                    url,
                    Response.Listener<String> {
                        response ->
                        content = response.toString()
                        callback.onSuccess(content)
                    },
                    Response.ErrorListener {
                        error ->
                        callback.onFailure(error.message!!)
                        Log.d(Const.TAG, "error : ${error.message}")
                    })
            stringRequest.setRetryPolicy(DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
            // Add the request to the RequestQueue.
            queue.add(stringRequest)
        }
    }

    interface getContentCallback {
        fun onSuccess(content: String)
        fun onFailure(error: String)
    }
}