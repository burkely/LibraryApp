package com.lydia.digitallibrary.Helper;

import com.lydia.digitallibrary.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by osulld13 on 19/02/16.
 */
public class AppConstants {
    //Strings
    public static final String documentTransferString = "document";
    public static final int documentImageWidth = 1500;
    public static final int documentImageHeight = 1500;
    public static final int thumbnailImageWidth = 100;
    public static final int thumbnailImageHeight = 100;
    public static final String imageTransitionName = "transition";
    public static final String listOfObjectsInDocumentDelimeter = "info:fedora/dris:";

    public static int popularItemCount = 20;

    public static final String KEY_LAYOUT_MANAGER = "layoutManager";

    public static int CARD_GRID = 1;
    public static int CARD_LIST = 2;
    public static int CARD_LIST_SUB = 3;


    public static ArrayList<String> CATEGORIES = new ArrayList<>(Arrays.asList(
            "Caricatures", "Charts", "Diagrams", "Engravings", "Etchings", "Graphs",
            "Illustrations", "Maps", "Manuscripts", "Music", "Paintings", "Pamphlets",
            "Photographs", "Postcards", "Posters"));

    public static ArrayList<String> myTestData = new ArrayList<>(Arrays.asList(
            "test 1", "test 2", "test 3", "test 4", "test 5",
            "test 6", "test 7", "test 8", "test 9", "test 10",
            "test 11", "test 12", "test 13", "test 14", "test 15"
            ));

    public static int myTestImgs[] = {R.mipmap.img1, R.mipmap.img2, R.mipmap.img2a, R.mipmap.img3,
                                        R.mipmap.img4, R.mipmap.img5, R.mipmap.img5a, R.mipmap.img1,
                                        R.mipmap.img2, R.mipmap.img2a, R.mipmap.img3, R.mipmap.img4,
                                        R.mipmap.img5, R.mipmap.img5a, R.mipmap.img5a};
}
