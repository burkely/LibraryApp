package com.lydia.digitallibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lydia on 01/07/2016.
 */
public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mTestData = new ArrayList<>();
    private int mLayoutType;


    public ItemRecyclerViewAdapter(Context context, List<String> array, int layoutType) {
        this.mContext = context;
        this.mTestData = array;
        this.mLayoutType = layoutType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(mLayoutType == Constants.CARD_LIST_SUB){
            View card = inflater.inflate(R.layout.card_layout_list_sub, parent, false);
            viewHolder = new SearchViewHolder(card);
        }
        else if(mLayoutType == Constants.CARD_LIST) {
            View card = inflater.inflate(R.layout.card_layout_list, parent, false);
            viewHolder = new ViewHolder(card);
        }
        else {
            View card = inflater.inflate(R.layout.card_layout_grid, null, false);
            viewHolder = new ViewHolder(card);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //If getting search results get SearchViewholder with subtitle
        if(mLayoutType == Constants.CARD_LIST_SUB){
            SearchViewHolder viewHolder = (SearchViewHolder) holder;
            viewHolder.mTitle.setText(mTestData.get(position));
            viewHolder.mSubText.setText(mTestData.get(position));
            viewHolder.mImg.setImageResource(Constants.myTestImgs[position]);
        }
        //Otherwise we are using list/grid version with no subtitle
        else {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.mTitle.setText(mTestData.get(position));
            viewHolder.mImg.setImageResource(Constants.myTestImgs[position]);
        }
    }

    @Override
    public int getItemCount() {
        return mTestData.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        ImageView mImg;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.card_title);
            mImg = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mSubText;
        ImageView mImg;

        public SearchViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.card_title);
            mSubText = (TextView) itemView.findViewById(R.id.card_subtext);
            mImg = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

}
