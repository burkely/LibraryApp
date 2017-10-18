package com.lydia.digitallibrary.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by osulld13 on 07/02/16.
 */
public class SolrQueryManager {

    //private final String TAG = SolrQueryManager.class.getSimpleName();
    private final String TAG = "thumbnail";
    // return elements in a collection
    public String constructCollectionQuery(String collectionQuery, int start, int rows) {

        String query;

        start = start*21;

        try {
            query = "http://library02.tchpc.tcd.ie:8080/solr/dris/select?indent=on&version=2.2"
                    + "&q=" + URLEncoder.encode(collectionQuery, "UTF-8")
                    + "&fq="
                    + "&start=" + String.valueOf(start)
                    + "&rows=" + String.valueOf(rows)
                    + "&fl=*%2Cscore&qt=standard&wt=standard&explainOther=&hl.fl=";

        } catch(UnsupportedEncodingException error) {
            Log.e(this.getClass().getSimpleName(), "Error encoding url. Trying without encoding");
            query = "http://library02.tchpc.tcd.ie:8080/solr/dris/select?indent=on&version=2.2&"
                    + "/select?q="
                    + collectionQuery
                    + "&fq="
                    + "&start=" + String.valueOf(start)
                    + "&rows=" + String.valueOf(start)
                    + "&fl=*%2Cscore&qt=standard&wt=standard&explainOther=&hl.fl=";
        }
        return query;
    }

    public String constructSolrSearchQuery(String freeQuery, int page, int rowsPerPage){

        String start = String.valueOf(page * rowsPerPage);
        String rows = String.valueOf(rowsPerPage);

        String query = "http://library02.tchpc.tcd.ie:8080/solr/dris/select?indent=on&version=2.2&q=subject_lctgm%3A[*%20TO%20*]&" +
                "fq=" +
                urlQueryAdapter(freeQuery) +
                "&start=" + start +
                "&rows="+ rows +
                "&fl=*%2Cscore&qt=standard&wt=standard&explainOther=&hl.fl=";
        return query;
    }

    public String constructListOfObjectsInCollectionQuery(String drisFolderNumber){
        String query =  "http://digitalcollections.tcd.ie/orderedListOfObjectsInCollection.php?folder=" + urlQueryAdapter(drisFolderNumber);
        return query;
    }

    public String constructDocMetadataQuery(String pId){
        String query = "http://digitalcollections.tcd.ie/home/getMeta.php?pid=" + pId;
        return query;
    }

    public String constructPopularItemsQuery(){
        String query = "http://46.101.47.238/";
        return query;
    }

    public Bitmap getImageResource(String drisFolderNum, String pid){
        URL url = getImageResourceURL(drisFolderNum, pid);
        Bitmap bmp = null;
        try {
            // Make sure image complies with memory limits

            // Load in info
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);

            // Calculate whether to load in resampled image
            options.inSampleSize = calculateInSampleSize(options,
                    AppConstants.documentImageWidth,
                    AppConstants.documentImageHeight);

            //Load in resampled image
            options.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);

        } catch (IOException e){
            e.printStackTrace();
        }
        return bmp;
    }

    public Bitmap getImageThumbnailResource(String pid, int size){

        URL url;

        Log.d(TAG, "trying to get thumbnail");

        // determine whether to get the larger of smaller thumbnail
        if (size == 0) {
            url = getResourceThumbnailURL(pid);
        }
        else {
            url = getResourceThumbnailLargerURL(pid);
        }

        //Log.d(TAG, url.toString());
        Bitmap bmp = null;
        try {
            Log.d(TAG, "in try");
            // Make sure image complies with memory limits

            // Load in info
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
            Log.d(TAG, "streaming in try");
            // Calculate whether to load in resampled image
            options.inSampleSize = calculateThumbnailSampleSize(options,
                    AppConstants.thumbnailImageWidth,
                    AppConstants.thumbnailImageHeight);

            //Load in resampled image
            options.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);

        } catch (IOException e){
            e.printStackTrace();
        }
        return bmp;
    }

    // Code got from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html#load-bitmap
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 4;
            }
        }

        return inSampleSize;
    }

    // Code got from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html#load-bitmap
    public static int calculateThumbnailSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private URL getImageResourceURL(String drisFolderNum, String pid){
        String urlString =  "http://digitalcollections.tcd.ie/content/"+drisFolderNum+"/jpeg/"+pid+"_LO.jpg";
        URL url = null;
        try {
            url =  new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Log.d(TAG, url.toString());
        return url;
    }

    private URL getResourceThumbnailURL(String pid){
        //String urlString =  "http://digitalcollections.tcd.ie/covers_220/"+pid+"_LO.jpg";
        String urlString =  "http://digitalcollections.tcd.ie/covers_thumbs/"+pid+"_LO.jpg";
        URL url = null;
        try {
            url =  new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, url.toString());
        return url;
    }

    private URL getResourceThumbnailLargerURL(String pid){
        String urlString =  "http://digitalcollections.tcd.ie/covers_220/"+pid+"_LO.jpg";
        URL url = null;
        try {
            url =  new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Log.d(TAG, url.toString());
        return url;
    }

    public InputStream queryUrlForDataStream(String url){

        // Have one (or more) threads ready to do the async tasks. Do this during startup of your app.
        ExecutorService executor = Executors.newFixedThreadPool(1);

        InputStream responseStream = null;
        try {

            // Fire a request.
            Future<Response> response = executor.submit(new Request(new URL(url)));

            // Get the response (here the current thread will block until response is returned).
            responseStream = response.get().getBody();

        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        catch (java.util.concurrent.ExecutionException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        // Shutdown the threads when finished
        executor.shutdown();

        return responseStream;

    }

    public String readStringFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    // Adapts queries to make them safe for HTTP transfer
    private String urlQueryAdapter(String query){
        // replace spaces with
        try {
            query = query.toLowerCase();
            query = query.replaceAll("[^a-zA-Z0-9\\s]", "");
            query = URLEncoder.encode(query, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e){
            // add error dialogue
            e.printStackTrace();
        }
        return query;
    }
}