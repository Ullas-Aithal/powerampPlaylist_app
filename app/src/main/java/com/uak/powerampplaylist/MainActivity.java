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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
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

    //Declaring Custom Adapter to handle check box and text view
    public class TrackAdapter extends ArrayAdapter<Tracks> {
        public TrackAdapter(Context context, ArrayList<Tracks> tracks) {
            super(context, 0, tracks);
        }

        //Overriding get view.
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Tracks tracks = getItem(position);

            //check if the old view is used
            if (convertView == null) {

                LayoutInflater li = getLayoutInflater();
                convertView = li.inflate(R.layout.track_list_view, parent, false);
            }
            //Get View items
            TextView tv = (TextView) convertView.findViewById(R.id.track_text_view);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.track_list_cb);
            TextView tv1 = (TextView) convertView.findViewById(R.id.track_number);

            //Set values to the View items
            tv.setText(tracks.track_name);
            cb.setChecked(tracks.track_check);
            tv1.setText(String.valueOf(tracks.track_number));
            return convertView;
        }

    }

    //Create and Array of Tracks objects
    ArrayList<Tracks> track_list = new ArrayList<Tracks>();

    int ready_flag = 0;
    int check_count = 0;

    public void getPlaylistsIntent(View view) {

        //Ask permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        Toast t = Toast.makeText(MainActivity.this, "Getting Playlists", Toast.LENGTH_SHORT);
        t.show();

        File file = new File("/sdcard/backups/poweramp_English.txt");


        TrackAdapter ta = new TrackAdapter(this, track_list);
        ListView listView = (ListView) findViewById(R.id.track_list);


        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int i = 1;
            while ((line = br.readLine()) != null) {


                //  text.append(line);

                //Copy from StringBuilder to String object
                // sTracks.add(i++, line);
                //Initialize objects
                Tracks tr = new Tracks(line, false, i++);

                //Add the object to TrackList
                track_list.add(tr);
                //ta.add(tr);


            }


            br.close();

            //Set the adapter with values
            listView.setAdapter(ta);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {


                    Tracks track_click = (Tracks) parent.getItemAtPosition(position);
                    CheckBox cb = (CheckBox) view.findViewById(R.id.track_list_cb);

                    //Handling checkbox clicks
                    if (track_click.track_check) {

                        track_click.setChecked(false);

                        check_count--;
                        if (check_count == 0)
                        {
                            ready_flag = 1;
                            disableViews();

                        }


                    }
                    else
                    {
                        track_click.setChecked(true);
                        check_count++;
                        enableViews();

                        //assign the value to the variable in the object

                    }
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

    public void reorderTracks(ArrayList<Integer> vCheckedTracks, int move) {


        ArrayList<Tracks> temp_track_list = new ArrayList<>(track_list);

        //Create a new Array to hold "to be moved" items
        ArrayList<Tracks> copy_list = new ArrayList<>();

//        Toast a = Toast.makeText(MainActivity.this, "before " + String.valueOf(track_list.size()), Toast.LENGTH_LONG);
//        a.show();

        int trackListLength = 0;
        int move_counter = 0;

        //Sort the selected indexes of the tracks
        Collections.sort(vCheckedTracks);

        //Repositioning the index to where all the tracks have to be moved
        for(int i=0; i<vCheckedTracks.size();i++ )
        {
            if(vCheckedTracks.get(i) < move)
            {
                move_counter++;
            }
            else break;
        }
        move = move - move_counter;


        //Copy all the items that are to be moved
        for (int i = 0; i < vCheckedTracks.size(); i++) {


//            Toast a = Toast.makeText(MainActivity.this, "before " + String.valueOf(vCheckedTracks.get(i).intValue()), Toast.LENGTH_LONG);
//        a.show();
           copy_list.add(track_list.get(vCheckedTracks.get(i)));



        }

        //Remove the tracks from the original array (items get rearranged automatically)
        for (int i = 0; i < vCheckedTracks.size(); i++) {

            track_list.remove(vCheckedTracks.get(i) - i);
        }



        //Add back the removed tracks to the place where it has to be moved
        for (int i = 0; i < vCheckedTracks.size(); i++) {

            track_list.add(move++, copy_list.get(i));
        }


//        Toast b = Toast.makeText(MainActivity.this, "after " + String.valueOf(track_list.size()), Toast.LENGTH_LONG);
//        b.show();


    }

    //Reset the checkboxes
    public void reinitilaizeTracks() {
        for (int i = 0; i < track_list.size(); i++) {
            Tracks t = track_list.get(i);
            t.setChecked(false);

            t.track_number = i+1;

        }
        ready_flag = 0;
        check_count = 0;
        disableViews();
    }

    public void disableViews()
    {
        EditText et = (EditText) findViewById(R.id.move_position);
        et.setVisibility(View.INVISIBLE);
        Button b = (Button) findViewById(R.id.selectedTracks);
        b.setEnabled(false);
    }
    public void enableViews()
    {

        EditText et = (EditText) findViewById(R.id.move_position);
        et.setVisibility(View.VISIBLE);
        et.setText(null);
        Button b = (Button) findViewById(R.id.selectedTracks);
        b.setEnabled(true);
    }

    public void selectTracks(View v) {

        EditText et = (EditText) findViewById(R.id.move_position);
        int move = Integer.parseInt(et.getText().toString());

        if(move <= 0 || move > track_list.size())
        {
            Toast a = Toast.makeText(MainActivity.this,"Please select number between 1 and " + track_list.size(), Toast.LENGTH_LONG);
            a.show();
            return;
        }

        ArrayList<Integer> checkedTracks = new ArrayList<>();


        ListView lv = (ListView) findViewById(R.id.track_list);

        //Get the indexes of to be moved tracks and keep track of these indexes in an array
        for (int i = 0; i < lv.getCount(); i++) {
            Tracks selectedTrack = (Tracks) lv.getItemAtPosition(i);

            if (selectedTrack.track_check) {

                checkedTracks.add(i);
            }

        }
        reorderTracks(checkedTracks,--move);
//        Toast a = Toast.makeText(MainActivity.this, String.valueOf(checkedTracks.size()), Toast.LENGTH_SHORT);
//        a.show();
//
        reinitilaizeTracks();

        //Refresh the View with updates
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
