package com.lydia.digitallibrary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lydia on 27/06/2016.
 */
public class BrowseFragment extends Fragment{

    private static final String TAG = "BrowseFragment";

    private BrowseExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;

    public int prevExpandedPosition;
    public int currExpandedPosition;

    public BrowseFragment(){
        setArguments(new Bundle());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listDataHeader = AppConstants.CATEGORIES;

        GlobalVariables.setCurrExpandedPos(-1);
        GlobalVariables.setPrevExpandedPos(-1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.browse_frag, container, false);
        rootView.setTag(TAG);

        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

        listAdapter = new BrowseExpandableListAdapter(getActivity(), this, listDataHeader);

        // setting list adapter
        expListView.setAdapter(listAdapter);


        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                currExpandedPosition = (int) listAdapter.getGroupId(groupPosition);
                GlobalVariables.setCurrExpandedPos(currExpandedPosition);

                prevExpandedPosition = GlobalVariables.getPrevExpandedPos();
                //if others are open close em
                if (prevExpandedPosition != -1) {
                    expListView.collapseGroup(prevExpandedPosition);
                }
                GlobalVariables.setPrevExpandedPos(currExpandedPosition);
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

                if (GlobalVariables.getCurrExpandedPos() == groupPosition) {
                    GlobalVariables.setPrevExpandedPos(-1);
                    GlobalVariables.setCurrExpandedPos(-1);
                }
            }
        });

        return rootView;
    }
}
