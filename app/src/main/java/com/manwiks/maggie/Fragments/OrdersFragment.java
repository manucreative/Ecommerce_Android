package com.manwiks.maggie.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.manwiks.maggie.Adapters.BannerAdapter;
import com.manwiks.maggie.Adapters.CatAdapter;
import com.manwiks.maggie.Adapters.DetailsVerticalAdapter;
import com.manwiks.maggie.Adapters.GreatOffersAdapter;
import com.manwiks.maggie.CartActivity;
import com.manwiks.maggie.Database.Local.CartDAO;
import com.manwiks.maggie.Database.ModelDB.Cart;
import com.manwiks.maggie.MainActivity;
import com.manwiks.maggie.Models.BannerModel;
import com.manwiks.maggie.Models.CategoryModel;
import com.manwiks.maggie.Models.DetailsVerticalModel;
import com.manwiks.maggie.Models.GreatOffersModel;
import com.manwiks.maggie.OperationRetrofit.ApiClient;
import com.manwiks.maggie.OperationRetrofit.ApiInterface;
import com.manwiks.maggie.OperationRetrofit.Users;
import com.manwiks.maggie.R;
import com.manwiks.maggie.RetroTwo.Common;
import com.manwiks.maggie.Sessions.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrdersFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
    ImageView navigationBar,strip_banner_image, add_to_cart;
    NavigationView navigationView;
    private View view;
    private RelativeLayout  relativeLayout3Bookmarks, relativeLayout4Earnings;
    private TextView your_orders, favorite_orders, address, online_order_help, send_feedback, report_safety, rate_us, contact_us, logout, txtCartIcon;
    CompositeDisposable compositeDisposable;

   // SessionManager sessionManager;

    public static ApiInterface apiInterface;


    //////////category slider start////////
    private RecyclerView recyclerViewCategory;
    private CatAdapter catAdapter;
    private List<CategoryModel> categoryModelList;
    ///////category slider end////////////
    /////////////////banner slider start///////////////////

    private ViewPager2 viewPagerBanner;
    private BannerAdapter bannerAdapter;
    private  List<BannerModel> bannerModelList;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 3000; // Delay between auto-scrolling (3 seconds in this case)
    final long PERIOD_MS = 6000; // Time period of auto-scrolling (10 seconds in this case)
    FloatingActionButton float_cart;

    //////////////////banner slider end////////////////////

    //////////Details Vertical start//////////////

    private RecyclerView recyclerViewDetails;
    private DetailsVerticalAdapter detailsVerticalAdapter;
    private List<DetailsVerticalModel> detailsVerticalModelList;
    ///////////////Details vertical end////////////

    /////great offers horizontal start////////

    private  RecyclerView greatOffersHorizontal;
    private GreatOffersAdapter greatOffersAdapter;
   private List<GreatOffersModel> greatOffersModelList;
    ////great offers horizontal ed/////////

    /////great offers start////////
    private  RecyclerView greatOffersRecyclerViewVertical;
    ////great offers  end/////////

    /////new arrival horizontal start////////
    private  RecyclerView newArrivalHorizontalRecyclerView;
    ////new arrival horizontal end/////////

    /////new arrival vertical start////////
    private  RecyclerView newArrivalVerticalRecyclerView;
    ////new arrival vertical  end/////////

    /////exclusive horizontal start////////
    private  RecyclerView exclusiveHorizontalRecyclerView;
    ////exclusive horizontal end/////////

    /////exclusive vertical start////////
    private  RecyclerView exclusiveVerticalRecyclerView;
    ////exclusive vertical  end/////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_orders, container, false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        compositeDisposable = new CompositeDisposable();

       // sessionManager = new SessionManager(getContext());

        onSetNavigationDrawerEvents();
        init();
        txtCartIcon = view.findViewById(R.id.cart_qty);
        Common.cartRepository.getCartItemsLiveData().observe(this, cartItems->{
            int totalQty = Common.cartRepository.getCountItems();
            txtCartIcon.setText(String.valueOf(totalQty));
        });

        float_cart = view.findViewById(R.id.float_cart);
        float_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
                Animatoo.animateFade(getActivity());
            }
        });
        return view;

    }
