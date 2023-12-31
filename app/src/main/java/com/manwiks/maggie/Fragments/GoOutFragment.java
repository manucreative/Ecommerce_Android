package com.manwiks.maggie.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.navigation.NavigationView;
import com.manwiks.maggie.Adapters.BranchAdapter;
import com.manwiks.maggie.Adapters.ProductAdapter;
import com.manwiks.maggie.MainActivity;
import com.manwiks.maggie.Models.BranchModel;
import com.manwiks.maggie.Models.ProductsModel;
import com.manwiks.maggie.OperationRetrofit.ApiClient;
import com.manwiks.maggie.OperationRetrofit.ApiInterface;
import com.manwiks.maggie.OperationRetrofit.Users;
import com.manwiks.maggie.R;
import com.manwiks.maggie.Sessions.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoOutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoOutFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GoOutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoOutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoOutFragment newInstance(String param1, String param2) {
        GoOutFragment fragment = new GoOutFragment();
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
    private RelativeLayout  relativeLayout3Bookmarks, relativeLayout4Earnings;
    private TextView your_orders, favorite_orders, address, online_order_help, send_feedback, report_safety, rate_us, contact_us,logout;

   // SessionManager sessionManager;

    public static ApiInterface apiInterface;

    //////////branch recyclerview//////////////
    private RecyclerView recyclerViewBranch;
    private BranchAdapter branchAdapter;
    private List<BranchModel> branchModelList;
    ///////////////branch recyclerview////////////

    //////////product recyclerview//////////////
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<ProductsModel> productsModelList;
    ///////////////products recyclerview////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_go_out, container, false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

      //  sessionManager = new SessionManager(getContext());
        onSetNavigationDrawerEvents();
        init();

        return view;
    }
    ////////branch name recycler view start////////
    private void init() {


        recyclerViewBranch = (RecyclerView) view.findViewById(R.id.recyclerViewBranch);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewBranch.setLayoutManager(layoutManager);

        branchModelList = new ArrayList<>();
        Call<Users> branchCall = apiInterface.getBranches();


            branchCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> branchCall, Response<Users> response) {

                branchModelList = response.body().getBranch();
                branchAdapter = new BranchAdapter(branchModelList,getContext());
                recyclerViewBranch.setAdapter(branchAdapter);
                branchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Users> branchCall, Throwable t) {

            }
        });

////////branch name recycler view end////////


        ////////Product recyclerView start/////////

        recyclerViewProducts = (RecyclerView)view.findViewById(R.id.recyclerViewProducts);
        int numberOfColumns = 2;
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        productsModelList = new ArrayList<>();

        productsModelList.add(new ProductsModel(R.drawable.two, "China food", "Ksh 300", "per Kg", "Add to Cat"));
        productsModelList.add(new ProductsModel(R.drawable.two, "China food", "Ksh 300", "per Kg", "Add to Cat"));
        productsModelList.add(new ProductsModel(R.drawable.two, "China food", "Ksh 300", "per Kg", "Add to Cat"));
        productsModelList.add(new ProductsModel(R.drawable.two, "China food", "Ksh 300", "per Kg", "Add to Cat"));
        productsModelList.add(new ProductsModel(R.drawable.two, "China food", "Ksh 300", "per Kg", "Add to Cat"));
        productsModelList.add(new ProductsModel(R.drawable.two, "China food", "Ksh 300", "per Kg", "Add to Cat"));
        productsModelList.add(new ProductsModel(R.drawable.two, "China food", "Ksh 300", "per Kg", "Add to Cat"));

           productAdapter = new ProductAdapter(productsModelList,getContext());
           recyclerViewProducts.setAdapter(productAdapter);
           productAdapter.notifyDataSetChanged();

        ///////Product recycler end///////
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
//                logOut();
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

//    private void logOut() {
//        sessionManager.editor.clear();
//        sessionManager.editor.commit();
//
//        Intent intent = new Intent(getContext(), MainActivity.class);
//        startActivity(intent);
//        getActivity().finish();
//        Animatoo.animateInAndOut(getContext());
//    }

    @Override
    public void onStart() {
        super.onStart();
//        if(!sessionManager.isLogin())
//        {
//
//            sessionManager.editor.clear();
//            sessionManager.editor.commit();
//
//            Intent intent = new Intent(getContext(), MainActivity.class);
//            startActivity(intent);
//            getActivity().finish();
//            Animatoo.animateInAndOut(getContext());
//        }
    }
}
