package com.lydia.digitallibrary;

/**
 * Created by Lydia on 09/07/2016.
 */
public class GlobalVariables {

    private static int currExpandedPos;
    private static int prevExpandedPos;

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

}
