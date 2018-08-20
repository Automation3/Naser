package com.example.no1.photongallery

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.AsyncTask
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.no1.photongallery.utils.OnImageClickedListener
import com.squareup.picasso.Picasso
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import com.example.no1.photongallery.utils.JSONParser
import com.example.no1.photongallery.utils.MyJsonArray
import com.example.no1.photongallery.utils.MyJsonObject
import org.apache.http.NameValuePair
import java.lang.ref.WeakReference

class ImageRecycleAdapter(private val mContext: Context, private var mURLs: ArrayList<String>,
                          private var mType: Int, private var mTitles: ArrayList<String>,
                          private var mSubtitles: ArrayList<String>, private var mLIKEs: ArrayList<String>?,
                          private var mIDs : ArrayList<String>) :
        RecyclerView.Adapter<ImageRecycleAdapter.ImageHolder>() {


/*class ImageRecycleAdapter(private val mContext: Context, private var mURLs: ArrayList<String>,
                          private var mType: Int, private var mTitles: ArrayList<String>,
                          private var mSubtitles: ArrayList<String>) :
        RecyclerView.Adapter<ImageRecycleAdapter.ImageHolder>() {*/

    private val mLayouts = arrayOf(R.layout.grid_two_h, R.layout.grid_two_v, R.layout.grid_two_h_two_w,
            R.layout.grid_three_h, R.layout.grid_one)
    private val mSizes = arrayOf(2, 2, 4, 3, 1)
    var mListener: OnImageClickedListener? = null
    private var prevArea: RelativeLayout? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val itemView: View = LayoutInflater.from(mContext).inflate(mLayouts[mType], parent, false)
        val viewGroup: ConstraintLayout = itemView.findViewById(R.id.parent)
        for (i in 0 until viewGroup.childCount) {
            val area = viewGroup.getChildAt(i)
            if (area is RelativeLayout) {
                fillRelative(area)
            }
        }
        return ImageHolder(itemView)
    }

    private fun fillRelative(area: RelativeLayout) {
        val img = ImageView(mContext)
        img.id = R.id.imgOne
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)
        img.scaleType = ImageView.ScaleType.CENTER_CROP
        img.layoutParams = params
        area.addView(img)
    }

    fun setSize(size: Int) {
        mType = size
        notifyDataSetChanged()
    }

    @SuppressLint("ClickableViewAccessibility")
    /*   private fun fillWithUrl(area: RelativeLayout, url: String, pTitle: String,
                               pSubtitle: String) {*/
    private fun fillWithUrl(area: RelativeLayout, url: String, pTitle: String,
                            pSubtitle: String, pLike: Boolean, position: String) {
        val imgOne = area.findViewById<ImageView>(R.id.imgOne)
        Picasso.with(mContext).load(url).fit().centerCrop().into(imgOne)

        imgOne.setOnLongClickListener({
            mListener?.onImageClicked(url)

            imgOne.setOnTouchListener { pView, pEvent ->
                pView.onTouchEvent(pEvent)
                if (pEvent.action == MotionEvent.ACTION_UP) {
                    mListener?.onImageReleased()
                }
                true
            }

            true
        })

        imgOne.setOnClickListener({ _ ->
            val imgMask = area.findViewWithTag<ImageView>("mask")
            if (imgMask == null) {
                if (prevArea != null) {
                    prevArea!!.removeView(prevArea!!.findViewWithTag<ImageView>("mask"))
                    prevArea!!.removeView(prevArea!!.findViewWithTag("like"))
                    prevArea!!.removeView(prevArea!!.findViewWithTag("subtitle"))
                    prevArea!!.removeView(prevArea!!.findViewWithTag("title"))
                    prevArea!!.removeView(prevArea!!.findViewWithTag("threeDot"))
                    prevArea!!.removeView(prevArea!!.findViewWithTag("share"))
                    prevArea!!.removeView(prevArea!!.findViewWithTag("print"))
                    prevArea!!.removeView(prevArea!!.findViewWithTag("wallpaper"))
                    prevArea = null
                }

                val anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)

                val mask = ImageView(mContext)
                mask.id = R.id.imgMask
                mask.tag = "mask"
                var params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                mask.layoutParams = params
                mask.setBackgroundColor(Color.argb(120, 0, 0, 0))
                area.addView(mask)
                mask.startAnimation(anim)

                ///////////////////// Mohammad /////////////////////////////////////////////
                /*     mask.setOnClickListener {
                         if (mLIKEs.contains((mURLs.get(itemCount)))) {
                             mLIKEs.remove((mURLs.get(itemCount)))
                             likePost(false, (mURLs.get(itemCount)).toInt())
                         }else {
                             mLIKEs.add((mURLs.get(itemCount)).toString())
                             likePost(true, (mURLs.get(itemCount)).toInt())
                         }
                     }*/

/*
                mask.setOnClickListener {
                    if (pLike) {
                        changeLike(false, position)
                    } else {
                        changeLike(true, position)

                    }
                }*/


                //////////////////////////////////////////////////////////////////////////////////
                val like = ImageView(mContext)
                like.id = R.id.imgLike
                like.tag = "like"
                val scale = mContext.resources.displayMetrics.density
                val px = (24 * scale + 0.5f).toInt() // replace with your dimens
                params = RelativeLayout.LayoutParams(px, px)
                params.setMargins(0, 0, 8, 8)
                params.addRule(RelativeLayout.ALIGN_PARENT_START)
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                like.layoutParams = params

                ////////////////////////  Mohammad  //////////////////////
                /* if (mLIKEs.contains((mURLs.get(itemCount))))

                     like.setImageResource(R.drawable.like_active)
                 else
                     like.setImageResource(R.drawable.like_diactive)
 */
/*

                if (pLike)
                    like.setImageResource(R.drawable.like_active)
                else
                    like.setImageResource(R.drawable.like_diactive)

*/

                //////////////////////////////////////////////

                /*           if (Math.random() < 0.7)
                               like.setImageResource(R.drawable.like_diactive)
                           else
                               like.setImageResource(R.drawable.like_active)*/

                area.addView(like)
                like.startAnimation(anim)

                val subtitle = TextView(mContext)
                subtitle.id = R.id.txtSubtitle
                subtitle.tag = "subtitle"
                params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(0, 0, 0, 5)
                params.marginStart = 5
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                params.addRule(RelativeLayout.END_OF, R.id.imgLike)
                subtitle.layoutParams = params
                subtitle.textSize = 4 * scale + 0.5f
                subtitle.setTextColor(Color.WHITE)
                subtitle.setPaddingRelative(5, 0, 5, 0)
                val font = Typeface.createFromAsset(mContext.assets, "fonts/iranyekanwebregular.ttf")
                subtitle.typeface = font
                if (pSubtitle != "null")
                    subtitle.text = pSubtitle
                else
                    subtitle.visibility = View.GONE
                area.addView(subtitle)
                subtitle.startAnimation(anim)

                val title = TextView(mContext)
                title.id = R.id.txtTitle
                title.tag = "title"
                params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(0, 0, 0, 0)
                params.marginStart = 5
                params.addRule(RelativeLayout.END_OF, R.id.imgLike)
                title.layoutParams = params
                title.textSize = 4 * scale + 0.5f
                title.setTextColor(Color.WHITE)
                title.setPaddingRelative(5, 0, 5, 0)
                title.typeface = font
                title.text = pTitle
                if (pSubtitle != "null") {
                    params = title.layoutParams as RelativeLayout.LayoutParams
                    params.addRule(RelativeLayout.ABOVE, R.id.txtSubtitle)
                    title.layoutParams = params
                } else {
                    params = title.layoutParams as RelativeLayout.LayoutParams
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    params.setMargins(0, 0, 0, 5)
                    title.layoutParams = params
                }
                area.addView(title)
                title.startAnimation(anim)

                val threeDot = ImageView(mContext)
                threeDot.id = R.id.imgMenuShort
                threeDot.tag = "threeDot"
                params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, px)
                params.setMargins(0, 0, 0, 8)
                params.addRule(RelativeLayout.ALIGN_PARENT_END)
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                threeDot.layoutParams = params
                threeDot.setImageResource(R.drawable.short_menu)
                area.addView(threeDot)
                threeDot.startAnimation(anim)
                threeDot.setOnClickListener({
                    val temp = area.findViewWithTag<ImageView>("wallpaper")
                    if (temp == null) {

                        val wallpaper = ImageView(mContext)
                        wallpaper.id = R.id.btnWallpaper
                        wallpaper.tag = "wallpaper"
                        params = RelativeLayout.LayoutParams(px * 2, px * 2)
                        params.addRule(RelativeLayout.ABOVE, R.id.imgMenuShort)
                        params.setMargins(0, 0, 0, 5)
                        params.marginEnd = 8
                        params.addRule(RelativeLayout.ALIGN_PARENT_END)
                        wallpaper.layoutParams = params
                        wallpaper.setImageResource(R.drawable.home_wallpaper)
                        area.addView(wallpaper)
                        wallpaper.startAnimation(anim)
                        wallpaper.setOnClickListener({
                            val intent = Intent(mContext, PictureActivity::class.java)
                            intent.putExtra("address", url)
                            mContext.startActivity(intent)
                        })

                        val share = ImageView(mContext)
                        share.id = R.id.btnShare
                        share.tag = "share"
                        params = RelativeLayout.LayoutParams(px * 2, px * 2)
                        params.addRule(RelativeLayout.ABOVE, R.id.btnWallpaper)
                        params.setMargins(0, 0, 0, 5)
                        params.marginEnd = 8
                        params.addRule(RelativeLayout.ALIGN_PARENT_END)
                        share.layoutParams = params
                        share.setImageResource(R.drawable.home_share)
                        area.addView(share)
                        share.startAnimation(anim)

                        val print = ImageView(mContext)
                        print.id = R.id.btnPrint
                        print.tag = "print"
                        params = RelativeLayout.LayoutParams(px * 2, px * 2)
                        params.addRule(RelativeLayout.ABOVE, R.id.btnShare)
                        params.setMargins(0, 0, 0, 5)
                        params.marginEnd = 8
                        params.addRule(RelativeLayout.ALIGN_PARENT_END)
                        print.layoutParams = params
                        print.setImageResource(R.drawable.home_print)
                        area.addView(print)
                        print.startAnimation(anim)
                    } else {
                        area.removeView(area.findViewWithTag("share"))
                        area.removeView(area.findViewWithTag("print"))
                        area.removeView(area.findViewWithTag("wallpaper"))
                    }
                })

                prevArea = area
            } else {
                area.removeView(imgMask)
                area.removeView(area.findViewWithTag("like"))
                area.removeView(area.findViewWithTag("subtitle"))
                area.removeView(area.findViewWithTag("title"))
                area.removeView(area.findViewWithTag("threeDot"))
                area.removeView(area.findViewWithTag("share"))
                area.removeView(area.findViewWithTag("print"))
                area.removeView(area.findViewWithTag("wallpaper"))
                prevArea = null
            }
        })
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        if (holder.type != mType) {
            val parent = holder.parent.parent as ViewGroup
            val index = parent.indexOfChild(holder.parent)
            parent.removeView(holder.parent)
            val view = LayoutInflater.from(mContext).inflate(mLayouts[mType], parent, false)
            val viewGroup: ConstraintLayout = view.findViewById(R.id.parent)
            for (i in 0 until viewGroup.childCount) {
                val area = viewGroup.getChildAt(i)
                if (area is RelativeLayout) {
                    fillRelative(area)
                }
            }
            parent.addView(view, index)
            bindViewHolder(ImageHolder(view), holder.adapterPosition)
        }
        var index = 0
        for (i in 0 until holder.parent.childCount) {
            val area = holder.parent.getChildAt(i)
            if (area is RelativeLayout && mSizes[mType] * holder.adapterPosition + index < mURLs.size) {
                /* fillWithUrl(area, mURLs[mSizes[mType] * holder.adapterPosition + index],
                         mTitles[mSizes[mType] * holder.adapterPosition + index],
                         mSubtitles[mSizes[mType] * holder.adapterPosition + index++])*/
                val situation :Boolean
                if (mLIKEs!!.contains(mIDs[mSizes[mType] * holder.adapterPosition + index])){
                    situation=true
                }else{
                    situation=false
                }


                fillWithUrl(area, mURLs[mSizes[mType] * holder.adapterPosition + index],
                        mTitles[mSizes[mType] * holder.adapterPosition + index],
                        mSubtitles[mSizes[mType] * holder.adapterPosition + index++],situation,
                        mIDs[mSizes[mType] * holder.adapterPosition + index])
//                mSubtitles[mSizes[mType] * holder.adapterPosition + index++],false,"0")

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mLayouts[mType]
    }

    override fun getItemCount(): Int {
        return ((mURLs.size - 1) / mSizes[mType]) + 1
    }

    fun setIDs(urls: ArrayList<String>, titles: ArrayList<String>, subtitles: ArrayList<String>, ids: ArrayList<String>) {
        for (url in urls)
            mURLs.add(url)
        for (title in titles)
            mTitles.add(title)
        for (subtitle in subtitles)
            mSubtitles.add(subtitle)
        for (id in ids)
            mIDs.add(id)
        notifyDataSetChanged()
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent: ConstraintLayout = itemView.findViewById(R.id.parent)
        val type = mType
    }

    fun changeLike(state: Boolean, position: Int) {
        if (state) {
            //mLIKEs[position]=false
            ///  send like state to server
        } else {
            //mLIKEs[position]=true
            ///  send dislike state to server
        }
    }

    private class send_data_to_server(context: Activity, dialog: ProgressDialog) : AsyncTask<String, String, String>() {
        var result_like: MyJsonObject? = null
        var items_like: MyJsonArray? = null
        var parent_like: WeakReference<Activity> = WeakReference(context)
        val pDialog: WeakReference<ProgressDialog> = WeakReference(dialog)

        override fun doInBackground(vararg p0: String?): String? {

            val params = ArrayList<NameValuePair>()
            val jParser = JSONParser()
            val context: Context? = parent_like.get()
            result_like = MyJsonObject(jParser.makeHttpRequest(context!!.getString(R.string.server_address) +
                    "/api/bookmarks/", "POST", params, context))
//            if (result_like != null && !result_like!!.isNull)
//                items_like = MyJsonArray(result_like!!.getJSONArraySafe("results"))
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pDialog.get()!!.dismiss()

        }
    }

}

