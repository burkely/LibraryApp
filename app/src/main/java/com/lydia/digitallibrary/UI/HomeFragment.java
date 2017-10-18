package com.lydia.digitallibrary.UI;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lydia.digitallibrary.Helper.AppConstants;
import com.lydia.digitallibrary.Helper.GlobalVariables;
import com.lydia.digitallibrary.R;

/**
 * Created by Lydia on 28/07/2016.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private RecyclerView popularListView;
    private RecyclerView recommendedListView;
    private RecyclerView featuredListView;

    private ItemRecyclerViewAdapter horizontalAdapter;
    private ItemRecyclerViewAdapter horizontalAdapter1;
    private ItemRecyclerViewAdapter horizontalAdapter2;

    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager1;
    private LinearLayoutManager layoutManager2;

    private FloatingActionButton seeMoreAbout;

    private TextView aboutContent;

    public HomeFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
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

        horizontalAdapter1 = new ItemRecyclerViewAdapter(getActivity(),
                GlobalVariables.getDocumentsRetrieved(), AppConstants.CARD_GRID);
        horizontalAdapter1.setHasStableIds(true);

        horizontalAdapter2 = new ItemRecyclerViewAdapter(getActivity(),
                GlobalVariables.getDocumentsRetrieved(), AppConstants.CARD_GRID);
        horizontalAdapter2.setHasStableIds(true);


        featuredListView.setAdapter(horizontalAdapter);
        recommendedListView.setAdapter(horizontalAdapter1);
        popularListView.setAdapter(horizontalAdapter2);

        //get the full text
        aboutContent = (TextView) rootView.findViewById(R.id.aboutContentView);
        CharSequence aboutText = aboutContent.getText();
        CharSequence moreAboutText = getResources().getString(R.string.moreAboutContent);
        StringBuilder sb = new StringBuilder();
        sb.append(moreAboutText);
        sb.append(aboutText);
        final String str = sb.toString();

        seeMoreAbout = (FloatingActionButton) rootView.findViewById(R.id.seeMoreAboutfab);
        seeMoreAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seeMoreAbout.getRotation()==0){
                    seeMoreAbout.animate().rotation(180).setDuration(500);

                    aboutContent.setText(str);
                }else {
                    seeMoreAbout.animate().rotation(0).setDuration(500);
                    aboutContent.setText(R.string.aboutContent);
                }
            }
        });


        return rootView;
    }
}
