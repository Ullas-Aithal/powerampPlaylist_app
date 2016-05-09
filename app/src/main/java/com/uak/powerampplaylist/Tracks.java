package com.uak.powerampplaylist;

/**
 * Created by uak on 4/26/2016.
 */
public class Tracks {

    public String track_name;
    public boolean track_check;
    public int track_number;

    public Tracks(String t_name, boolean t_check, int t_number)
    {
        this.track_name = t_name;
        this.track_check = t_check;
        this.track_number = t_number;
    }


    public void setChecked(boolean t_check)
    {
        this.track_check = t_check;

    }
    public void setNumber(int t_number)
    {

        this.track_number = t_number;
    }



}
