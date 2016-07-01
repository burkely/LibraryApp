package com.lydia.digitallibrary;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lydia on 27/06/2016.
 */
public class BrowseExpandableListAdapter extends BaseExpandableListAdapter {


    private Context mContext;
    private List<String> listCategories; // header titles
    private HashMap<String, List<String>> listChildData;

    public BrowseExpandableListAdapter(Context context, List<String> listDataHeader,
                                       HashMap<String, List<String>> _listChildData) {
        this.mContext = context;
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
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.browse_list_child_view, null);
        }

        RecyclerView horizontalListView = (RecyclerView) convertView.findViewById(R.id.browseChildView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        horizontalListView.setLayoutManager(layoutManager);

        List<String> childData =  listChildData.get(this.listCategories.get(groupPosition));

        BrowseRecyclerViewAdapter horizontalAdapter = new BrowseRecyclerViewAdapter(
                                                            mContext, childData);

        // use this to improve performance if you know changes in
        // content do not change the layout size of the RecyclerView
        horizontalListView.setHasFixedSize(true);

        horizontalListView.setAdapter(horizontalAdapter);

        return convertView;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        ///ParentHolder parentHolder = null;

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.browse_list_parent, null);
            convertView.setHorizontalScrollBarEnabled(true);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
