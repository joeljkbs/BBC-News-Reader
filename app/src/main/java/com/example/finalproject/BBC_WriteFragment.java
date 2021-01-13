package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;



public class BBC_WriteFragment extends Fragment {

    private EditText shared;


    // TODO: Rename and change types of parameters


    public BBC_WriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        View result=inflater.inflate(R.layout.bbc_fragment_write, container, false);
        shared=result.findViewById(R.id.feedback);
        editor.putString("save",shared.getText().toString());
        editor.commit();
        result.findViewById(R.id.send).setOnClickListener(v -> {
            shared.setText(pref.getString("save",null));
        });





        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
