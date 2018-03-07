package com.wayrtoo.excursion.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ishwar on 23/12/2017.
 */
public class ActivitiesOperationTimingModel implements Serializable{


    private String Day  ;
    private String From_Time;
    private String To_Time;
    private ArrayList<TimingModel> booking_timings  ;

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getFrom_Time() {
        return From_Time;
    }

    public void setFrom_Time(String from_Time) {
        From_Time = from_Time;
    }

    public String getTo_Time() {
        return To_Time;
    }

    public void setTo_Time(String to_Time) {
        To_Time = to_Time;
    }

    public ArrayList<TimingModel> getBooking_timings() {
        return booking_timings;
    }

    public void setBooking_timings(ArrayList<TimingModel> booking_timings) {
        this.booking_timings = booking_timings;
    }

   /* public String getBooking_timings() {
        return booking_timings;
    }

    public void setBooking_timings(String booking_timings) {
        this.booking_timings = booking_timings;
    }*/
}

