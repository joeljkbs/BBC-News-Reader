package com.example.finalproject;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BBC_SavedNews extends AppCompatActivity {
    ListView view;
    ArrayList<BBC_NewsInfo> titles = new ArrayList<>();
    TextView headLines;
    TextView description;
    TextView link;
    TextView Date;
    NewsAdapter ns;
    ContentValues databaseValues;
    SQLiteDatabase db;
    Database dbOpener;
    BBC_NewsInfo n;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbc_activity_saved_news);
        view = findViewById(R.id.savedlist);
        view.setOnItemLongClickListener((parent, view, position, id) -> {

            androidx.appcompat.app.AlertDialog.Builder alert= new androidx.appcompat.app.AlertDialog.Builder(BBC_SavedNews.this);
            alert.setTitle("Delete");
            alert.setMessage("Do you want to delete this saved article on row"+position);
            alert.setPositiveButton("Yes", (dialog,which)->{
                db.delete(dbOpener.TABLE_NAME, dbOpener._ID + "= ?", new String[]{Long.toString(titles.get(position).getId())});
                titles.remove(position);
                ns.notifyDataSetChanged();
            });
            alert.setNegativeButton("No",(dialog,which)->dialog.dismiss());
            alert.show();
            return true;
        });

        view.setOnItemClickListener((parent, view, position, id) ->{
            String url= titles.get(position).getLink();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        n = new BBC_NewsInfo();

        dbOpener = new Database(this);
        db = dbOpener.getWritableDatabase();
        Cursor cursor = db.query(false, dbOpener.TABLE_NAME, new String[]{dbOpener._ID, dbOpener.COL_HEADLINES, dbOpener.COL_DESCRIPTION, dbOpener.COL_URL, dbOpener.COL_DATE}, null, null, null, null, null, null, null);
        databaseValues = new ContentValues();

        if (getIntent().getAction() == "1") {

            databaseValues.put(dbOpener.COL_HEADLINES, getIntent().getStringExtra("headline"));
            databaseValues.put(dbOpener.COL_DESCRIPTION, getIntent().getStringExtra("description"));
            databaseValues.put(dbOpener.COL_URL, getIntent().getStringExtra("link"));
            databaseValues.put(dbOpener.COL_DATE,getIntent().getStringExtra("date"));

            id = db.insert(dbOpener.TABLE_NAME, null, databaseValues);
        }

        while (cursor.moveToNext()) {
            int headlinesColumnIndex = cursor.getColumnIndex(dbOpener.COL_HEADLINES);
            int descriptionColumnIndex = cursor.getColumnIndex(dbOpener.COL_DESCRIPTION);
            int linkColIndex = cursor.getColumnIndex(dbOpener.COL_URL);
            int dateColIndex = cursor.getColumnIndex(dbOpener.COL_DATE);
            int idColIndex = cursor.getColumnIndex(dbOpener._ID);

            String head = cursor.getString(headlinesColumnIndex);
            String desc = cursor.getString(descriptionColumnIndex);
            String url = cursor.getString(linkColIndex);
            String date= cursor.getString(dateColIndex);
            Long id = cursor.getLong(idColIndex);


            titles.add(new BBC_NewsInfo(id, head, desc, url,date));

        }


        ns = new NewsAdapter(titles, this);
        view.setAdapter(ns);

        Toast.makeText(this,getString(R.string.ArticlesSaved)+" "+titles.size(),Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.bbc_moremenu,menu);// inflates and displays menu
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)  {

        switch (item.getItemId()) {

            case R.id.help:
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(BBC_SavedNews.this);
                builder.setTitle("Instructions");
                builder.setMessage(getString(R.string.inst)+"\n"+
                        getString(R.string.inst1)+"\n"+
                        getString(R.string.inst2)+"\n"+
                        getString(R.string.inst3)+"\n"+getString(R.string.inst4)+"\n"+getString(R.string.inst5));

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("OK", (dialog, which) -> {
                });
                builder.show();
                return true;

            case R.id.more:
                final Dialog fbDialogue = new Dialog(BBC_SavedNews.this, android.R.style.Theme_Black);
                fbDialogue.setContentView(R.layout.bbc_fragment_more);
                BBC_MoreFragment dFragment = new BBC_MoreFragment(); //add a DetailFragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.More, dFragment) //Add the fragment in FrameLayout
                        .commit();//actually load the fragment.
                fbDialogue.setCancelable(true);
                fbDialogue.show();

            case R.id.contact:
                final Dialog dialogue = new Dialog(BBC_SavedNews.this, android.R.style.Theme_Black);
                dialogue.setContentView(R.layout.bbc_fragment_write);
                BBC_WriteFragment write = new BBC_WriteFragment(); //add a DetailFragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.writee, write) //Add the fragment in FrameLayout
                        .commit();//actually load the fragment.
                dialogue.setCancelable(true);
                dialogue.show();


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private class NewsAdapter extends ArrayAdapter<BBC_NewsInfo>  {

        public NewsAdapter(ArrayList<BBC_NewsInfo> data, Context ctx) {
            super(ctx, 0,data);
        }

        /**
         * to read titles from arraylist
         * @return
         */
        public int getCount(){
            return titles.size();
        }

        /**
         * to display the news titles saved into the arraylist
         * @param position
         * @return
         */
        public BBC_NewsInfo getItem(int position){
            return titles.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = BBC_SavedNews.this.getLayoutInflater();
            View result;
            result = inflater.inflate(R.layout.bbc_saved_news_layout,null);

            headLines = result.findViewById(R.id.savedHead);
            description=result.findViewById(R.id.savedDesc);
            link=result.findViewById(R.id.savedLink);
            Date=result.findViewById(R.id.savedDate);

            headLines.setText(getItem(position).getHeadline()  ); // get the string at position
            description.setText(getItem(position).getDescription());
            link.setText(getItem(position).getLink());
            Date.setText(getItem(position).getDate());
            return result;
        }

    }

    public class Database extends SQLiteOpenHelper  {

        protected final static String DATABASE_NAME = "NewsDB";
        protected final static int VERSION_NUM = 2;
        public final static String TABLE_NAME = "News";
        public final static String COL_HEADLINES = "HEADLINES";
        public final static String COL_DESCRIPTION = "DESCRIPTION";
        public final static String COL_URL = "URL";
        public final static String COL_DATE = "DATE";
        public final static String _ID = "_id";


        public Database(Context ctx)
        {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + "("+_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_HEADLINES + " text,"+ COL_DESCRIPTION  + " text,"+ COL_URL  + " text,"+ COL_DATE +" text); ");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
