package com.example.no1.photongallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Spannable
import android.text.SpannableString
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.no1.photongallery.utils.*
import com.squareup.picasso.Picasso
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import java.lang.ref.WeakReference
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, OnImageClickedListener {

    lateinit var btnUp: ImageView
    lateinit var imgTransparent: ImageView
    private var mLayout = 0
    var mNext = ""
    var mPrev = ""
    var mAdapter: ImageRecycleAdapter? = null
    var curGallery: String = "-1"
    var curGalleyTextView: TextView? = null
    private val visibleThreshold = 4
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    var state = "0"
    var switch: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.elevation = 2f
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        val font = Typeface.createFromAsset(assets, "fonts/iranyekanwebregular.ttf")
        val fontB = Typeface.createFromAsset(assets, "fonts/iranyekanwebbold.ttf")

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val metrics = DisplayMetrics()

        val imageView = findViewById<ImageView>(R.id.imgLogo)
        val imgMid = imageView.layoutParams as RelativeLayout.LayoutParams
        imgMid.marginEnd = 50
        imageView.layoutParams = imgMid

        windowManager.defaultDisplay.getMetrics(metrics)
        val params = navigationView.layoutParams as DrawerLayout.LayoutParams
        params.width = metrics.widthPixels
        navigationView.layoutParams = params
        var textView = navigationView.getHeaderView(0).findViewById<TextView>(R.id.txtName)
        textView.typeface = fontB
        textView = navigationView.getHeaderView(0).findViewById(R.id.txtInfo)
        textView.typeface = font
        val m = navigationView.menu
        for (i in 0 until m.size()) {
            val mi = m.getItem(i)
            val subMenu = mi.subMenu
            //the method we have create in activity
            applyFontToMenuItem(mi)
        }
        switch = ImageView(this)

        //TODO laod sate from SharedPref and set it into switch
        switch?.setImageResource(R.drawable.autobg_active)
        navigationView.menu.findItem(R.id.nav_auto_bg).actionView = switch

        btnUp = findViewById(R.id.btnUp)
        btnUp.setOnClickListener(this)
        var button = findViewById<ImageView>(R.id.grid1)
        button.setOnClickListener({
            mAdapter!!.setSize(1)
        })
        button = findViewById(R.id.grid2)
        button.setOnClickListener({
            mAdapter!!.setSize(2)
        })
        button = findViewById(R.id.grid4)
        button.setOnClickListener({
            mAdapter!!.setSize(4)
        })
        button = findViewById(R.id.grid5)
        button.setOnClickListener({
            mAdapter!!.setSize(0)
        })
        button = findViewById(R.id.grid6)
        button.setOnClickListener({
            mAdapter!!.setSize(3)
        })
        imgTransparent = findViewById(R.id.imgTransparent)

        LoadIcons(this).execute()
    }

    private fun applyFontToMenuItem(mi: MenuItem) {
        val font = Typeface.createFromAsset(assets, "fonts/iranyekanwebregular.ttf")
        val mNewTitle = SpannableString(mi.title)
        mNewTitle.setSpan(CustomTypefaceSpan("", font), 0, mNewTitle.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        mi.title = mNewTitle
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //getMenuInflater().inflate(R.menu.main, menu);
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.nav_auto_bg -> {
                val intent = Intent(this, AutoBackgroundActivity::class.java)
                // startActivity(intent)
                startActivityForResult(intent, 1)
            }
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(view: View) {
        if (view == btnUp) {
            if (mLayout == 0) {
                val grid = findViewById<LinearLayout>(R.id.areaGrid)
                grid.visibility = View.VISIBLE
                val icons = findViewById<LinearLayout>(R.id.areaIcons)
                icons.visibility = View.GONE
                mLayout = 1
            } else {
                val grid = findViewById<LinearLayout>(R.id.areaGrid)
                grid.visibility = View.GONE
                val icons = findViewById<LinearLayout>(R.id.areaIcons)
                icons.visibility = View.VISIBLE
                mLayout = 0
            }
        }
    }

    override fun onImageClicked(url: String) {
        Picasso.with(this).load(url).into(imgTransparent)
        imgTransparent.visibility = View.VISIBLE
        imgTransparent.setOnClickListener({
            imgTransparent.visibility = View.GONE
        })
    }

    override fun onImageReleased() {
        imgTransparent.visibility = View.GONE
    }

    private class LoadIcons(context: Activity) : AsyncTask<String, String, String>() {

        var result: MyJsonObject? = null
        var items: MyJsonArray? = null
        var parent: WeakReference<Activity> = WeakReference(context)

        override fun doInBackground(vararg p0: String?): String? {
            val params = ArrayList<NameValuePair>()
            val jParser = JSONParser()
            val context: Context? = parent.get()
            result = MyJsonObject(jParser.makeHttpRequest(context!!.getString(R.string.server_address) +
                    "/api/galleries/", "GET", params, context))
            if (result != null && !result!!.isNull)
                items = MyJsonArray(result!!.getJSONArraySafe("results"))
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val context = parent.get()

            context!!.runOnUiThread {
                val font = Typeface.createFromAsset(context.assets, "fonts/iranyekanwebregular.ttf")
                if (items != null && !items!!.isNull)
                    for (i in 0 until items!!.length()) {
                        val item = MyJsonObject(items!!.getJSONObjectSafe(i))
                        if (!item.isNull) {
                            val area = context.findViewById<LinearLayout>(R.id.areaIcons)
                            val v = context.layoutInflater.inflate(R.layout.gallery_icons, area, false)
                            val imgGalley = v.findViewById<ImageView>(R.id.imgGallery)
                            val txtGallery = v.findViewById<TextView>(R.id.txtGallery)
                            txtGallery.text = item.getStringSafe("title")
                            txtGallery.typeface = font
                            Picasso.with(context).load("http://178.79.133.93" +
                                    item.getStringSafe("cover_pic")).into(imgGalley)
                            v.tag = item.getStringSafe("pk")
                            v.setOnClickListener({
                                val arrLikes = (context as MainActivity).mAdapter!!.mLIKEs
                                (context as MainActivity).mAdapter = null
                                context.mNext = ""
                                context.curGallery = v.tag as String
                                LoadPics(context, arrLikes).execute()
                                //////////////////  mohammad /////////////////////
//                                txtGallery.currentTextColor == Color.RED
                                if (context.curGalleyTextView != null)
                                    context.curGalleyTextView!!.setTextColor(Color.WHITE)
                                txtGallery.setTextColor(Color.BLACK)
                                context.curGalleyTextView = txtGallery
                                //////////////////////////////////////////////////
                            })
                            area.addView(v)
                        }
                    }
                LoadLikes(context).execute()
            }
        }
    }

    private class LoadPics(context: Activity, var mLikes: ArrayList<String>? = null) : AsyncTask<String, String, String>() {
        var result: MyJsonObject? = null
        var items: MyJsonArray? = null
        var parent: WeakReference<Activity> = WeakReference(context)

        override fun doInBackground(vararg p0: String?): String? {
            val params = ArrayList<NameValuePair>()

            val jParser = JSONParser()
            val context: MainActivity = parent.get() as MainActivity
            if (context.curGallery != "-1")
                params.add(BasicNameValuePair("gallery", context.curGallery))
            var url = context.getString(R.string.server_address) +
                    "/api/photos/"
            if (p0.isNotEmpty()) {
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
            val context: MainActivity = parent.get() as MainActivity
            context.runOnUiThread {
                if (items != null && !items!!.isNull) {
                    val temp = ArrayList<String>()
                    val tempFullImage = ArrayList<String>()
                    val titles = ArrayList<String>()
                    val subtitles = ArrayList<String>()
                    val iDs = ArrayList<String>()
                    for (i in 0 until items!!.length()) {
                        val item = MyJsonObject(items!!.getJSONObjectSafe(i))
//                        temp.add(context.getString(R.string.server_address) + item.getStringSafe("image"))
                        temp.add(context.getString(R.string.server_address) + item.getStringSafe("thumbnail"))
                        tempFullImage.add(context.getString(R.string.server_address) + item.getStringSafe("image"))
                        titles.add(item.getStringSafe("title"))
                        subtitles.add(item.getStringSafe("subtitle"))
                        iDs.add(item.getStringSafe("pk"))
                    }
                    val mRecyclerView = context.findViewById<RecyclerView>(R.id.grdCollection)
                    val llm = LinearLayoutManager(context)
                    mRecyclerView.layoutManager = llm
                    if (context.mAdapter == null) {
//                        context.mAdapter = ImageRecycleAdapter(context, temp, 0, titles, subtitles)
                        if (mLikes == null)
                            mLikes = ArrayList<String>()
                        context.mAdapter = ImageRecycleAdapter(context, temp, 0, titles, subtitles, mLikes, iDs)
                        mRecyclerView.adapter = context.mAdapter
                        context.mAdapter!!.mListener = context
                        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                                context.totalItemCount = llm.itemCount
                                context.lastVisibleItem = llm.findLastVisibleItemPosition()
                                if (context.totalItemCount <= context.lastVisibleItem + context.visibleThreshold
                                        && context.mNext.isNotEmpty() && context.mNext != "null") {
                                    LoadPics(context).execute(context.mNext)
                                }
                            }
                        })
                    } else
                        context.mAdapter!!.setIDs(temp, titles, subtitles, iDs)
                }
            }
        }
    }

    private class LoadLikes(context: Activity) : AsyncTask<String, String, String>() {
        var resultLike: MyJsonObject? = null
        var itemsLike: MyJsonArray? = null
        var parentLike: WeakReference<Activity> = WeakReference(context)

        override fun doInBackground(vararg p0: String?): String? {

            val params = ArrayList<NameValuePair>()
            val jParser = JSONParser()
            val context: Context? = parentLike.get()
            resultLike = MyJsonObject(jParser.makeHttpRequest(context!!.getString(R.string.server_address) +
                    "/api/bookmarks/", "GET", params, context))
            if (resultLike != null && !resultLike!!.isNull)
                itemsLike = MyJsonArray(resultLike!!.getJSONArraySafe("results"))
            //ToDo: this only laods the first 10 likes from user
            //TODO:  check the itemsLike.getStringSafe("next") to see if more likes are available
            //TODO: if more is there you have to load again , check line 339 for an example
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (itemsLike != null && !itemsLike!!.isNull) {
                val mLikes = ArrayList<String>()
                for (i in 0 until itemsLike!!.length()) {
                    val item = MyJsonObject(itemsLike!!.getJSONObjectSafe(i))
                    if (!item.isNull) {
                        //      pk.add(item.getStringSafe("pk"))
                        mLikes.add(item.getStringSafe("object_id"))
                    }
                }
                LoadPics(parentLike.get()!!, mLikes).execute()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                state = data.getStringExtra("keyIdentifier")
                //TODO save this state into SharedPreferences
                if (state == "0") {
                    switchBackground(this, state)
                }
                if (state == "1") {
                    switchBackground(this, state)
                }
            }
        }
    }

    private fun switchBackground(context: Context, state: String) {
        if (state == "1")
            switch?.setImageResource(R.drawable.autobg_active)
        else
            switch?.setImageResource(R.drawable.double_down)
    }

}

