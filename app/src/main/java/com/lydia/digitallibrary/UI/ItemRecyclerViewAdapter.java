package com.lydia.digitallibrary.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lydia.digitallibrary.Helper.Document;
import com.lydia.digitallibrary.Helper.AppConstants;
import com.lydia.digitallibrary.Helper.GetThumbnailImage;
import com.lydia.digitallibrary.Helper.GlobalVariables;
import com.lydia.digitallibrary.Helper.SolrQueryManager;
import com.lydia.digitallibrary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lydia on 01/07/2016.
 */
public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Document> documentsRetrieved = new ArrayList<>();
    private int mLayoutType;
    private int EMPTY_VIEW = 21;
    private String category;

    private SolrQueryManager solrQueryManager;

    public ItemRecyclerViewAdapter(Context context, List<Document> documents, int layoutType) {
        this.mContext = context;
        this.documentsRetrieved = documents;
        this.mLayoutType = layoutType;
        solrQueryManager = new SolrQueryManager();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == EMPTY_VIEW) {
            View card = inflater.inflate(R.layout.card_layout_empty, null, false);
            viewHolder = new EmptyViewHolder(card);
        }

        else if(mLayoutType == AppConstants.CARD_LIST_SUB){
            View card = inflater.inflate(R.layout.card_layout_list_sub, parent, false);
            viewHolder = new SearchViewHolder(card);
        }
        else if(mLayoutType == AppConstants.CARD_LIST) {
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
        SearchViewHolder sViewHolder;
        ViewHolder viewHolder;
        EmptyViewHolder eViewHolder;
        ImageView img;

    if(documentsRetrieved==null){documentsRetrieved=new ArrayList<>();}
            // Get data for this position
        if(documentsRetrieved.size() != 0) {
            Document document = documentsRetrieved.get(position);
            //If getting search results get SearchViewholder with subtitle
            if (holder instanceof SearchViewHolder) {
                sViewHolder = (SearchViewHolder) holder;
                sViewHolder.mTitle.setText(document.getText());
                sViewHolder.mSubText.setText(document.getGenre());
                sViewHolder.mImg.setImageResource(R.mipmap.image_loading);
                sViewHolder.mImg.setTransitionName("cardImg" + position);
                sViewHolder.mImg.setTag(document.getPid());
                img = sViewHolder.mImg;
            }else if (holder instanceof ViewHolder){ //(holder instanceof ViewHolder)
                viewHolder = (ViewHolder) holder;
                viewHolder.mTitle.setText(document.getText());
                viewHolder.mImg.setImageResource(R.mipmap.image_loading);
                viewHolder.mImg.setTransitionName("cardImg" + position);
                viewHolder.mImg.setTag(document.getPid());
                img = viewHolder.mImg;
            }else{ //(holder instanceof EmptyViewHolder)
                eViewHolder = (EmptyViewHolder) holder;
                img = eViewHolder.mImg;
            }

            category = GlobalVariables.getCategory();
            HashMap<String, List<Bitmap>> hp = GlobalVariables.getPreviewArray();
            List<Bitmap> value = hp.get(category);

            //if we've already gotten the preview
            if (value != null && !(position >= value.size())) {
                img.setImageBitmap(value.get(position));
                Log.d("have image", String.valueOf(position));
            }else{
                // Get thumbnail image from url
                GetThumbnailImage getThumbnailImage = new GetThumbnailImage();
                getThumbnailImage.updateInfoSyncTask(document.getPid(), img, img.getTag(), solrQueryManager, 0, category); // get small thumbnail
                getThumbnailImage.execute();
                Log.d("getting image", String.valueOf(position));
            }
            Log.d("logTAg", img.getTag().toString());
        }
    }

    @Override
    public int getItemCount() {
        if(documentsRetrieved==null){
            return EMPTY_VIEW;
        }
        return documentsRetrieved.size() > 0 ? documentsRetrieved.size() : EMPTY_VIEW;
    }

    @Override
    public int getItemViewType(int position) {
        if(GlobalVariables.getDocumentsRetrieved() == null ||
                GlobalVariables.getDocumentsRetrieved().size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position){
        GlobalVariables.count++;
        //Log.d("yo", String.valueOf(GlobalVariables.count));
        return GlobalVariables.count;

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

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.thumbnail);
            mImg.setImageResource(R.mipmap.image_loading);
        }
    }

}
