package com.lydia.digitallibrary.UI;

import android.content.Context;
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
    private int selectedTab;
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
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.home_tab:
                                if(selectedTab == 0){
                                    //if we're already here, do nothing
                                    break;
                                }
                                else if(selectedTab == 1){
                                    //if the prev tab was browse make sure to set icon to unselected again
                                    bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.browse_line_ic);
                                    // as well as setting curr tab to selected
                                    item.setIcon(R.drawable.home_fill_ic);
                                    // + new frag
                                    selectedFragment = new HomeFragmentContainer();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content_frame, selectedFragment);
                                    transaction.commit();
                                }//else if its bookmarked..
                                else {
                                    bottomNavigation.getMenu().getItem(2).setIcon(R.drawable.star_line_ic);
                                    item.setIcon(R.drawable.home_fill_ic);
                                    selectedFragment = new HomeFragmentContainer();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content_frame, selectedFragment);
                                    transaction.commit();
                                }
                                //set curr selected tab
                                selectedTab = 0;
                                break;

                            case R.id.browse_tab:
                                if(selectedTab == 1){
                                    break;
                                }
                                else if(selectedTab == 2){
                                    bottomNavigation.getMenu().getItem(2).setIcon(R.drawable.star_line_ic);
                                    item.setIcon(R.drawable.browse_fill_ic);
                                    selectedFragment = new BrowseFragmentContainer();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content_frame, selectedFragment);
                                    transaction.commit();
                                }
                                else {
                                    bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.home_line_ic);
                                    item.setIcon(R.drawable.browse_fill_ic);
                                    selectedFragment = new BrowseFragmentContainer();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content_frame, selectedFragment);
                                    transaction.commit();
                                }
                                selectedTab = 1;
                                break;

                            case R.id.bookmark_tab:
                                if(selectedTab == 2){
                                    break;
                                }
                                else if(selectedTab == 0){
                                    bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.home_line_ic);
                                    item.setIcon(R.drawable.star_fill_ic);
                                    selectedFragment = new HomeFragmentContainer();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content_frame, selectedFragment);
                                    transaction.commit();
                                }
                                else {
                                    bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.browse_line_ic);
                                    item.setIcon(R.drawable.star_fill_ic);
                                    selectedFragment = new HomeFragmentContainer();
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content_frame, selectedFragment);
                                    transaction.commit();
                                }
                                selectedTab = 2;
                                break;
                        }

                        return true;
                    }
                });



        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, new HomeFragmentContainer());
        transaction.commit();
        //And the nav icon is selected
        bottomNavigation.getMenu().getItem(0).setIcon(R.drawable.home_fill_ic);
        selectedTab = 0;
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

