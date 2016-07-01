/**
 * Created by Lydia on 23/06/2016.
 */

package com.lydia.digitallibrary;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import android.view.Menu;


public class MainFragmentActivity extends FragmentActivity {

    public static final String TAG = "MainFragmentActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BrowseFragment bFragment = new BrowseFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            bFragment.setArguments(getIntent().getExtras());

            transaction.add(R.id.fragment_container, bFragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}


