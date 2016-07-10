package com.lydia.digitallibrary;

import android.content.Context;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lydia on 27/06/2016.
 */
public class BrowseExpandableListAdapter extends BaseExpandableListAdapter {

    private FragmentActivity mActivity;
    private Context mContext;
    private Fragment mParent;
    private List<String> listCategories; // header titles
    private HashMap<String, List<String>> listChildData;
    public LinearLayoutManager layoutManager;
    private List<String> childData;
    private RecyclerView horizontalListView;

    public Parcelable state;

    public BrowseExpandableListAdapter(FragmentActivity activity, Fragment parent, List<String> listDataHeader,
                                       HashMap<String, List<String>> _listChildData) {
        this.mActivity = activity;
        this.mParent = parent;
        this.mContext = mParent.getContext();
        this.listCategories = listDataHeader;
        this.listChildData = _listChildData;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listCategories.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listCategories.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listCategories.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.browse_list_child_view, null);
        }

        horizontalListView = (RecyclerView) convertView.findViewById(R.id.browseChildView);
        layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        horizontalListView.setLayoutManager(layoutManager);

        childData =  listChildData.get(this.listCategories.get(groupPosition));

        BrowseRecyclerViewAdapter horizontalAdapter = new BrowseRecyclerViewAdapter(
                mActivity, childData);

        // use this to improve performance if you know changes in
        // content do not change the layout size of the RecyclerView
        horizontalListView.setHasFixedSize(true);

        horizontalListView.setAdapter(horizontalAdapter);

        horizontalListView.addOnItemTouchListener(new RecyclerItemClickListener(mActivity,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        LinearLayoutManager lm = (LinearLayoutManager) horizontalListView.getLayoutManager();
                        state = lm.onSaveInstanceState();

                        startItemView(view, Constants.myTestImgs[position], position);

                        Log.d("state", state.toString());
                    }
                }));

        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);

        //set up the ViewHolder
        final ViewHolderItem viewHolder;

        if(convertView == null){

            LayoutInflater inflater = (LayoutInflater) this.mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.browse_list_parent, null);
            convertView.setHorizontalScrollBarEnabled(true);

            viewHolder = new ViewHolderItem();
            viewHolder.categoryName = (TextView) convertView.findViewById(R.id.lblListHeader);

            viewHolder.seeMore = (Button) convertView.findViewById(R.id.seeMore);

            // store the holder with the view.
            convertView.setTag(viewHolder);
        }

        else{
            // avoided calling findViewById() on resource everytime
            // just use the existing viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        //assign values to holder
        viewHolder.categoryName.setText(headerTitle);
        viewHolder.categoryName.setTypeface(null, Typeface.BOLD);
        viewHolder.seeMore.setFocusable(false);
        int expView = GlobalVariables.getCurrExpandedPos();
        if (expView == groupPosition){
            viewHolder.seeMore.setVisibility(View.VISIBLE);
        }else {
            viewHolder.seeMore.setVisibility(View.INVISIBLE);
        }

        //set on click listener on our button
        viewHolder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                startCollectionView();
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void startCollectionView(){

        CollectionViewFragment collectionFragment = new CollectionViewFragment();

        //Bundle bun = new Bundle();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            collectionFragment.setEnterTransition(new Fade());
            mParent.setExitTransition(new Fade());

            FragmentTransaction transaction = mActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, collectionFragment)
                    .addToBackStack("transactioncoll");
            //apply transaction
            transaction.commit();
        }
    }

    public void startItemView(View view, int imgId, int position){

        // Find the shared element (in Fragment A)
        ImageView img = (ImageView) view.findViewById(R.id.thumbnail);

        ItemViewFragment viewFragment = new ItemViewFragment();

        Bundle bun = new Bundle();
        bun.putString("trans name", img.getTransitionName());
        bun.putInt("selected_image", imgId);
        viewFragment.setArguments(bun);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            viewFragment.setSharedElementEnterTransition(new ItemViewTransition());
            viewFragment.setEnterTransition(new Explode());
            mParent.setExitTransition(new Fade());
            viewFragment.setSharedElementReturnTransition(new ItemViewTransition());

            FragmentTransaction transaction = mActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, viewFragment)
                    .addToBackStack("transaction")
                    .addSharedElement(img, "cardImg" + position);
            //apply transaction
            transaction.commit();
        }
    }

    public static class ViewHolderItem {
        TextView categoryName;
        Button seeMore;
    }

}
