package com.example.no1.photongallery.utils

import android.app.Activity
import android.app.IntentService
import android.app.ProgressDialog
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.no1.photongallery.AutoBackgroundActivity
import com.example.no1.photongallery.ImageRecycleAdapter
import com.example.no1.photongallery.MainActivity
import com.example.no1.photongallery.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import java.lang.ref.WeakReference


class AutoChangeBackgroundImage : IntentService("AutoChangeBackgroundImage") {
    var t: Target? = null
    override fun onHandleIntent(intent: Intent?) {
        LoadRandomPics(this).execute()
    }

    private class LoadRandomPics(context: Context) : AsyncTask<String, String, String>() {
        var result: MyJsonObject? = null
        var item: String = ""

        var parent: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg p0: String?): String? {
            val params = ArrayList<NameValuePair>()

            val jParser = JSONParser()
            var url = parent.get()?.getString(R.string.server_address) + "/api/get-random-photo/?gallery=1"

            result = MyJsonObject(jParser.makeHttpRequest(url, "GET", params, parent.get()))
            if (result != null && !result!!.isNull) {
                item = result!!.getStringSafe("image")
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            (parent.get() as AutoChangeBackgroundImage).t = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom) {
                    if (bitmap != null) {
                        changeBackground(bitmap)
                    }
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {}

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            }

            Picasso.with(parent.get()).load(parent.get()?.getString(R.string.server_address) + item).into(
                    (parent.get() as AutoChangeBackgroundImage).t)
        }

        private fun changeBackground(bitmap: Bitmap) {
            val myWallpaperManager: WallpaperManager = WallpaperManager.getInstance(parent.get())
            myWallpaperManager.setBitmap(bitmap);
        }
    }
}


