package com.lydia.digitallibrary.UI;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lydia.digitallibrary.Helper.Document;
import com.lydia.digitallibrary.Helper.AppConstants;
import com.lydia.digitallibrary.Helper.GlobalVariables;
import com.lydia.digitallibrary.Helper.RecyclerItemClickListener;
import com.lydia.digitallibrary.Helper.ResponseXMLParser;
import com.lydia.digitallibrary.Helper.SolrQueryManager;
import com.lydia.digitallibrary.Helper.ItemViewTransition;
import com.lydia.digitallibrary.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static final int SPAN_COUNT = 3;

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected FloatingActionButton mFab;
    protected ItemRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    private SolrQueryManager solrQueryManager;
    private ResponseXMLParser responseXMLParser;

    private int page;

    private String category;
    private List<Bitmap> previewImages;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    public CollectionViewFragment() {
        setArguments(new Bundle());
    }

    @Override
    public void onPause(){
        super.onPause();
        getArguments().putSerializable(AppConstants.KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        solrQueryManager = new SolrQueryManager();
        responseXMLParser = new ResponseXMLParser();

        page = 1;

        category = GlobalVariables.getCategory();

        HashMap<String, List<Bitmap>> previewMap = GlobalVariables.getPreviewArray();
        previewImages = previewMap.get(category);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.collection_view_frag, container, false);
        rootView.setTag(TAG);

        //get recyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewCollection);

        // The RecyclerView.LayoutManager defines elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        //Set layout manager type
        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        Bundle mySavedInstanceState = getArguments();
        if(mySavedInstanceState != null &&
                mySavedInstanceState.getSerializable(AppConstants.KEY_LAYOUT_MANAGER) != null) {
            Log.d("savedstate", mySavedInstanceState.toString());
            mCurrentLayoutManagerType = (LayoutManagerType) mySavedInstanceState.getSerializable(
                    AppConstants.KEY_LAYOUT_MANAGER);
        }

        //if restoring view get previous layout
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            Log.d("restoring ", "in collection view");
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(AppConstants.KEY_LAYOUT_MANAGER);
        }

        //set layout manager
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        //set layout depending on layout type (Grid/list)
        int layoutType;
        if ( mCurrentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER) {
            layoutType = AppConstants.CARD_LIST;
        }else{
            layoutType = AppConstants.CARD_GRID;
        }

        // Set RecyclerViewAdapter as the adapter for RecyclerView
        mAdapter = new ItemRecyclerViewAdapter(getContext(), GlobalVariables.getDocumentsRetrieved(), layoutType);
        mRecyclerView.setAdapter(mAdapter);

        //Open ItemViewFragment when item clicked
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        startItemView(view, position);
                    }
                }));


        // set the scroll listener
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean loading = true;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                loading = true;
                LinearLayoutManager mLM;
                if (dy > 0) //check for scroll down
                {
                    if (mCurrentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER) {
                        mLM = (LinearLayoutManager) recyclerView.getLayoutManager();
                    }else{
                        mLM = (GridLayoutManager) recyclerView.getLayoutManager();
                    }
                    final int visibleItemCount = mLM.getChildCount();
                    final int totalItemCount = mLM.getItemCount();
                    final int pastVisibleItems = mLM.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = false;
                            Log.d("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            GetCollectionResults getResults = new GetCollectionResults();
                            getResults.paging(true, page);
                            getResults.execute(GlobalVariables.getCategory());
                            page++;
                            Log.d("page", String.valueOf(page)+" "+GlobalVariables.getCategory());
                        }
                    }
                }
            }
        });


    //onClick listener toggle views (Grid-List) on Floating Action Button click
    mFab=(FloatingActionButton)rootView.findViewById(R.id.fab);
    mFab.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick (View v){
        if (mCurrentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER) {
                    mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                    //Get new adapter with correct layout type
                    mAdapter = new ItemRecyclerViewAdapter(getContext(), GlobalVariables.getDocumentsRetrieved(), AppConstants.CARD_GRID);
                }else{
                    mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                    //Get new adapter with correct layout type
                    mAdapter = new ItemRecyclerViewAdapter(getContext(), GlobalVariables.getDocumentsRetrieved(), AppConstants.CARD_LIST);
                }
                //Set layout manager type
                setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
                //Set adapter
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        return rootView;
    }

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
    public void onDestroyView(){
        super.onDestroyView();
        GlobalVariables.clearDocumentsRetrieved();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.

        Log.d("on save instance state", "in collection view");
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(AppConstants.KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
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
        bun.putStringArray(AppConstants.documentTransferString, GlobalVariables.getDocumentsRetrieved().get(position).toArray());
        bun.putString(AppConstants.imageTransitionName, imgView.getTransitionName());
        viewFragment.setArguments(bun);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            viewFragment.setSharedElementEnterTransition(new ItemViewTransition());
            this.setExitTransition(new Fade());
            this.setReturnTransition(new Fade());
            viewFragment.setSharedElementReturnTransition(new ItemViewTransition());

            FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, viewFragment)
                    .addToBackStack("transaction")
                    .addSharedElement(imgView, "cardImg" + position);
            //apply transaction
            transaction.commit();
        }
    }

    private class GetCollectionResults extends AsyncTask<String, Integer, ArrayList<Document>> {

        String query;
        boolean appendToList = false;
        int resultsPage;

        public void paging(boolean appendToList, int resultsPage) {
            this.appendToList = appendToList;
            this.resultsPage = resultsPage;
        }

        protected ArrayList<Document> doInBackground(String... sQuery){
            try {
                if (android.os.Debug.isDebuggerConnected()) {
                    android.os.Debug.waitForDebugger();
                }
                query = sQuery[0].toLowerCase();

                String solrQuery = solrQueryManager.constructCollectionQuery(query, resultsPage, 21);

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
                setListToRetrievedDocuments(result, appendToList);
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

    private void setListToRetrievedDocuments(ArrayList<Document> result, boolean appendToList) {
        if(result.size() != 0){
            if(appendToList){
                //Log.d(TAG, "Will append to the list");
                for (Document doc : result) {
                    GlobalVariables.appendToDocumentsRetrieved(doc);
                    mAdapter.notifyItemInserted(GlobalVariables.getDocumentsRetrieved().size()-1);
                }
                //mAdapter.notifyItemRangeInserted();
            }
        }
    }

}
