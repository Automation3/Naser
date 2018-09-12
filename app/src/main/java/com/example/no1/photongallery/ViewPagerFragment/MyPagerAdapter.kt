package com.example.no1.photongallery.ViewPagerFragment


import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

// 1
class MyPagerAdapter(fragmentManager: FragmentManager, context: Context, titles2:ArrayList<String>,
                     subtitles2:ArrayList<String>, iDs2:ArrayList<String>, temp2:ArrayList<String>,
                     mLikes2: ArrayList<String>) : FragmentStatePagerAdapter(fragmentManager) {

    private val COUNT = 6
    private var titles=titles2
    private var subtitles=subtitles2
    private var iDs=iDs2
    private var temp=temp2
    private var mLikes=mLikes2



    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null

        when (position) {
        //    0 -> fragment = BlankFragment.newInstance(movies[position])
//            0 -> fragment = Frag2(context, titles, subtitles, iDs, temp, mLikes)

            0 -> fragment = Frag1.newInstance(titles,subtitles,iDs, temp, mLikes)
            1 -> fragment = Frag2.newInstance(titles,subtitles,iDs, temp, mLikes)
            2 -> fragment = Frag3()
            3 -> fragment = Frag4()
            4 -> fragment = Frag5()
            5 -> fragment = Frag6()
        }

        return fragment
    }

    override fun getCount(): Int {
        return COUNT
    }

}
