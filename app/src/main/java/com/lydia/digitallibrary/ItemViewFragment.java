package com.lydia.digitallibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Lydia on 04/07/2016.
 */

public class ItemViewFragment extends Fragment {

    private static final String TAG = "ItemViewFragment";
    private int imgId;
    private String transName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        imgId = bundle.getInt("selected_image");
        transName = bundle.getString("trans name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.item_view_frag, container, false);

        rootView.setTag(TAG);

        // get the imgView
        ImageView img = (ImageView) rootView.findViewById(R.id.itemView);
        img.setImageResource(imgId);
        img.setTransitionName(transName);


        Toast.makeText(getContext(), "in new frag",
                Toast.LENGTH_SHORT).show();

        return rootView;
    }

}
