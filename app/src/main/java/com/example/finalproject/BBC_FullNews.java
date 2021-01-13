package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BBC_FullNews extends AppCompatActivity {

    TextView headline;
    TextView desc;
    TextView lnk;
    TextView date;
    Button saveArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbc_activity_full_news);
        Intent intent = getIntent();
        String head = intent.getStringExtra("headline");
        String description = intent.getStringExtra("description");
        String link = intent.getStringExtra("link");
        String Date= intent.getStringExtra("date");

        headline=findViewById(R.id.head);
        date= findViewById(R.id.date);

        headline.setText(head);
        date.setText(Date);

        saveArticle = findViewById(R.id.saveNews);

        BBC_DescriptionFragment dFragment = new BBC_DescriptionFragment(); //add a DetailFragment
        Bundle fragmentdesc= new Bundle();
        fragmentdesc.putString("fragmentdesc",description);
        fragmentdesc.putString("fragmentlink",link);

        dFragment.setArguments(fragmentdesc); //pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, dFragment) //Add the fragment in FrameLayout
                .commit(); //actually load the fragment.

        Context context = getApplicationContext();

        saveArticle.setOnClickListener(e -> {
            Intent next = new Intent(BBC_FullNews.this, BBC_SavedNews.class);
            next.putExtra("headline", head);
            next.putExtra("description", description);
            next.putExtra("link", link);
            next.putExtra("date",Date);
            next.setAction("1");
            Toast.makeText(context,getString(R.string.message),
                    Toast.LENGTH_LONG).show();
            startActivity(next);
        });

    }


}
