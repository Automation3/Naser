package com.example.no1.photongallery.ViewPagerFragment

import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.no1.photongallery.R

class BlankFragment : Fragment() {

    // Store instance variables
    private var title: String? = null
    private var page: Int = 0


    // newInstance constructor for creating fragment with arguments
    fun newInstance(page: Int, title: String): BlankFragment {
        val fragmentFirst = BlankFragment()
        val args = Bundle()
        args.putInt("someInt", page)
        args.putString("someTitle", title)
        fragmentFirst.setArguments(args)
        return fragmentFirst
    }

    // Store instance variables based on arguments passed
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

}
