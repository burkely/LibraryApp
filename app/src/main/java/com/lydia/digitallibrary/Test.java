package com.lydia.digitallibrary;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lydia on 22/07/2016.
 */
public class Test {

    List<String> test1 = new ArrayList<>(
            Arrays.asList("Buenos Aires", "Córdoba", "La Plata"));

    List<String> test2= new ArrayList<>(
            Arrays.asList("Buenos FAires", "CórdobaF", "LaFata"));

    List<List<String>> testtest = new ArrayList<>();

    public Test(){
        for(int i = 0; i<2; i++){
            testtest.add(i, test1);
            testtest.add(i, test2);
        }


        for(int i = 0; i < testtest.size(); i++){
            List<String> thing = testtest.get(i);
            testtest.get(i).clear();
        }
        testtest.clear();

    }

}
