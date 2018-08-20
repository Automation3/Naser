package com.example.no1.photongallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.no1.photongallery.utils.OnImageClickedListener
import com.mukesh.image_processing.ImageProcessor
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.nio.file.Files.size
import java.util.*

class AutoBGIconAdapter(private val mContext: Context, private var mIDs: ArrayList<String>) : RecyclerView.Adapter<AutoBGIconAdapter.ImageHolder>() {


    var mListener: OnImageClickedListener? = null
    var mStates = arrayOfNulls<Int>(mIDs.size)
    val imageProcessor: ImageProcessor = ImageProcessor()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.auto_bg_four_image,
                parent, false)

        itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

            }
        })

        return ImageHolder(itemView)
    }

    private fun loadAndSave(imageView: ImageView, position: Int, imgTick: ImageView) {
        val t = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom) {
                if (bitmap != null) {
                    mStates[position] = 0
                    imageView.setImageBitmap(bitmap)
                }
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
//                loadDefaultMarker(listener)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        }
        imageView.tag = t
        Picasso.with(mContext).load(mIDs[position]).into(t)
        imageView.setOnClickListener({
            if (mStates[position] == 0) {
                mStates[position] = 1
                imgTick.visibility = View.VISIBLE
            } else {
                mStates[position] = 0
                imgTick.visibility = View.INVISIBLE
            }
        })
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        loadAndSave(holder.imgOne, 4 * position, holder.imgTickOne)

        if (mIDs.size > 4 * position + 1)
            loadAndSave(holder.imgTwo, 4 * position + 1, holder.imgTickTwo)
        else
            holder.imgTickTwo.visibility = View.INVISIBLE

        if (mIDs.size > 4 * position + 2)
            loadAndSave(holder.imgThree, 4 * position + 2, holder.imgTickThree)
        else
            holder.imgTickThree.visibility = View.INVISIBLE

        if (mIDs.size > 4 * position + 3)
            loadAndSave(holder.imgFour, 4 * position + 3, holder.imgTickFour)
        else
            holder.imgTickFour.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return ((mIDs.size - 1) / 4) + 1
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgOne: ImageView = itemView.findViewById(R.id.imgOne)
        var imgTwo: ImageView = itemView.findViewById(R.id.imgTwo)
        var imgThree: ImageView = itemView.findViewById(R.id.imgThree)
        var imgFour: ImageView = itemView.findViewById(R.id.imgFour)
        val imgTickOne: ImageView = itemView.findViewById(R.id.imgTickOne)
        var imgTickTwo: ImageView = itemView.findViewById(R.id.imgTickTwo)
        var imgTickThree: ImageView = itemView.findViewById(R.id.imgTickThree)
        var imgTickFour: ImageView = itemView.findViewById(R.id.imgTickFour)

    }
    //////
    fun getRandom(array: IntArray): Int {
        val rnd = Random().nextInt(array.size)
        return array[rnd]
    }

    fun array_of_clicked_item(){


    }


}




