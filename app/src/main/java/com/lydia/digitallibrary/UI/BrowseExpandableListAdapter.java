package com.lydia.digitallibrary.UI;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lydia.digitallibrary.Helper.AppConstants;
import com.lydia.digitallibrary.Helper.Document;
import com.lydia.digitallibrary.Helper.GlobalVariables;
import com.lydia.digitallibrary.Helper.ItemViewTransition;
import com.lydia.digitallibrary.Helper.RecyclerItemClickListener;
import com.lydia.digitallibrary.Helper.ResponseXMLParser;
import com.lydia.digitallibrary.Helper.SolrQueryManager;
import com.lydia.digitallibrary.R;
import com.lydia.digitallibrary.UI.CollectionViewFragment;
import com.lydia.digitallibrary.UI.ItemRecyclerViewAdapter;
import com.lydia.digitallibrary.UI.ItemViewFragment;

import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lydia on 27/06/2016.
 */
public class BrowseExpandableListAdapter extends BaseExpandableListAdapter {

    private FragmentActivity mActivity;

    private Context mContext;
    private Fragment mParent;
    private List<String> listCategories; // header titles
    public LinearLayoutManager layoutManager;
    private RecyclerView horizontalListView;

    private SolrQueryManager solrQueryManager;
    private ArrayList<Document> documentsRetrieved;
    private ResponseXMLParser responseXMLParser;

    private String category;

    private ItemRecyclerViewAdapter horizontalAdapter;

    public BrowseExpandableListAdapter(FragmentActivity activity, Fragment parent, List<String> listDataHeader) {
        this.mActivity = activity;
        this.mParent = parent;
        this.mContext = mParent.getContext();
        this.listCategories = AppConstants.CATEGORIES;

        // Initialize Query constructor
        solrQueryManager = new SolrQueryManager();
        // Get new response parser
        responseXMLParser = new ResponseXMLParser();

        documentsRetrieved = new ArrayList<>();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        category = this.listCategories.get(groupPosition);
        return category;
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
    public Object getChild(int groupPosition, int childPosition) {
        return this.listCategories.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        GlobalVariables.clearDocumentsRetrieved();
        GlobalVariables.clearPreviewArray();

        Log.d("Clears", GlobalVariables.getDocumentsRetrieved().toString() +
        GlobalVariables.getPreviewArray().toString());

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.browse_list_child_view, null);
        }

        category = listCategories.get(groupPosition);
        GlobalVariables.setCategory(category);

        //Get the collection items to preview
        GetCollectionResults getCollectionResults = new GetCollectionResults();
        getCollectionResults.execute(category);

        //Get recyclerView
        horizontalListView = (RecyclerView) convertView.findViewById(R.id.browseChildView);
        //assign new layout manager
        layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        horizontalListView.setLayoutManager(layoutManager);

        //Return to previous scroll position if currently expanded
        if (GlobalVariables.getLastFirstVisiblePos()!=-1) {
            horizontalListView.scrollToPosition(GlobalVariables.getLastFirstVisiblePos());
            GlobalVariables.setFirstLastVisiblePos(-1);
        }

        // use this to improve performance if you know changes in
        // content do not change the layout size of the RecyclerView
        horizontalListView.setHasFixedSize(true);

        //Get & Set adapter
        horizontalAdapter = new ItemRecyclerViewAdapter(mActivity,
                GlobalVariables.getDocumentsRetrieved(), AppConstants.CARD_GRID);
        horizontalAdapter.setHasStableIds(true);
        horizontalListView.setAdapter(horizontalAdapter);




