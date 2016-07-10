package com.lydia.digitallibrary;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

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
    private HashMap<String, List<String>> listDataChild;
    private Parcelable mState;
    public int prevExpandedPosition;
    public int currExpandedPosition;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalVariables.setCurrExpandedPos(-1);
        GlobalVariables.setPrevExpandedPos(-1);

        // preparing list data
        prepareListData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.browse_frag, container, false);
        rootView.setTag(TAG);

        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

        listAdapter = new BrowseExpandableListAdapter(getActivity(), this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


        // Listview Group click listener
        /*expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {}
        });*/


        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                currExpandedPosition = (int) listAdapter.getGroupId(groupPosition);
                GlobalVariables.setCurrExpandedPos(currExpandedPosition);

                prevExpandedPosition = GlobalVariables.getPrevExpandedPos();
                //if others are open close em
                if (prevExpandedPosition != -1) {
                    Toast.makeText(getContext(), "Expanding " + groupPosition + " collapsing " + prevExpandedPosition,
                            Toast.LENGTH_LONG).show();

                    //and hide the seeMore view
                    /*expListView.findViewById((int) listAdapter.getGroupId(prevExpandedPosition))
                            .findViewById(R.id.seeMore)
                            .setVisibility(View.INVISIBLE);*/

                    expListView.collapseGroup(prevExpandedPosition);
                }

                //prevExpandedPosition = currExpandedPosition;
                GlobalVariables.setPrevExpandedPos(currExpandedPosition);
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                if (GlobalVariables.getCurrExpandedPos() == groupPosition) {
                    //Log.d("closing open", String.valueOf(prevExpandedPosition));
                    //and hide the seeMore view
                    /*expListView.findViewById((int) listAdapter.getGroupId(currExpandedPosition))
                            .findViewById(R.id.seeMore)
                            .setVisibility(View.INVISIBLE);*/

                    GlobalVariables.setPrevExpandedPos(-1);
                    GlobalVariables.setCurrExpandedPos(-1);
                }
            }
        });


/*        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {}
        });*/

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
        View rv = getView();
        Log.d("in on pause", " ");
        // get the listview
        expListView = (ExpandableListView) rv.findViewById(R.id.lvExp);

        RecyclerView viewer = (RecyclerView) expListView.findViewById(R.id.browseChildView);

       if (viewer != null){
           LinearLayoutManager lm = (LinearLayoutManager) viewer.getLayoutManager();
           mState = lm.onSaveInstanceState();
       }
    }


    @Override
    public void onStop() {
        super.onStop();
        View rv = getView();
        Log.d("in on stop", " ");
        // get the listview
        expListView = (ExpandableListView) rv.findViewById(R.id.lvExp);

        RecyclerView viewer = (RecyclerView) expListView.findViewById(R.id.browseChildView);

        if (viewer != null){
            Log.d("onStop", "viewer "+viewer.toString());
            LinearLayoutManager lm = (LinearLayoutManager) viewer.getLayoutManager();
            Log.d("onStop", "lm "+lm.toString());
            mState = lm.onSaveInstanceState();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here

        View rv = getView();
        Log.d("in on stop", " ");
        // get the listview
        expListView = (ExpandableListView) rv.findViewById(R.id.lvExp);

        RecyclerView viewer = (RecyclerView) expListView.findViewById(R.id.browseChildView);

        if (viewer != null){
            Log.d("onStop", "viewer "+viewer.toString());
            LinearLayoutManager lm = (LinearLayoutManager) viewer.getLayoutManager();
            Log.d("onStop", "lm "+lm.toString());
            mState = lm.onSaveInstanceState();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("in on resume", " ");
        View rv = getView();

        if (rv != null) {

        }
    }

    private void prepareListData() {
        listDataHeader = Constants.CATEGORIES;
        listDataChild = new HashMap<>();

        // Adding child data
        for(int i=0; i<listDataHeader.size(); i++){
            listDataChild.put(listDataHeader.get(i), Constants.myTestData); // Header, Child data
        }
    }


}
