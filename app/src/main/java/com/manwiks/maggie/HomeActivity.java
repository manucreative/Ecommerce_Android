package com.manwiks.maggie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.manwiks.maggie.Database.DataSource.CartRepository;
import com.manwiks.maggie.Database.DataSource.FavoriteRepository;
import com.manwiks.maggie.Database.Local.CartDataSource;
import com.manwiks.maggie.Database.Local.FavoriteDataSource;
import com.manwiks.maggie.Database.Local.MaggieRoomDatabase;
import com.manwiks.maggie.Fragments.GoOutFragment;
import com.manwiks.maggie.Fragments.GoldFragment;
import com.manwiks.maggie.Fragments.OrdersFragment;
import com.manwiks.maggie.Fragments.VideosFragment;
import com.manwiks.maggie.RetroTwo.Common;


public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);

        ///initialize database
        onitDB();

    }

    private void onitDB() {
        Common.maggieRoomDatabase = MaggieRoomDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.maggieRoomDatabase.cartDAO()));
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.maggieRoomDatabase.FavoriteDAO()));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigation =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;

                    switch(item.getItemId())
                    {
                        case R.id.oders:
                        selectedFragment = new OrdersFragment();
                            break;

                        case R.id.goOut:
                           selectedFragment = new GoOutFragment();
                            break;

                        case R.id.gold:
                           selectedFragment = new GoldFragment();
                            break;

                        case R.id.videos:
                           selectedFragment = new VideosFragment();
                            break;

                    }

                    //////////Replacing by default fragment on home activity////////////
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                            selectedFragment).commit();
                    return true;
                }
            };
}