        horizontalListView.addOnItemTouchListener(new RecyclerItemClickListener(mActivity,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        int lastFirstVisiblePosition = ((LinearLayoutManager)horizontalListView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                        GlobalVariables.setFirstLastVisiblePos(lastFirstVisiblePosition);

                        startItemView(view, position);
                    }
                }));

        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

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
        category = listCategories.get(groupPosition);
        viewHolder.categoryName.setText(category);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            collectionFragment.setEnterTransition(new Fade());
            collectionFragment.setReenterTransition(new Fade());
            collectionFragment.setExitTransition(new Fade());
            mParent.setExitTransition(new Fade());
            mParent.setReenterTransition(new Fade());

            FragmentTransaction transaction = mActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, collectionFragment)
                    .addToBackStack("transactionCollection");
            //apply transaction
            transaction.commit();
        }
    }

    public void startItemView(View view, int position){

        // Find the shared element (in Fragment A)
        ImageView imgView = (ImageView) view.findViewById(R.id.thumbnail);

        Bitmap bitmap = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
        if(bitmap != null) {
            GlobalVariables.setSelectedBitmap(bitmap);
        }

        ItemViewFragment viewFragment = new ItemViewFragment();

        Bundle bun = new Bundle();
        bun.putStringArray(AppConstants.documentTransferString, documentsRetrieved.get(position).toArray());
        bun.putString(AppConstants.imageTransitionName, imgView.getTransitionName());
        viewFragment.setArguments(bun);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            viewFragment.setSharedElementEnterTransition(new ItemViewTransition());
            viewFragment.setEnterTransition(new Fade());
            mParent.setExitTransition(new Fade());
            //mParent.setReturnTransition(new Fade());
            viewFragment.setSharedElementReturnTransition(new ItemViewTransition());

            FragmentTransaction transaction = mActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, viewFragment)
                    .addToBackStack("transaction")
                    .addSharedElement(imgView, "cardImg" + position);
            //apply transaction
            transaction.commit();
        }
    }

    public static class ViewHolderItem {
        TextView categoryName;
        Button seeMore;
    }

    // Creates an asynchronous task that gets the results to the search
    private class GetCollectionResults extends AsyncTask<String, Integer, ArrayList<Document>>  {

        String query;

        protected ArrayList<Document> doInBackground(String... sQuery){
            try {
                if (android.os.Debug.isDebuggerConnected()) {
                    android.os.Debug.waitForDebugger();
                }
                query = sQuery[0].toLowerCase();

                String solrQuery = solrQueryManager.constructCollectionQuery(query, 0, 21);

                InputStream responseStream = solrQueryManager.queryUrlForDataStream(solrQuery);

                ArrayList<Document> documentList = null;

                try {
                    documentList = (ArrayList<Document>) responseXMLParser.parseSearchResponse(responseStream);
                    //Log.d("Test2 doc lit", String.valueOf(documentList.size()));
                } catch (java.io.IOException e){
                    // Add error dialogue
                    e.printStackTrace();
                } catch (XmlPullParserException e){
                    // Add error dialogue
                    e.printStackTrace();
                }
                // Assign the retrieved documents to the documentsRetrieved List
                return documentList;
            } catch (java.lang.RuntimeException e){
                return null;
            }
        }

        protected void onPostExecute (ArrayList<Document> result) {
            // if result retrieved
            if (result != null) {
                setListToRetrievedDocuments(result);
                /*if documentsRetrieved
                if (!result.isEmpty()) {
                    writeSearchToDatabase(query);
                }*/
            }
            // if no result retrieved
            else {
                Log.d("OnPostExecuteresult", "No result received");
            }
                /*builder.setMessage(R.string.network_error_message)
                        .setTitle(R.string.network_error_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                final AlertDialog networkErrorDialog = builder.create();
                networkErrorDialog.show();
            }
            mProgressBar.setVisibility(View.INVISIBLE);*/
        }
    }

    private void setListToRetrievedDocuments(ArrayList<Document> result) {
        if(result.size() != 0){

            GlobalVariables.clearDocumentsRetrieved();

            for (Document doc : result) {
                GlobalVariables.appendToDocumentsRetrieved(doc);
            }

            documentsRetrieved = GlobalVariables.getDocumentsRetrieved();

            horizontalAdapter = new ItemRecyclerViewAdapter(mContext, GlobalVariables.getDocumentsRetrieved(), AppConstants.CARD_GRID);
            horizontalListView.setAdapter(horizontalAdapter);
        }
    }

}
