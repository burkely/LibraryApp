package com.lydia.digitallibrary;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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
public class BrowseRecyclerViewAdapter extends RecyclerView.Adapter<BrowseRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<String> testData = new ArrayList<>();

    public BrowseRecyclerViewAdapter(Context context, List<String> array) {
            this.context = context;
            this.testData = array;
            }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = inflater.inflate(R.layout.browse_cards, null, false);
        ViewHolder viewHolder = new ViewHolder(card);

        viewHolder.Name = (TextView) card.findViewById(R.id.card_title);
        viewHolder.mImg = (ImageView) card.findViewById(R.id.thumbnail);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView dataTextView = holder.Name;
        ImageView testImgs = holder.mImg;
        dataTextView.setText(testData.get(position));
        testImgs.setImageResource(Constants.myTestImgs[position]);
        testImgs.setTransitionName("cardImg" + position);
    }

    @Override
    public int getItemCount() {
        return testData.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView mImg;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.card_title);
            mImg = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

}
