package com.lydia.digitallibrary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Lydia on 23/06/2016.
 */

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */

public class CollectionViewFragment extends Fragment{

    private static final String TAG = "CollectionViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 3;

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected FloatingActionButton mFab;
    protected ItemRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;

    private List<String> childData;

    public CollectionViewFragment(){}

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.collection_view_frag, container, false);
        rootView.setTag(TAG);


       /* if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }*/

        //get recyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewCollection);

        //get data for recyclerview
        childData =  Constants.myTestData;

        // The RecyclerView.LayoutManager defines elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        //Set layout manager type
        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        // Set RecyclerViewAdapter as the adapter for RecyclerView.
        mAdapter = new ItemRecyclerViewAdapter(getContext(), childData, Constants.CARD_GRID);
        mRecyclerView.setAdapter(mAdapter);

        //Toggle views (Grid-List) on Floating Action Button click
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mCurrentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER) {
                    mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                    //Get new adapter with correct layout type
                    mAdapter = new ItemRecyclerViewAdapter(getContext(), childData, Constants.CARD_GRID);
                }else{
                    mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                    //Get new adapter with correct layout type
                    mAdapter = new ItemRecyclerViewAdapter(getContext(), childData, Constants.CARD_LIST);
                }
                //Set layout manager type
                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                //Set adapter
                mRecyclerView.setAdapter(mAdapter);
            }
        });


        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

}