//////////category model/////////
    private void init() {

        //////////////////Strip Banners start///////////////////////////
        strip_banner_image= (ImageView)view.findViewById(R.id.strip_banner_image);
        Call<Users> stripCall = apiInterface.getStripBanners();
        stripCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> stripCall, Response<Users> response) {

                Glide.with(getContext()).load(response.body().getStrip_image()).placeholder(R.drawable.two).into(strip_banner_image);
            }

            @Override
            public void onFailure(Call<Users> stripCall, Throwable t) {

            }
        });
        ////////////////// Banners end///////////////////////////
        recyclerViewCategory = (RecyclerView)view.findViewById(R.id.recyclerViewCategory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
      layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewCategory.setLayoutManager(layoutManager);

      categoryModelList = new ArrayList<>();
        Call<Users> categoryCall = apiInterface.getCategory();

        categoryCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                categoryModelList = response.body().getCategory();
                catAdapter = new CatAdapter(categoryModelList,getContext());
                recyclerViewCategory.setAdapter(catAdapter);
                catAdapter.notifyDataSetChanged();
                autoScroll();
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

            }
        });
        //////////category model////////////////
       /*==========================================================================================*/

        //////////banner model////////////////
        viewPagerBanner = view.findViewById(R.id.viewPagerBanner);
        bannerModelList = new ArrayList<>();
        bannerAdapter = new BannerAdapter(bannerModelList, getContext());
        viewPagerBanner.setAdapter(bannerAdapter);
        startAutoScroll();

        Call<Users> bannerCall = apiInterface.getBanner();
        bannerCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    bannerModelList = response.body().getBanner();
                    bannerAdapter.setData(bannerModelList);
                    bannerAdapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onFailure(Call<Users> bannerCall, Throwable t) {
            }
        });

        ////////////banner model end////////
