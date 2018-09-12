package com.example.no1.photongallery.ViewPagerFragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.no1.photongallery.ImageRecycleAdapter
import com.example.no1.photongallery.MainActivity

import com.example.no1.photongallery.R


class Frag2 : Fragment() {

    var mAdapter: ImageRecycleAdapter? = null
    var mLikes = ArrayList<String>()
    var temp = ArrayList<String>()
    var titles = ArrayList<String>()
    var subtitles = ArrayList<String>()
    var iDs = ArrayList<String>()



    fun Frag2(context: Context, titles:ArrayList<String>,
              subtitles:ArrayList<String>, iDs:ArrayList<String>,
              temp:ArrayList<String>, mLikes: ArrayList<String>) {

        this.mLikes = mLikes
        this.temp = temp
        this.titles = titles
        this.subtitles = subtitles
        this.iDs = iDs
//        this.context = context
    }

    fun Frag2() {

    }

    companion object {
        fun newInstance(titles: java.util.ArrayList<String>, subtitles: java.util.ArrayList<String>, iDs: java.util.ArrayList<String>,
                        temp: java.util.ArrayList<String>, mLikes: java.util.ArrayList<String>): Frag2 {
            val bundle = Bundle()
            bundle.putStringArrayList("titles", titles)
            bundle.putStringArrayList("subtitles", subtitles)
            bundle.putStringArrayList("iDs", iDs)
            bundle.putStringArrayList("temp", temp)
            bundle.putStringArrayList("mLikes", mLikes)

            val fragment = Frag2()
            fragment.arguments = bundle

            return fragment
        }
    }


    private fun readBundle(bundle: Bundle?) {
        if (bundle != null) {
            titles = bundle.getStringArrayList("titles")
            subtitles = bundle.getStringArrayList("subtitles")
            iDs = bundle.getStringArrayList("iDs")
            temp = bundle.getStringArrayList("temp")
            mLikes = bundle.getStringArrayList("mLikes")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_frag2, container, false)

        readBundle(getArguments())

        val mRecyclerView = view.findViewById<RecyclerView>(R.id.grdCollection2)
        val llm = LinearLayoutManager(context)
        mRecyclerView.layoutManager = llm
        if (mAdapter == null) {
//                        context.mAdapter = ImageRecycleAdapter(context, temp, 0, titles, subtitles)
            if (mLikes == null)
                mLikes = ArrayList<String>()
            mAdapter = ImageRecycleAdapter(context!!, temp, 0, titles, subtitles, mLikes, iDs)
            mRecyclerView.adapter =mAdapter
//            mAdapter!!.mListener = context
            mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                    context.totalItemCount = llm.itemCount
//                    context.lastVisibleItem = llm.findLastVisibleItemPosition()
//                    if (context.totalItemCount <= context.lastVisibleItem + context.visibleThreshold
//                            && context.mNext.isNotEmpty() && context.mNext != "null") {
//                        MainActivity.LoadPics(context).execute(context.mNext)
//                    }
                }
            })
        } else
            mAdapter!!.setIDs(temp, titles, subtitles, iDs)
        return view
    }

}
