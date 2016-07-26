package com.lydia.digitallibrary;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Donal on 13/03/2016.
 */
// Creates an asynchronous task that gets the image for the document view
public class GetThumbnailImage extends AsyncTask<Void, Void, Bitmap> {

    private final String TAG = GetThumbnailImage.class.getSimpleName();

    private String pId;
    private ImageView imageView;
    private Object viewId;
    private SolrQueryManager mSolrQueryManager;
    private int size;
    private String category;

    public void updateInfoSyncTask(String pId, ImageView imageView, Object viewID, SolrQueryManager solrQueryManager,
                                   int size ,String cat){
        this.pId = pId;
        this.imageView = imageView;
        this.viewId = viewID;
        this.mSolrQueryManager = solrQueryManager;
        this.size = size;
        this.category = cat;

        Log.d("logTAg", viewID.toString() + " " + imageView.toString());
    }

    protected Bitmap doInBackground(Void... params){
        try {
            if (android.os.Debug.isDebuggerConnected()) {
                android.os.Debug.waitForDebugger();
            }
            return mSolrQueryManager.getImageThumbnailResource(pId, size);
        } catch(RuntimeException e){
            return null;
        }
    }

    protected void onPostExecute (Bitmap result) {
        //Turn Progress indicator off
        if(android.os.Debug.isDebuggerConnected()){
            android.os.Debug.waitForDebugger();
        }

        // If result has been retrieved
        if (result != null) {

            category = GlobalVariables.getCategory();

            HashMap<String, List<Bitmap>> hp = GlobalVariables.getPreviewArray();
            List<Bitmap> imageList = hp.get(category);

            // if list does not exist create it
            if(imageList == null) {
                imageList = new ArrayList<>();
                imageList.add(result);
                GlobalVariables.appendPreviewArray(category, imageList);
            } else {
                // add if item is not already in list
                if(!imageList.contains(result)) imageList.add(result);
            }

            if(viewId == imageView.getTag()){
                this.imageView.setImageBitmap(result);
            }
        }

    }
}