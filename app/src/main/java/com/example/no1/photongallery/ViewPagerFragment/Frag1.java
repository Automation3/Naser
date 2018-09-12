package com.example.no1.photongallery.ViewPagerFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.no1.photongallery.R;

import java.util.ArrayList;


public class Frag1 extends Fragment {

    private  ArrayList<String> titles ;
    private  ArrayList<String> iDs;
    private  ArrayList<String> subtitles ;
    private  ArrayList<String> mLikes;
    private  ArrayList<String> temp;



    public static Frag1 newInstance( ArrayList<String> titles, ArrayList<String> subtitles, ArrayList<String> iDs,
                             ArrayList<String> temp, ArrayList<String> mLikes) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("titles", titles);
        bundle.putStringArrayList("subtitles", subtitles);
        bundle.putStringArrayList("iDs", iDs);
        bundle.putStringArrayList("temp", temp);
        bundle.putStringArrayList("mLikes", mLikes);

        Frag1 fragment = new Frag1();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            titles = bundle.getStringArrayList("titles");
            subtitles = bundle.getStringArrayList("subtitles");
            iDs = bundle.getStringArrayList("iDs");
            temp = bundle.getStringArrayList("temp");
            mLikes = bundle.getStringArrayList("mLikes");}
    }



    public Frag1() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag1, container, false);

        return view;
    }

}
