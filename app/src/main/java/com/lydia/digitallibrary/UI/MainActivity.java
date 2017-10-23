package com.lydia.digitallibrary.UI;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lydia.digitallibrary.R;
import com.lydia.digitallibrary.UI.BrowseFragmentContainer;
import com.lydia.digitallibrary.UI.HomeFragmentContainer;
import com.lydia.digitallibrary.UI.HomeFragmentContainer1;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;

    private int[] tabIcons = {
            R.mipmap.home_icon,
            R.mipmap.browse_docs_icon,
            R.mipmap.bookmarked_icon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_copy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (SearchView) toolbar.findViewById(R.id.mySearchView);


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView toolbarTitle = (TextView)toolbar.findViewById(R.id.toolbarTextView);
                toolbarTitle.setVisibility(View.GONE);
                LinearLayout searchPlate = (LinearLayout) searchView.findViewById(R.id.search_plate);
                searchPlate.setBackgroundColor(0xffffffff);

            }});


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new HomeFragmentContainer();
            }else if(position == 1) {
                return new BrowseFragmentContainer();
            }else{
                return new HomeFragmentContainer1();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }


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

