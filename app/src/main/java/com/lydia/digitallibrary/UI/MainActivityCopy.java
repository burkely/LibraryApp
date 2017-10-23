package com.lydia.digitallibrary.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lydia.digitallibrary.R;

public class MainActivityCopy extends AppCompatActivity {

    private SearchView searchView;

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView toolbarTitle = (TextView)toolbar.findViewById(R.id.toolbarTextView);
                toolbarTitle.setVisibility(View.GONE);
                LinearLayout searchPlate = (LinearLayout) searchView.findViewById(R.id.search_plate);
                searchPlate.setBackgroundColor(0xffffffff);
            }});
            */

        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.home_tab:
                                selectedFragment = new HomeFragmentContainer();
                                break;
                            case R.id.browse_tab:
                                selectedFragment = new BrowseFragmentContainer();
                                break;
                            case R.id.bookmark_tab:
                                selectedFragment = new HomeFragmentContainer1();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_frame, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });


        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, new HomeFragmentContainer());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigation.getMenu().getItem(0).setChecked(true);
    }

    /*void LoadFragment(int id)
    {
        Android.Support.V4.App.Fragment fragment = null;
        switch (id)
        {
            case Resource.Id.menu_home:
                fragment = Fragment1.NewInstance();
                break;
            case Resource.Id.menu_audio:
                fragment = Fragment2.NewInstance();
                break;
            case Resource.Id.menu_video:
                fragment = Fragment3.NewInstance();
                break;
        }

        if (fragment == null)
            return;

        SupportFragmentManager.BeginTransaction()
                .Replace(Resource.Id.content_frame, fragment)
                .Commit();
    }
*/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
       // SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        //searchView.setMinimumHeight(55);


        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setOnQueryTextListener(this);


        return super.onCreateOptionsMenu(menu);
    }
}

