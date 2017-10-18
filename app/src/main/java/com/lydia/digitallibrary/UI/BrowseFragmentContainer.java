/**
 * Created by Lydia on 23/06/2016.
 */

package com.lydia.digitallibrary.UI;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lydia.digitallibrary.Helper.GlobalVariables;
import com.lydia.digitallibrary.R;

public class BrowseFragmentContainer extends Fragment {

    public static final String TAG = "BrowseFragmentContainer";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.browse_container);

        //initialize arrays on opening of app
        GlobalVariables.setPreviewArray();
        Log.d("tagtag", GlobalVariables.getPreviewArray().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                        Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.browse_container, container, false);
        rootView.setTag(TAG);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        BrowseFragment bFragment = new BrowseFragment();

        transaction.replace(R.id.fragment_container, bFragment);
        transaction.commit();

        return rootView;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        GlobalVariables.clearPreviewArray();
    }


}


