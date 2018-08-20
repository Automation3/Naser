package com.example.no1.photongallery.utils

import android.app.Activity
import android.app.IntentService
import android.app.ProgressDialog
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.example.no1.photongallery.AutoBackgroundActivity
import com.example.no1.photongallery.ImageRecycleAdapter
import com.example.no1.photongallery.MainActivity
import com.example.no1.photongallery.R
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import java.lang.ref.WeakReference


class AutoChangeBackgroundImage : IntentService("AutoChangeBackgroundImage") {

    override fun onHandleIntent(intent: Intent?) {

        LoadRandomPics(this).execute()

     /*   val t = ImageView(this)
        if (Math.random() < 0.5){
            t.setImageResource(R.drawable.test)
        }else{
            t.setImageResource(R.drawable.test2)
        }
        val draw = t.getDrawable() as BitmapDrawable
        val bitmap = draw.bitmap
        changeBackground(bitmap)*/

    }
    private class LoadRandomPics(context: Activity) : AsyncTask<String, String, String>() {
        var result: MyJsonObject? = null
        var items: MyJsonArray? = null
        var title: MyJsonArray? = null
        var thumbnail: MyJsonArray? = null

        var parent: WeakReference<Activity> = WeakReference(context)
      //  val pDialog: WeakReference<ProgressDialog> = WeakReference(dialog)

        override fun doInBackground(vararg p0: String?): String? {
            val params = ArrayList<NameValuePair>()

            val jParser = JSONParser()
            val context: MainActivity = parent.get() as MainActivity
            if (context.curGallery != "-1")
                params.add(BasicNameValuePair("gallery", context.curGallery))
            var url = context.getString(R.string.server_address) +
                    "/api/get-random-photo/?gallery=1"
            if (p0.isNotEmpty()){
                url = p0[0]!!
            }

            result = MyJsonObject(jParser.makeHttpRequest(url, "GET", params, context))
            if (result != null && !result!!.isNull) {
                items = MyJsonArray(result!!.getJSONArraySafe("image"))
                title = MyJsonArray(result!!.getJSONArraySafe("title"))
                thumbnail = MyJsonArray(result!!.getJSONArraySafe("thumbnail"))


            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // pDialog.get()!!.dismiss()
            val context: MainActivity = parent.get() as MainActivity
            context.runOnUiThread {
                if (items != null && !items!!.isNull) {
                    changeBackground)(items)            }
                    }
            }
    }

      private fun changeBackground(bitmap: Bitmap){
                   val myWallpaperManager:WallpaperManager = WallpaperManager.getInstance(getApplicationContext())
                  myWallpaperManager.setBitmap(bitmap);
      }
}


