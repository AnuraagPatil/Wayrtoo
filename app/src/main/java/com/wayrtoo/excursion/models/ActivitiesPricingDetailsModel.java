package com.wayrtoo.excursion.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ishwar on 23/12/2017.
 */
public class ActivitiesPricingDetailsModel implements Serializable{


    private String currency  ;
    private String from_date;
    private String to_date;
    private String adult_price  ;
    private String child_price  ;
    private String from_pax_count;
    private String infant_price;
    private String inventory;
    private String to_pax_count  ;
    private ArrayList<ActivitiesOperationTimingModel> operationTimingModels;

    public ArrayList<ActivitiesOperationTimingModel> getOperationTimingModels() {
        return operationTimingModels;
    }

    public void setOperationTimingModels(ArrayList<ActivitiesOperationTimingModel> operationTimingModels) {
        this.operationTimingModels = operationTimingModels;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getAdult_price() {
        return adult_price;
    }

    public void setAdult_price(String adult_price) {
        this.adult_price = adult_price;
    }

    public String getChild_price() {
        return child_price;
    }

    public void setChild_price(String child_price) {
        this.child_price = child_price;
    }

    public String getFrom_pax_count() {
        return from_pax_count;
    }

    public void setFrom_pax_count(String from_pax_count) {
        this.from_pax_count = from_pax_count;
    }

    public String getInfant_price() {
        return infant_price;
    }

    public void setInfant_price(String infant_price) {
        this.infant_price = infant_price;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getTo_pax_count() {
        return to_pax_count;
    }

    public void setTo_pax_count(String to_pax_count) {
        this.to_pax_count = to_pax_count;
    }
}

