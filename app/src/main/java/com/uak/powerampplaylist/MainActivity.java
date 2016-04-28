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
import android.widget.ListAdapter;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    ArrayList<Tracks> track_list = new ArrayList<Tracks>();


    public void getPlaylistsIntent(View view) {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        Toast t = Toast.makeText(MainActivity.this, "Getting Playlists", Toast.LENGTH_SHORT);
        t.show();

        File file = new File("/sdcard/backups/poweramp_English.txt");

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
                track_list.add(tr);
                //ta.add(tr);


            }


            br.close();

            listView.setAdapter(ta);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {


                    Tracks track_click = (Tracks) parent.getItemAtPosition(position);
                    CheckBox cb = (CheckBox) view.findViewById(R.id.track_list_cb);

                    if (track_click.track_check) {
                        track_click.setChecked(false);

                    } else
                        track_click.setChecked(true);


                    cb.setChecked(track_click.track_check);

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

//    public void check(View v) {
//        LayoutInflater lf = getLayoutInflater();
//        View track_list = lf.inflate(R.layout.track_list_view, null);
//        CheckBox cb = (CheckBox) track_list.findViewById(R.id.checkbox);
//        ListView lv = (ListView) findViewById(R.id.track_list);
//        Toast a = Toast.makeText(MainActivity.this, String.valueOf(lv.getCheckedItemPosition()), Toast.LENGTH_LONG);
//        a.show();
//        Log.v("id:", String.valueOf(cb.getId()));
//
//
//    }

    public void reorderTracks(ArrayList<Integer> vCheckedTracks) {
        ArrayList<Tracks> temp_track_list = new ArrayList<>(track_list);
        ArrayList<Tracks> copy_list = new ArrayList<>();

//        Toast a = Toast.makeText(MainActivity.this, "before " + String.valueOf(track_list.size()), Toast.LENGTH_LONG);
//        a.show();

        int trackListLength = 0;

        Collections.sort(vCheckedTracks);

        for (int i = 0; i < vCheckedTracks.size(); i++) {


//            Toast a = Toast.makeText(MainActivity.this, "before " + String.valueOf(vCheckedTracks.get(i).intValue()), Toast.LENGTH_LONG);
//        a.show();
           copy_list.add(track_list.get(vCheckedTracks.get(i)));



        }


        for (int i = 0; i < vCheckedTracks.size(); i++) {

            track_list.remove(vCheckedTracks.get(i) - i);
        }
        trackListLength = track_list.size();

        for (int i = 0; i < vCheckedTracks.size(); i++) {

            track_list.add(trackListLength++, copy_list.get(i));
        }


//        Toast b = Toast.makeText(MainActivity.this, "after " + String.valueOf(track_list.size()), Toast.LENGTH_LONG);
//        b.show();


    }

    public void reinitilaizeTracks() {
        for (int i = 0; i < track_list.size(); i++) {
            Tracks t = track_list.get(i);
            t.setChecked(false);

        }
    }

    public void selectTracks(View v) {

        ArrayList<Integer> checkedTracks = new ArrayList<>();


        ListView lv = (ListView) findViewById(R.id.track_list);

        for (int i = 0; i < lv.getCount(); i++) {
            Tracks selectedTrack = (Tracks) lv.getItemAtPosition(i);

            if (selectedTrack.track_check) {

                checkedTracks.add(i);
            }

        }
        reorderTracks(checkedTracks);
//        Toast a = Toast.makeText(MainActivity.this, String.valueOf(checkedTracks.size()), Toast.LENGTH_SHORT);
//        a.show();
//
        reinitilaizeTracks();

        TrackAdapter ta = new TrackAdapter(this, track_list);
        lv.setAdapter(ta);


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
