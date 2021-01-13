package com.example.finalproject;

import  android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BBC_DescriptionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BBC_DescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BBC_DescriptionFragment extends Fragment {
    private AppCompatActivity parentActivity;
    private String Description;
    private String Link;
    private TextView desc;
    private TextView link;
    private Bundle data;


    public BBC_DescriptionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data = getArguments();
        Description=data.getString("fragmentdesc");
        Link=data.getString("fragmentlink");

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.bbc_fragment_description, container, false);

        desc=result.findViewById(R.id.fragmentdescription);
        link= result.findViewById(R.id.url);
        desc.setText(Description);
        link.setText(Link);

        return result;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }
}
