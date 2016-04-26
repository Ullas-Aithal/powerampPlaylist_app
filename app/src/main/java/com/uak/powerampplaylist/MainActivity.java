package com.uak.powerampplaylist;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        //ListView lv = (ListView) findViewById(R.id.track_list);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick (AdapterView<?> parent,View v,int pos, long id ) {
//
//                //CheckedTextView cv = (CheckedTextView) v;
//
////                    if(cv.isChecked() == true)
////                    {
////                        cv.setChecked(false);
////                    }
////                    else
////                    {
////                        cv.setChecked(true);
////                    }
//                Toast a = Toast.makeText(MainActivity.this, "hi", Toast.LENGTH_LONG);
//                a.show();
//
//
//            }
//        });


    }


    public class TrackAdapter extends ArrayAdapter<Tracks> {
        public TrackAdapter(Context context, ArrayList<Tracks> tracks) {
            super(context, 0, tracks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Tracks tracks = getItem(position);

            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                convertView = li.inflate(R.layout.track_list_view, parent, false);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.track_text_view);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.track_list_cb);

            tv.setText(tracks.track_name);
            cb.setChecked(tracks.track_check);
            return convertView;
        }

    }


    public void getPlaylistsIntent(View view) {
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        Toast t = Toast.makeText(MainActivity.this, "Getting Playlists", Toast.LENGTH_SHORT);
        t.show();

        File file = new File("/sdcard/backups/poweramp_English.txt");

        //Creating a String to append the lines from the file
        //StringBuilder text = new StringBuilder();

        //Creating String Object to pass it for Adapter
        //ArrayList<String> sTracks = new ArrayList<String>();

        //Creating ArrayAdapter for List View
        //ArrayAdapter<String> trackAdapter = new ArrayAdapter<String>(this, R.layout.track_list_view, R.id.track_text_view, sTracks);
        // ArrayAdapter<String> trackAdapter = new ArrayAdapter<String>(this, R.layout.track_list_view, R.id.ct, sTracks);
        //Creating ListView
        //ListView lv = (ListView) findViewById(R.id.track_list);


        int i = 0;

        ArrayList<Tracks> track_list = new ArrayList<Tracks>();
        TrackAdapter ta = new TrackAdapter(this, track_list);
        ListView listView = (ListView) findViewById(R.id.track_list);


        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {


                //  text.append(line);

                //Copy from StringBuilder to String object
                // sTracks.add(i++, line);

                Tracks tr = new Tracks(line, false);
                ta.add(tr);


            }


            br.close();

            listView.setAdapter(ta);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    Toast a = Toast.makeText(MainActivity.this, "here", Toast.LENGTH_LONG);
                    a.show();
                    Tracks track_click = (Tracks) parent.getItemAtPosition(position);

                    if (track_click.track_check) {
                        track_click.setChecked(false);
                    } else
                        track_click.setChecked(true);


                }
            });
//


            Toast b = Toast.makeText(MainActivity.this, "Finished", Toast.LENGTH_SHORT);
            b.show();
        } catch (IOException e) {
            Toast a = Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG);
            a.show();
        }


    }

    public void check(View v) {
        LayoutInflater lf = getLayoutInflater();
        View track_list = lf.inflate(R.layout.track_list_view, null);
        CheckBox cb = (CheckBox) track_list.findViewById(R.id.checkbox);
        ListView lv = (ListView) findViewById(R.id.track_list);
        Toast a = Toast.makeText(MainActivity.this, String.valueOf(lv.getCheckedItemPosition()), Toast.LENGTH_LONG);
        a.show();
        Log.v("id:", String.valueOf(cb.getId()));


    }

    public void selectTracks(View v) {
//
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.uak.powerampplaylist/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.uak.powerampplaylist/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
