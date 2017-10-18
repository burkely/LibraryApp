package com.lydia.digitallibrary.UI;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lydia.digitallibrary.Helper.AppConstants;
import com.lydia.digitallibrary.Helper.GlobalVariables;
import com.lydia.digitallibrary.Helper.ResponseJSONParser;
import com.lydia.digitallibrary.Helper.SolrQueryManager;
import com.lydia.digitallibrary.R;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Lydia on 04/07/2016.
 */

public class ItemViewFragment extends Fragment {

    private String[] docInfo; // Pid, DrisFolderNumber, Text, Genre, Lang, TypeOfResource

    ArrayList<CharSequence> documentMetadata; // title, origin_place, publisher, date, language, abstract, access_condition
    ArrayList<String> documentDetails; // title, origin_place, publisher, date, language, abstract, access_condition, pid

    ImageView mImageView;

    RelativeLayout mTitleLayout;
    RelativeLayout mOriginLayout;
    RelativeLayout mPublisherLayout;
    RelativeLayout mDateLayout;
    RelativeLayout mLanguageLayout;
    RelativeLayout mAbstractLayout;
    RelativeLayout mAccessConditionLayout;

    TextView mTitleLabelTextView;
    TextView mTitleTextView;
    TextView mOriginLabelTextView;
    TextView mOriginTextView;
    TextView mPublisherLabelTextView;
    TextView mPublisherTextView;
    TextView mDateLabelTextView;
    TextView mDateTextView;
    TextView mLanguageLabelTextView;
    TextView mLanguageTextView;
    TextView mAbstractLabelTextView;
    TextView mAbstractTextView;
    TextView mAccessConditionLabelTextView;
    TextView mAccessConditionTextView;

    private static final String TAG = "ItemViewFragment";

    private SolrQueryManager solrQueryManager;

    private Bitmap imgBitmap;
    private String transName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieves doc info passed from previous activity
        Bundle bundle = getArguments();
        transName = bundle.getString(AppConstants.imageTransitionName);

        docInfo = bundle.getStringArray(AppConstants.documentTransferString);
        Log.d("docInfo", docInfo.toString());
        imgBitmap = GlobalVariables.getSelectedBitmap();

        solrQueryManager = new SolrQueryManager();

