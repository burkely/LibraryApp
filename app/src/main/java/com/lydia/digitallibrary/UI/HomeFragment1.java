package com.lydia.digitallibrary.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lydia.digitallibrary.Helper.AppConstants;
import com.lydia.digitallibrary.Helper.GlobalVariables;
import com.lydia.digitallibrary.R;

/**
 * Created by Lydia on 28/07/2016.
 */
public class HomeFragment1 extends Fragment {

    private static final String TAG = "HomeFragment1";

    private RecyclerView popularListView;
    private RecyclerView recommendedListView;
    private RecyclerView featuredListView;

    private ItemRecyclerViewAdapter horizontalAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager1;
    private LinearLayoutManager layoutManager2;

    public HomeFragment1(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_fragment1, container, false);
        rootView.setTag(TAG);


        //Get recyclerViews
        featuredListView = (RecyclerView) rootView.findViewById(R.id.browseFeatured);
        recommendedListView = (RecyclerView) rootView.findViewById(R.id.browseRecommended);
        popularListView = (RecyclerView) rootView.findViewById(R.id.browsePopular);
        //assign new layout manager
        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager1 = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager2 = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);

        featuredListView.setLayoutManager(layoutManager);
        recommendedListView.setLayoutManager(layoutManager1);
        popularListView.setLayoutManager(layoutManager2);

        //Get & Set adapters
        horizontalAdapter = new ItemRecyclerViewAdapter(getActivity(),
                GlobalVariables.getDocumentsRetrieved(), AppConstants.CARD_GRID);
        horizontalAdapter.setHasStableIds(true);

        featuredListView.setAdapter(horizontalAdapter);
        recommendedListView.setAdapter(horizontalAdapter);
        popularListView.setAdapter(horizontalAdapter);

        return rootView;
    }
}
