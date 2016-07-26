/**
 * Created by Lydia on 23/06/2016.
 */

package com.lydia.digitallibrary;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.Window;


public class BrowseFragmentActivity extends FragmentActivity {

    public static final String TAG = "BrowseFragmentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_activity);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            BrowseFragment bFragment = new BrowseFragment();

            //ItemViewFragment viewFragment = new ItemViewFragment();

            transaction.add(R.id.fragment_container, bFragment);
            transaction.commit();
        }

        //initialize arrays on opening of app
        GlobalVariables.setPreviewArray();
        Log.d("tagtag", GlobalVariables.getPreviewArray().toString());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        GlobalVariables.clearPreviewArray();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}


