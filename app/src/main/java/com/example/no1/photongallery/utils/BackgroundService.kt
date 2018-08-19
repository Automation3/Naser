package com.example.no1.photongallery.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.util.Log
import android.view.Display
import android.view.WindowManager

import com.example.no1.photongallery.R
import com.squareup.picasso.Picasso

import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import java.lang.ref.WeakReference
import java.net.URL

import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class BackgroundService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (isInternetAvailable(this@BackgroundService))
            LoadPicture(this).execute()
        else
            stopSelf()
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000 * 60,
                PendingIntent.getService(this, 0, Intent(this, BackgroundService::class.java), 0)
        )
    }

    private class LoadPicture(context: Service) : AsyncTask<String, String, String>() {
        internal val jParser = JSONParser()
        internal var url: String? = null
        internal var address: String? = null
        var bmp: Bitmap? = null
        var mContext: WeakReference<Service>? = null

        init {
            mContext = WeakReference(context)
        }

        override fun doInBackground(vararg args: String?): String? {
            val params = ArrayList<NameValuePair>()
            val context = mContext!!.get()
            url = context!!.getString(R.string.server_address) + "/api/photos/"
            val json = MyJsonObject(jParser.makeHttpRequest(url, "GET", params, context))
            if (!json.isNull) {
                val items = MyJsonArray(json.getJSONArraySafe("results"))
                val index: Int = (Math.random() * items.length()).toInt()
                val item = MyJsonObject(items.getJSONObjectSafe(index))
                address = item.getStringSafe("image")
                Log.d("URL IS", context.getString(R.string.server_address) + address)
                val window: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val display = window.defaultDisplay;
                val p: Point = Point()
                display.getSize(p)
                bmp = Picasso.with(context).load(context.getString(R.string.server_address) + address)
                        .centerCrop().resize(p.x,p.y).get()
            }
            return null
        }

        override fun onPostExecute(file_url: String?) {
            val manager = WallpaperManager.getInstance(mContext!!.get())
            if (bmp != null)
                manager.setBitmap(bmp)
            mContext!!.get()!!.stopSelf()
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}
