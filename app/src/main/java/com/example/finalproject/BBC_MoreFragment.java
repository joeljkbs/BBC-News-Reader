package com.example.finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BBC_MoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BBC_MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BBC_MoreFragment extends Fragment {


    public BBC_MoreFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bbc_fragment_more, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
}