        // Get metadata for document
        GetDocumentMetadata getDocumentMetadata = new GetDocumentMetadata();
        getDocumentMetadata.execute(docInfo[0]);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.item_view_frag, container, false);
        rootView.setTag(TAG);

        // get the imgView
        mImageView = (ImageView) rootView.findViewById(R.id.itemView);
        mImageView.setImageBitmap(imgBitmap);
        mImageView.setTransitionName(transName);

        // Find the Title Views
        mTitleLayout= (RelativeLayout) rootView.findViewById(R.id.detailViewTitleContainer);
        mTitleLabelTextView = (TextView) rootView.findViewById(R.id.detailViewTitleTextViewLabel);
        mTitleTextView = (TextView) rootView.findViewById(R.id.detailViewTitleTextView);

        // Find the Origin Views
        mOriginLayout = (RelativeLayout) rootView.findViewById(R.id.detailViewOriginPlaceContainer);
        mOriginLabelTextView = (TextView) rootView.findViewById(R.id.detailViewOriginLabelTextView);
        mOriginTextView = (TextView) rootView.findViewById(R.id.detailViewOriginTextView);

        // Find the Publisher Views
        mPublisherLayout = (RelativeLayout) rootView.findViewById(R.id.detailViewPublisherContainer);
        mPublisherLabelTextView = (TextView) rootView.findViewById(R.id.detailViewPublisherLabelTextView);
        mPublisherTextView = (TextView) rootView.findViewById(R.id.detailViewPublisherTextView);

        // Find the Date Views
        mDateLayout = (RelativeLayout) rootView.findViewById(R.id.detailViewDateContainer);
        mDateLabelTextView = (TextView) rootView.findViewById(R.id.detailViewDateLabelTextView);
        mDateTextView = (TextView) rootView.findViewById(R.id.detailViewDateTextView);

        // Find the Language Views
        mLanguageLayout = (RelativeLayout) rootView.findViewById(R.id.detailViewLanguageContainer);
        mLanguageLabelTextView = (TextView) rootView.findViewById(R.id.detailViewLanguageLabelTextView);
        mLanguageTextView = (TextView) rootView.findViewById(R.id.detailViewLanguageTextView);

        // Find the Abstract Views
        mAbstractLayout = (RelativeLayout) rootView.findViewById(R.id.detailViewAbstractContainer);
        mAbstractLabelTextView = (TextView) rootView.findViewById(R.id.detailViewAbstractLabelTextView);
        mAbstractTextView = (TextView) rootView.findViewById(R.id.detailViewAbstractTextView);

        // Find the Access Condition Views
        mAccessConditionLayout = (RelativeLayout) rootView.findViewById(R.id.detailViewAccessConditionContainer);
        mAccessConditionLabelTextView = (TextView) rootView.findViewById(R.id.detailViewAccessConditionLabelTextView);
        mAccessConditionTextView = (TextView) rootView.findViewById(R.id.detailViewAccessConditionTextView);

        return rootView;
    }

    private void setTextInDetailTextViews(String detailVal, TextView label, TextView detailView, RelativeLayout layout) {
        // If a detail val exists assign it to its text fields, otherwise set its corresponding fields to invisible
        if (!detailVal.equals("")) {
            detailView.setText(detailVal);
        }
        else {
            label.setVisibility(View.GONE);
            detailView.setVisibility(View.GONE);
            if (layout != null){
                layout.setVisibility(View.GONE);
            }
        }
    }

    private class GetDocumentMetadata extends AsyncTask<String, Integer, ArrayList<CharSequence>> {

        @Override
        protected ArrayList<CharSequence> doInBackground(String... params) {
            try {
                if (android.os.Debug.isDebuggerConnected()) {
                    android.os.Debug.waitForDebugger();
                }
                String metadataQuery = solrQueryManager.constructDocMetadataQuery(params[0]);
                //Log.d(TAG, metadataQuery);
                InputStream responseStream = solrQueryManager.queryUrlForDataStream(metadataQuery);

                // title, origin_place, publisher, date, language, abstract, access_condition
                ArrayList<CharSequence> response = null;
                try {
                    // Get response and response as string (for debugging)
                    ResponseJSONParser responseJSONParser = new ResponseJSONParser();
                    response = responseJSONParser.parseMetadata(responseStream);


                    documentMetadata = response;
                    //documentDetails = new ArrayList<String>(documentMetadata);

                    //Add Pid to documentMetadata it will be later transfered to the detail view activity
                    //documentMetadata.add(docInfo[0]);

                    /*
                    for(String s: response) {
                        Log.d(TAG, s);
                    }
                    */
                } catch (java.io.IOException e){
                    e.printStackTrace();
                }
                return response;
            } catch(java.lang.RuntimeException e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<CharSequence> result){

            if (result != null){

                // title, origin_place, publisher, date, language, abstract, access_condition
                //mTitleTextView.setText(result.get(0));

                //Assign text to the Views
                setTextInDetailTextViews(documentMetadata.get(0).toString(), mTitleLabelTextView, mTitleTextView, mTitleLayout);
                setTextInDetailTextViews(documentMetadata.get(1).toString(), mOriginLabelTextView, mOriginTextView, mOriginLayout);
                setTextInDetailTextViews(documentMetadata.get(2).toString(), mPublisherLabelTextView, mPublisherTextView, mPublisherLayout);
                setTextInDetailTextViews(documentMetadata.get(3).toString(), mDateLabelTextView, mDateTextView, mDateLayout);
                setTextInDetailTextViews(documentMetadata.get(5).toString(), mAbstractLabelTextView, mAbstractTextView, mAbstractLayout);
                setTextInDetailTextViews(documentMetadata.get(4).toString(), mLanguageLabelTextView, mLanguageTextView, mLanguageLayout);
                setTextInDetailTextViews(documentMetadata.get(6).toString(), mAccessConditionLabelTextView, mAccessConditionTextView, mAccessConditionLayout);

            }

        }
    }

}