/*====================================================================================================*/

        ///////////Details Vertical view///////////////////
        recyclerViewDetails = (RecyclerView)view.findViewById(R.id.recyclerViewDetails);
        LinearLayoutManager layoutManagerDetails = new LinearLayoutManager(getContext());
        layoutManagerDetails.setOrientation(RecyclerView.VERTICAL);
        recyclerViewDetails.setLayoutManager(layoutManagerDetails);

        detailsVerticalModelList = new ArrayList<>();
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | Rider Vegetables buy now","3.5"));

       detailsVerticalAdapter = new DetailsVerticalAdapter(detailsVerticalModelList,getContext());
        recyclerViewDetails.setAdapter(detailsVerticalAdapter);
        detailsVerticalAdapter.notifyDataSetChanged();
        //////////// details vertical view end/////////////////
        /*====================================================================================================*/

        /*====================================================================================================*/

        ///////////Great offers horizontal view///////////////////
        greatOffersHorizontal = (RecyclerView)view.findViewById(R.id.recyclerViewOffers);
        LinearLayoutManager layoutManagerOffers = new LinearLayoutManager(getContext());
        layoutManagerOffers.setOrientation(RecyclerView.HORIZONTAL);
        greatOffersHorizontal.setLayoutManager(layoutManagerOffers);

       greatOffersModelList = new ArrayList<>();
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","12 hrs day time","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","12 hrs day time","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","12 hrs day time","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","12 hrs day time","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","12 hrs day time","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","12 hrs day time","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","12 hrs day time","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","12 hrs day time","30% OFF","3.5"));

        greatOffersAdapter = new GreatOffersAdapter(greatOffersModelList,getContext());
        greatOffersHorizontal.setAdapter(greatOffersAdapter);
        greatOffersAdapter.notifyDataSetChanged();
        //////////// great offers horizontal view end/////////////////

        ///////////Great offers view///////////////////
        greatOffersRecyclerViewVertical = (RecyclerView)view.findViewById(R.id.greatOffersRecyclerViewVertical);
        LinearLayoutManager layoutManagerGreatOffers = new LinearLayoutManager(getContext());
        layoutManagerGreatOffers.setOrientation(RecyclerView.VERTICAL);
        greatOffersRecyclerViewVertical.setLayoutManager(layoutManagerGreatOffers);

        detailsVerticalModelList = new ArrayList<>();
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | green Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | green Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | green Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | green Vegetables buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well chopped | green Vegetables buy now","3.5"));


        detailsVerticalAdapter = new DetailsVerticalAdapter(detailsVerticalModelList,getContext());
        greatOffersRecyclerViewVertical.setAdapter(detailsVerticalAdapter);
        detailsVerticalAdapter.notifyDataSetChanged();
        ////////////Great offers view end/////////////////

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//

        ///////////New Arrivals horizontal view///////////////////
        newArrivalHorizontalRecyclerView = (RecyclerView)view.findViewById(R.id.newArrivalHorizontalRecyclerView);
        LinearLayoutManager layoutManagerNewArrival = new LinearLayoutManager(getContext());
        layoutManagerNewArrival.setOrientation(RecyclerView.HORIZONTAL);
        newArrivalHorizontalRecyclerView.setLayoutManager(layoutManagerNewArrival);

        greatOffersModelList = new ArrayList<>();
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));

        greatOffersAdapter = new GreatOffersAdapter(greatOffersModelList,getContext());
        newArrivalHorizontalRecyclerView.setAdapter(greatOffersAdapter);
        greatOffersAdapter.notifyDataSetChanged();
        //////////// new Arrival horizontal view end/////////////////

        ///////////new Arrival Viertical view///////////////////
        newArrivalVerticalRecyclerView = (RecyclerView)view.findViewById(R.id.newArrivalVerticalRecyclerView);
        LinearLayoutManager layoutManagerNewArrivalVertical = new LinearLayoutManager(getContext());
        layoutManagerNewArrivalVertical.setOrientation(RecyclerView.VERTICAL);
        newArrivalVerticalRecyclerView.setLayoutManager(layoutManagerNewArrivalVertical);

        detailsVerticalModelList = new ArrayList<>();
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Fruits","Greenish and yellowish healthy fruits","NO DISCOUNTS","Ksh. 100 per 3 fruits","Well chopped | Rider fruits buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Fruits","Greenish and yellowish healthy fruits","NO DISCOUNTS","Ksh. 100 per 3 fruits","Well chopped | Rider fruits buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Fruits","Greenish and yellowish healthy fruits","NO DISCOUNTS","Ksh. 100 per 3 fruits","Well chopped | Rider fruits buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Fruits","Greenish and yellowish healthy fruits","NO DISCOUNTS","Ksh. 100 per 3 fruits","Well chopped | Rider fruits buy now","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Fruits","Greenish and yellowish healthy fruits","NO DISCOUNTS","Ksh. 100 per 3 fruits","Well chopped | Rider fruits buy now","3.5"));


        detailsVerticalAdapter = new DetailsVerticalAdapter(detailsVerticalModelList,getContext());
        newArrivalVerticalRecyclerView.setAdapter(detailsVerticalAdapter);
        detailsVerticalAdapter.notifyDataSetChanged();
        ////////////new Arrival Virtical horizontl view end/////////////////

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//

        ///////////exclusive  horizontal view///////////////////
        exclusiveHorizontalRecyclerView = (RecyclerView)view.findViewById(R.id.exclusiveHorizontalRecyclerView);
        LinearLayoutManager layoutManagerHorizontalExclusive = new LinearLayoutManager(getContext());
        layoutManagerHorizontalExclusive.setOrientation(RecyclerView.HORIZONTAL);
        exclusiveHorizontalRecyclerView.setLayoutManager(layoutManagerHorizontalExclusive);

        greatOffersModelList = new ArrayList<>();
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));
        greatOffersModelList.add(new GreatOffersModel(R.drawable.two,"Vegetable juction","24 hrs service","30% OFF","3.5"));

        greatOffersAdapter = new GreatOffersAdapter(greatOffersModelList,getContext());
        exclusiveHorizontalRecyclerView.setAdapter(greatOffersAdapter);
        greatOffersAdapter.notifyDataSetChanged();
        ////////////exclusive horizontal view end/////////////////

        ///////////Exclusive  Vertical view///////////////////
        exclusiveVerticalRecyclerView = (RecyclerView)view.findViewById(R.id.exclusiveVerticalRecyclerView);
        LinearLayoutManager layoutManagerVerticalExclusive = new LinearLayoutManager(getContext());
        layoutManagerVerticalExclusive.setOrientation(RecyclerView.VERTICAL);
        exclusiveVerticalRecyclerView.setLayoutManager(layoutManagerVerticalExclusive);

        detailsVerticalModelList = new ArrayList<>();
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well sanitized  | Rider hand wash","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well sanitized  | Rider hand wash","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well sanitized  | Rider hand wash","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well sanitized  | Rider hand wash","3.5"));
        detailsVerticalModelList.add(new DetailsVerticalModel(R.drawable.two,"Spices","Greenish and yellowish healthy food","10% DISCOUNT ALLOWED","Ksh. 100 per kg|2hrs","Well sanitized  | Rider hand wash","3.5"));


        detailsVerticalAdapter = new DetailsVerticalAdapter(detailsVerticalModelList,getContext());
        exclusiveVerticalRecyclerView.setAdapter(detailsVerticalAdapter);
        detailsVerticalAdapter.notifyDataSetChanged();
        ////////////exclusive Vertical view end/////////////////
    }

    public void autoScroll() {
        final int speedScroll = 2500;
        final Handler handle = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            int direction = 1; // 1 for forward, -1 for backward

            @Override
            public void run() {
                if (count >= catAdapter.getItemCount() - 1) {
                    direction = -1; // Change direction to scroll backward
                } else if (count <= 0) {
                    direction = 1; // Change direction to scroll forward
                }

                count += direction;

                recyclerViewCategory.smoothScrollToPosition(count);
                handle.postDelayed(this, speedScroll);
            }
        };
        handle.postDelayed(runnable, speedScroll);
    }

    private void startAutoScroll() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == bannerModelList.size()) {
                    currentPage = 0;
                }
                viewPagerBanner.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private void onSetNavigationDrawerEvents() {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) view.findViewById(R.id.navigationView);

        navigationBar = (ImageView) view.findViewById(R.id.navigationBar);
    //    add_to_cart = (ImageView)view.findViewById(R.id.icon_cart);



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

//        add_to_cart.setOnClickListener(this);
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
            case R.id.icon_cart:
//              Intent intent = new Intent(getActivity(),CartActivity.class);
//                 startActivity(intent);

            case R.id.navigationBar:
                drawerLayout.openDrawer(navigationView, true);
                break;
            case R.id.logOut:
                //logOut();
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAutoScroll();
    }

    private void stopAutoScroll() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    }
