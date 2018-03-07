package com.wayrtoo.excursion.models;

import java.io.Serializable;

/**
 * Created by Ishwar on 23/12/2017.
 */
public class CityListModel implements Serializable{


    private String city_id  ;
    private String city_name;
    private String grn_country_code;
    private String country_name;
    private String featured_image  ;
    private String tbo_country_code;
    private String city_Short_Name;


    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public String getTbo_country_code() {
        return tbo_country_code;
    }

    public void setTbo_country_code(String tbo_country_code) {
        this.tbo_country_code = tbo_country_code;
    }

    public String getCity_Short_Name() {
        return city_Short_Name;
    }

    public void setCity_Short_Name(String city_Short_Name) {
        this.city_Short_Name = city_Short_Name;
    }

    public String getGrn_country_code() {
        return grn_country_code;
    }

    public void setGrn_country_code(String grn_country_code) {
        this.grn_country_code = grn_country_code;
    }
}

