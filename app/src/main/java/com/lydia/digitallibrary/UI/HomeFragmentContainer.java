/**
 * Created by Lydia on 23/06/2016.
 */

package com.lydia.digitallibrary.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lydia.digitallibrary.R;

public class HomeFragmentContainer extends Fragment {

    public static final String TAG = "HomeFragmentContainer";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.browse_container);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.home_container, container, false);
        rootView.setTag(TAG);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        HomeFragment hFragment = new HomeFragment();

        transaction.replace(R.id.home_fragment_container, hFragment);

        transaction.commit();

        return rootView;
    }

}


