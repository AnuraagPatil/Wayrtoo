package com.wayrtoo.excursion.models;

import java.io.Serializable;

/**
 * Created by Ishwar on 23/12/2017.
 */
public class ActivitiesCancellatioPolicyModel implements Serializable{

    private String charge  ;
    private String chargeable_by_percentage;
    private String from_hr;
    private String to_hr  ;

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getChargeable_by_percentage() {
        return chargeable_by_percentage;
    }

    public void setChargeable_by_percentage(String chargeable_by_percentage) {
        this.chargeable_by_percentage = chargeable_by_percentage;
    }

    public String getFrom_hr() {
        return from_hr;
    }

    public void setFrom_hr(String from_hr) {
        this.from_hr = from_hr;
    }

    public String getTo_hr() {
        return to_hr;
    }

    public void setTo_hr(String to_hr) {
        this.to_hr = to_hr;
    }
}

