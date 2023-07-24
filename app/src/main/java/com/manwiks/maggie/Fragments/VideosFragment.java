package com.manwiks.maggie.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.manwiks.maggie.Adapters.MediaRecyclerAdapter;
import com.manwiks.maggie.MainActivity;
import com.manwiks.maggie.Models.MediaObject;
import com.manwiks.maggie.R;
import com.manwiks.maggie.Sessions.SessionManager;
import com.manwiks.maggie.ui.ExoPlayerRecyclerView;
import com.manwiks.maggie.utils.DividerItemDecoration;

import static android.widget.LinearLayout.VERTICAL;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideosFragment newInstance(String param1, String param2) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    DrawerLayout drawerLayout;
    ImageView navigationBar;
    NavigationView navigationView;
    private View view;
    private RelativeLayout relativeLayout3Bookmarks, relativeLayout4Earnings;
    private TextView your_orders, favorite_orders, address, online_order_help, send_feedback, report_safety, rate_us, contact_us,logout;

    SessionManager sessionManager;


    ///video recyclerView/////
    ExoPlayerRecyclerView mRecyclerView;

    private ArrayList<MediaObject> mediaObjectList = new ArrayList<>();
    private MediaRecyclerAdapter mAdapter;
    private boolean firstTime = true;
    ///video recyclerview/////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_videos, container, false);

        sessionManager = new SessionManager(getContext());
        onSetNavigationDrawerEvents();
        initView();
        // Prepare demo content
        prepareVideoList();

        //set data object
        mRecyclerView.setMediaObjects(mediaObjectList);
        mAdapter = new MediaRecyclerAdapter(mediaObjectList, initGlide());

        //Set Adapter
        mRecyclerView.setAdapter(mAdapter);

        if (firstTime) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.playVideo(false);
                }
            });
            firstTime = false;
        }
        return view;
    }


    @SuppressLint("WrongConstant")
    private void initView() {
        mRecyclerView = view.findViewById(R.id.exoPlayerRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL, false));
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.devider_drawable);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    @Override
    public void onDestroy() {
        if (mRecyclerView != null) {
            mRecyclerView.releasePlayer();
        }
        super.onDestroy();
    }
    private void prepareVideoList() {

        MediaObject mediaObject = new MediaObject();
        mediaObject.setId(1);
        mediaObject.setUserHandle("User 1");
        mediaObject.setTitle(
                "Exclusive product shows available?");
        mediaObject.setCoverUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-1.png");
        mediaObject.setUrl("http://192.168.43.219:3336/ecom/admin/ajax/cat_videos/shopping.mp4");

        MediaObject mediaObject2 = new MediaObject();
        mediaObject2.setId(2);
        mediaObject2.setUserHandle("user 2");
        mediaObject2.setTitle(
                "Vegetables inside the store ");
        mediaObject2.setCoverUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-1.png");
        mediaObject2.setUrl("http://192.168.43.219:3336/ecom/admin/ajax/cat_videos/web3.mp4");

        MediaObject mediaObject3 = new MediaObject();
        mediaObject3.setId(3);
        mediaObject3.setUserHandle("admin");
        mediaObject3.setTitle("try those Fruits now they are fresh and sweet");
        mediaObject3.setCoverUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-1.png");
        mediaObject3.setUrl("http://192.168.43.219:3336/ecom/admin/ajax/cat_videos/web1.mp4");

        MediaObject mediaObject4 = new MediaObject();
        mediaObject4.setId(4);
        mediaObject4.setUserHandle("admin");
        mediaObject4.setTitle("Cereals the best prices ever");
        mediaObject4.setCoverUrl(
                "https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-4.png");
        mediaObject4.setUrl("https://androidwave.com/media/androidwave-video-6.mp4");


        mediaObjectList.add(mediaObject);
        mediaObjectList.add(mediaObject2);
        mediaObjectList.add(mediaObject3);
        mediaObjectList.add(mediaObject4);
        mediaObjectList.add(mediaObject);
        mediaObjectList.add(mediaObject2);
        mediaObjectList.add(mediaObject3);
        mediaObjectList.add(mediaObject4);
    }


    private void onSetNavigationDrawerEvents() {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) view.findViewById(R.id.navigationView);

        navigationBar = (ImageView) view.findViewById(R.id.navigationBar);



        logout = (TextView) view.findViewById(R.id.logOut);
        relativeLayout3Bookmarks = (RelativeLayout) view.findViewById(R.id.relativeLayout3);
        relativeLayout4Earnings = (RelativeLayout) view.findViewById(R.id.relativeLayout4);

        your_orders = (TextView) view.findViewById(R.id.your_orders);
        favorite_orders = (TextView) view.findViewById(R.id.favorite_orders);
        address = (TextView) view.findViewById(R.id.address);
        online_order_help = (TextView) view.findViewById(R.id.online_order_help);
        send_feedback = (TextView) view.findViewById(R.id.send_feedback);
        report_safety = (TextView) view.findViewById(R.id.report_safety);
        rate_us = (TextView) view.findViewById(R.id.rate_us);
        contact_us = (TextView) view.findViewById(R.id.contact_us);

        navigationBar.setOnClickListener(this);
        logout.setOnClickListener(this);
        relativeLayout3Bookmarks.setOnClickListener(this);
        relativeLayout4Earnings.setOnClickListener(this);

        your_orders.setOnClickListener(this);
        favorite_orders.setOnClickListener(this);
        address.setOnClickListener(this);
        online_order_help.setOnClickListener(this);
        send_feedback.setOnClickListener(this);
        report_safety.setOnClickListener(this);
        rate_us.setOnClickListener(this);
        contact_us.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigationBar:
                drawerLayout.openDrawer(navigationView, true);
                break;
            case R.id.logOut:
                logOut();
                break;
            case R.id.relativeLayout3:
                Toast.makeText(getContext(), "Bookmarks", Toast.LENGTH_SHORT).show();
                break;
            case R.id.relativeLayout4:
                Toast.makeText(getContext(), "Earnings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.your_orders:
                Toast.makeText(getContext(), "Orders", Toast.LENGTH_SHORT).show();
                break;
            case R.id.favorite_orders:
                Toast.makeText(getContext(), "favorite", Toast.LENGTH_SHORT).show();
                break;
            case R.id.address:
                Toast.makeText(getContext(), "Address Book", Toast.LENGTH_SHORT).show();
                break;
            case R.id.online_order_help:
                Toast.makeText(getContext(), "online order help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.send_feedback:
                Toast.makeText(getContext(), "Send feedback", Toast.LENGTH_SHORT).show();
                break;
            case R.id.report_safety:
                Toast.makeText(getContext(), "Report safety", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rate_us:
                Toast.makeText(getContext(), "Rate us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact_us:
                Toast.makeText(getContext(), "Contact us", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void logOut() {
        sessionManager.editor.clear();
        sessionManager.editor.commit();

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        Animatoo.animateInAndOut(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!sessionManager.isLogin())
        {

            sessionManager.editor.clear();
            sessionManager.editor.commit();

            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
            Animatoo.animateInAndOut(getContext());
        }
    }
}
