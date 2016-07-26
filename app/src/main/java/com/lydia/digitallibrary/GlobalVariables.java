package com.lydia.digitallibrary;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lydia on 09/07/2016.
 */
public class GlobalVariables {

    private static int currExpandedPos;
    private static int prevExpandedPos;
    private static ArrayList<Document> documentsRetrieved;
    private static int lastFirstVisiblePosition = -1;
    private static Bitmap selectedBitmap;
    private static HashMap<String, List<Bitmap>> collectionPreviews;
    private static String category;

    public static int count = 0;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getCategory(){return category;}

    public static void setCategory(String cat){category = cat;}

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static void setSelectedBitmap(Bitmap bitmap){
        selectedBitmap = bitmap;
    }

    public static Bitmap getSelectedBitmap(){
        return selectedBitmap;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static HashMap getPreviewArray(){
        return collectionPreviews;
    }

    public static void setPreviewArray(){
        collectionPreviews = new HashMap<>();
    }

    public static void clearPreviewArray(){
        collectionPreviews = new HashMap<>();
    }

    public static void appendPreviewArray(String Key, List<Bitmap> img){collectionPreviews.put(Key, img);}

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static void setCurrExpandedPos(int Pos){
        currExpandedPos = Pos;
    }

    public static int getCurrExpandedPos(){
        return currExpandedPos;
    }

    public static void setPrevExpandedPos(int Pos){
        prevExpandedPos = Pos;
    }

    public static int getPrevExpandedPos(){
        return prevExpandedPos;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static ArrayList<Document> getDocumentsRetrieved(){
        return documentsRetrieved;
    }

    public static void clearDocumentsRetrieved(){
            documentsRetrieved = new ArrayList<>();
    }

    public static void appendToDocumentsRetrieved(Document doc){
        if(documentsRetrieved == null){
            documentsRetrieved = new ArrayList<>();
        }
        documentsRetrieved.add(doc);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static void setFirstLastVisiblePos(int Pos){
        lastFirstVisiblePosition = Pos;
    }

    public static int getLastFirstVisiblePos(){
        return lastFirstVisiblePosition;
    }

}
