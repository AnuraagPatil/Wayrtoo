package com.wayrtoo.excursion.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ishwar on 27/12/2017.
 */
public class ActivitiesDetailsModel implements Serializable{

    private String Title;
    private String Website;
    private String address;
    private String category;
    private String city  ;
    private String currency;
    private String day;
    private String hour;
    private String min;
    private String featured_image;
    private String name;
    private String rating;
    private String short_description;
    private String starting_from_price;
    private String old_price;
    private String new_price;
    private String latitude;
    private String longitude;
    private String discount;
    private String available_for_child;
    private String description;
    private String child_max_age;
    private String child_min_age;
    private ArrayList<String> highlights;
    private ArrayList<String> inclusion;
    private ArrayList<ActivitiesOperationTimingModel> operationTimingModels;
    private ArrayList<ActivitiesCancellatioPolicyModel> cancellatioPolicyModels;
    private ArrayList<ImagesListModel> imagesListModels;

    public ArrayList<ImagesListModel> getImagesListModels() {
        return imagesListModels;
    }

    public void setImagesListModels(ArrayList<ImagesListModel> imagesListModels) {
        this.imagesListModels = imagesListModels;
    }

    public ArrayList<ActivitiesOperationTimingModel> getOperationTimingModels() {
        return operationTimingModels;
    }

    public void setOperationTimingModels(ArrayList<ActivitiesOperationTimingModel> operationTimingModels) {
        this.operationTimingModels = operationTimingModels;
    }

    public ArrayList<ActivitiesCancellatioPolicyModel> getCancellatioPolicyModels() {
        return cancellatioPolicyModels;
    }

    public void setCancellatioPolicyModels(ArrayList<ActivitiesCancellatioPolicyModel> cancellatioPolicyModels) {
        this.cancellatioPolicyModels = cancellatioPolicyModels;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getStarting_from_price() {
        return starting_from_price;
    }

    public void setStarting_from_price(String starting_from_price) {
        this.starting_from_price = starting_from_price;
    }

    public String getAvailable_for_child() {
        return available_for_child;
    }

    public void setAvailable_for_child(String available_for_child) {
        this.available_for_child = available_for_child;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getNew_price() {
        return new_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getChild_max_age() {
        return child_max_age;
    }

    public void setChild_max_age(String child_max_age) {
        this.child_max_age = child_max_age;
    }

    public String getChild_min_age() {
        return child_min_age;
    }

    public void setChild_min_age(String child_min_age) {
        this.child_min_age = child_min_age;
    }

    public ArrayList<String> getHighlights() {
        return highlights;
    }

    public void setHighlights(ArrayList<String> highlights) {
        this.highlights = highlights;
    }

    public ArrayList<String> getInclusion() {
        return inclusion;
    }

    public void setInclusion(ArrayList<String> inclusion) {
        this.inclusion = inclusion;
    }
}

