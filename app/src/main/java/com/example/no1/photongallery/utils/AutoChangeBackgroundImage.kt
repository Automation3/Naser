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
        // TODO: load random pic

//        val pDialog = ProgressDialog(this)
//        pDialog.setMessage("در حال اتصال به سرور")
//        pDialog.isIndeterminate = false
//        pDialog.setCancelable(false)
//        pDialog.show()
//        MainActivity.LoadIcons(this, pDialog).execute()
    ///////////////////////////////    LoadRandomPics(context = AutoBackgroundActivity).execute()   //////////////////////


        val t = ImageView(this)

        if (Math.random() < 0.5){
            t.setImageResource(R.drawable.test)
        }else{
            t.setImageResource(R.drawable.test2)
        }
        val draw = t.getDrawable() as BitmapDrawable
        val bitmap = draw.bitmap

        change_background(bitmap)

    }
/*
    private class LoadRandomPics(context: Activity) : AsyncTask<String, String, String>() {
        var result: MyJsonObject? = null
        var items: MyJsonArray? = null
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
                items = MyJsonArray(result!!.getJSONArraySafe("results"))
                context.mNext = result!!.getStringSafe("next")
                context.mPrev = result!!.getStringSafe("previous")
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // pDialog.get()!!.dismiss()
            val context: MainActivity = parent.get() as MainActivity
            context.runOnUiThread {
                if (items != null && !items!!.isNull) {
                    val temp = ArrayList<String>()
                    val titles = ArrayList<String>()
                    val subtitles = ArrayList<String>()
                    for (i in 0 until items!!.length()) {
                        val item = MyJsonObject(items!!.getJSONObjectSafe(i))
                        temp.add(context.getString(R.string.server_address) + item.getStringSafe("image"))
                        titles.add(item.getStringSafe("title"))
                        subtitles.add(item.getStringSafe("subtitle"))
                    }
                    val mRecyclerView = context.findViewById<RecyclerView>(R.id.grdCollection)
                    val llm = LinearLayoutManager(context)
                    mRecyclerView.layoutManager = llm
                    if (context.mAdapter == null) {
                        context.mAdapter = ImageRecycleAdapter(context, temp, 0, titles, subtitles)
//                        context.mAdapter = ImageRecycleAdapter(context, temp, 0, titles, subtitles,liked_image)
                        mRecyclerView.adapter = context.mAdapter
                        context.mAdapter!!.mListener = context
                        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                             //   context.totalItemCount = llm.itemCount
                             //   context.lastVisibleItem = llm.findLastVisibleItemPosition()
//                                if (context.totalItemCount <= context.lastVisibleItem + context.visibleThreshold
//                                        && context.mNext.isNotEmpty() && context.mNext != "null") {
//                                    val pDialog = ProgressDialog(context)
//                                    pDialog.setMessage("در حال اتصال به سرور")
//                                    pDialog.isIndeterminate = false
//                                    pDialog.setCancelable(false)
//                                    pDialog.show()
//                                    LoadRandomPics(context, pDialog).execute(context.mNext)
//                                }
                            }
                    })
                    } else
                        context.mAdapter!!.setIDs(temp, titles, subtitles)
                }
            }
        }
    }*/

     fun change_background(bitmap: Bitmap){
                   val   myWallpaperManager:WallpaperManager
                   myWallpaperManager = WallpaperManager.getInstance(getApplicationContext())
            //     myWallpaperManager.setResource(R.drawable.five);
                  myWallpaperManager.setBitmap(bitmap);
                 }
}


