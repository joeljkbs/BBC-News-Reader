package com.example.finalproject;


import android.content.Context;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BBC_ActivityMain extends AppCompatActivity {
    protected ListView newsList;
    protected ProgressBar progressBar;
    TextView headLines;
    ArrayList<BBC_NewsInfo> newsInfoList = new ArrayList<>();
    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbc_activity_main);

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        newsAdapter = new NewsAdapter(this);
        new NewsQuery().execute();
        newsList=findViewById(R.id.list);
        newsList.setAdapter(newsAdapter);


        progressBar.setVisibility(View.INVISIBLE);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * method to take user to the choosen articles description and link
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent in = new Intent(BBC_ActivityMain.this, BBC_FullNews.class);
                in.putExtra("headline", newsInfoList.get(position).getHeadline());
                in.putExtra("description", newsInfoList.get(position).getDescription());
                in.putExtra("link", newsInfoList.get(position).getLink());
                in.putExtra("date",newsInfoList.get(position).getDate());
                startActivityForResult(in,10);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.bbc_mainmenu,menu);// inflates and displays menu
        return super.onCreateOptionsMenu(menu);
    }

        public boolean onOptionsItemSelected(MenuItem item)  {

            switch (item.getItemId()) {

                case R.id.help:
                    AlertDialog.Builder builder = new AlertDialog.Builder(BBC_ActivityMain.this);
                    builder.setTitle("Instructions");
                    builder.setMessage(getString(R.string.inst)+"\n"+
                    getString(R.string.inst1)+"\n"+
                    getString(R.string.inst2)+"\n"+
                    getString(R.string.inst3));
                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("OK", (dialog, which) -> {
                    });
                    builder.show();
                    return true;


                case R.id.saved:
                    Intent savedNews = new Intent(BBC_ActivityMain.this, BBC_SavedNews.class);
                    savedNews.setAction("0");
                    startActivity(savedNews);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);

            }
        }


    private class NewsAdapter extends ArrayAdapter<BBC_NewsInfo>      {
        public NewsAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){ return newsInfoList.size(); }

        public BBC_NewsInfo getItem(int position){ return newsInfoList.get(position); }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = BBC_ActivityMain.this.getLayoutInflater();
            View result;
            result = inflater.inflate(R.layout.bbc_activity_news_headlines, null);

            headLines = result.findViewById(R.id.headlines);
            headLines.setText(getItem(position).getHeadline()); // get the string at position
            return result;
        }

    }

    private class NewsQuery extends AsyncTask<String, Integer, String>    {
        private String news;
        private String link;
        private String descriptions;
        private String date;
        boolean isItem=false;
        private ArrayList<String> description = new ArrayList<>();
        private ArrayList<String> title = new ArrayList<>();
        private ArrayList<String> links = new ArrayList<>();
        private ArrayList<String> dates = new ArrayList<>();

        /**
         * method done into the background when the application is running
         * make the connection with the given url and read the tags that we want
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings)  {

            try {
                URL url = new URL("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream iStream = conn.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(iStream,"UTF-8");

                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    switch (xpp.getEventType()) {

                        case XmlPullParser.START_TAG:
                            String name = xpp.getName();
                            if(name.equals("item")){
                                isItem=true;
                            } else if (name.equals("title")) {
                                xpp.next();
                                news = xpp.getText();
                                if (!news.contains("BBC | World News") && !news.contains("BBC.ca") && isItem) {
                                    title.add(news);
                                }

                                publishProgress(30);
                                Log.d("News is:", news);

                            } else if (name.equals("pubDate")) {
                                xpp.next();
                                 date = xpp.getText();
                                    dates.add(date);
                                publishProgress((30));

                            } else if (name.equals("link")) {
                                xpp.next();
                                link = xpp.getText();
                                if (!link.equals("\"https://www.bbc.co.uk/news/") && !link.equals("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml ")&& isItem)
                                    links.add(link);
                                publishProgress((30));

                            } else if (name.equals("description")) {
                                xpp.next();
                                descriptions = xpp.getText();
                                if (!descriptions.contains("BBC | World News") && !descriptions.contains("BBC.ca")&&isItem) {
                                    description.add(descriptions);
                                }
                                publishProgress(30);
                            }

                            Log.i("read XML tag:", name);
                            break;
                        case XmlPullParser.TEXT:

                            break;
                    }

                    xpp.next();//look at next XML tag
                }
            } catch (Exception e) {
                Log.i("Exception", e.getMessage());
            }

            return "";
        }

        /**
         * to set the all headlines in to the arraylist
         * @param s
         */
        @Override
        protected void onPostExecute(String s)  {
            super.onPostExecute(s);
            newsInfoList.clear();
            for(int i=0; i<description.size(); i++) {
                newsInfoList.add(new BBC_NewsInfo(title.get(i),description.get(i),links.get(i),dates.get(i)));
                newsAdapter.notifyDataSetChanged();
            }
        }
    }
}




