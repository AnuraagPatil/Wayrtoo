package com.wayrtoo.excursion.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Ishwar on 10-06-2017.
 */

public class SessionManager {
    private static final String PREF_NAME = "Wayrtoo";
    private static final String FIRSTTIME = "isFirstTime";
    private static final String BASE_URL = "isBaseUrl";
    private static final String COUNTRY_NAME = "isContryName";
    private static final String SELECT_FRAGMENT = "isSelectFragment";
    private static final String CITY_STATUS = "isCityStatus";
    private static final String CITY_LIST = "isCityList";
    private static final String CITY_ID = "isCityId";
    private static final String CITY_NAME = "isCityName";
    private static final String CITY_IMAGE = "isCityImage";
    private static final String ACTIVITY_STATUS = "isActivityStatus";
    private static final String ACTIVITY_ABU_DHABI = "isAbudhabi";
    private static final String ACTIVITY_DUBAI = "isDubai";
    private static final String ACTIVITY_BALI = "isBali";
    private static final String ACTIVITY_SINGAPORE = "isSingapore";
    private static final String ACTIVITY_BANGKOK = "isBankok";
    private static final String ACTIVITY_HONG_KONG = "isHongkong";
    private static final String ACTIVITY_KUALA_LUMPUR = "isKuala";
    private static final String ACTIVITY_LANGKAWI = "isLangkawi";
    private static final String ACTIVITY_PENANG = "isPenang";
    private static final String SORTING_ID = "isSorting";
    private static final String FILTER_MIN = "isFilterMin";
    private static final String FILTER_MAX = "isFilterMaxg";

    private static String TAG = SessionManager.class.getSimpleName();

    Context _context;
    int PRIVATE_MODE = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirsttime(boolean isLoggedIn) {

        editor.putBoolean(FIRSTTIME, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");

    }

    public boolean setBaseUrl(String key) {
        editor.putString(BASE_URL, key);
        if (editor.commit()) {
            Log.d(TAG, "Authorization key is set!");
            return true;
        }
        return false;
    }

    public boolean setCityID(String key) {
        editor.putString(CITY_ID, key);
        if (editor.commit()) {
            Log.d(TAG, "City ID key is set!");
            return true;
        }
        return false;
    }

    public boolean setCityName(String key) {
        editor.putString(CITY_NAME, key);
        if (editor.commit()) {
            Log.d(TAG, "City Name key is set!");
            return true;
        }
        return false;
    }

    public boolean setCityImage(String key) {
        editor.putString(CITY_IMAGE, key);
        if (editor.commit()) {
            Log.d(TAG, "City Image key is set!");
            return true;
        }
        return false;
    }

    public boolean setCountryName(String key) {
        editor.putString(COUNTRY_NAME, key);
        if (editor.commit()) {
            Log.d(TAG, "Country Name is set!");
            return true;
        }
        return false;
    }

    public boolean setSelectFragmentPosition(String key) {
        editor.putString(SELECT_FRAGMENT, key);
        if (editor.commit()) {
            Log.d(TAG, "SELECT_FRAGMENT is set!");
            return true;
        }
        return false;
    }

    public boolean setCityStatus(String key) {
        editor.putString(CITY_STATUS, key);
        if (editor.commit()) {
            Log.d(TAG, "CityStatus is set!");
            return true;
        }
        return false;
    }

    public boolean setCityList(String key) {
        editor.putString(CITY_LIST, key);
        if (editor.commit()) {
            Log.d(TAG, "City List is set!");
            return true;
        }
        return false;
    }

    public boolean setActivtityStatus(String key) {
        editor.putString(ACTIVITY_STATUS, key);
        if (editor.commit()) {
            Log.d(TAG, "CityStatus is set!");
            return true;
        }
        return false;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean setCityAbuDhabi(String key) {
        editor.putString(ACTIVITY_ABU_DHABI, key);
        if (editor.commit()) {
            Log.d(TAG, "AbuDhabi City List is set!");
            return true;
        }
        return false;
    }

    public boolean setCityDubai(String key) {
        editor.putString(ACTIVITY_DUBAI, key);
        if (editor.commit()) {
            Log.d(TAG, "Dubai City List is set!");
            return true;
        }
        return false;
    }

    public boolean setCityBali(String key) {
        editor.putString(ACTIVITY_BALI, key);
        if (editor.commit()) {
            Log.d(TAG, "Bali City List is set!");
            return true;
        }
        return false;
    }

    public boolean setCitySingapore(String key) {
        editor.putString(ACTIVITY_SINGAPORE, key);
        if (editor.commit()) {
            Log.d(TAG, "Singapore City List is set!");
            return true;
        }
        return false;
    }

    public boolean setCityBankok(String key) {
        editor.putString(ACTIVITY_BANGKOK, key);
        if (editor.commit()) {
            Log.d(TAG, "Bankok City List is set!");
            return true;
        }
        return false;
    }

    public boolean setCityHongKong(String key) {
        editor.putString(ACTIVITY_HONG_KONG, key);
        if (editor.commit()) {
            Log.d(TAG, "HongKong City List is set!");
            return true;
        }
        return false;
    }

    public boolean setCityKualaLumpur(String key) {
        editor.putString(ACTIVITY_KUALA_LUMPUR, key);
        if (editor.commit()) {
            Log.d(TAG, "KualaLumpur City List is set!");
            return true;
        }
        return false;
    }

    public boolean setCityLangKawi(String key) {
        editor.putString(ACTIVITY_LANGKAWI, key);
        if (editor.commit()) {
            Log.d(TAG, "LangKawi City List is set!");
            return true;
        }
        return false;
    }

    public boolean setCityPenang(String key) {
        editor.putString(ACTIVITY_PENANG, key);
        if (editor.commit()) {
            Log.d(TAG, "Penang City List is set!");
            return true;
        }
        return false;
    }

    public boolean setSorting_Position(String key) {
        editor.putString(SORTING_ID, key);
        if (editor.commit()) {
            Log.d(TAG, "SORTING_ID  is set!");
            return true;
        }
        return false;
    }

    public boolean setFilterMin(String key) {
        editor.putString(FILTER_MIN, key);
        if (editor.commit()) {
            Log.d(TAG, "FILTER_MIN  is set!");
            return true;
        }
        return false;
    }

    public boolean setFilterMax(String key) {
        editor.putString(FILTER_MAX, key);
        if (editor.commit()) {
            Log.d(TAG, "FILTER_MAX  is set!");
            return true;
        }
        return false;
    }

    public boolean getFirstTime() {
        return pref.getBoolean(FIRSTTIME, false);
    }

    public String getCityId() {
        return pref.getString(CITY_ID, "6");
    }

    public String getCityName() {
        return pref.getString(CITY_NAME, "Dubai");
    }

    public String getCityImage() {
        return pref.getString(CITY_IMAGE, "https://guiddooimages.blob.core.windows.net/booking-engine-images/dubai.jpg");
    }

    public String getBaseUrl() {
        return pref.getString(BASE_URL, null);
    }

    public String getCountryName() {
        return pref.getString(COUNTRY_NAME, null);
    }

    public String getSelectFragmentPosition() {
        return pref.getString(SELECT_FRAGMENT, "0");
    }

    public String getCityStatus() {
        return pref.getString(CITY_STATUS, "false");
    }

    public String getCityList() {
        return pref.getString(CITY_LIST, "");
    }

    public String getActivityStatus() {
        return pref.getString(ACTIVITY_STATUS, "false");
    }

    public String getSortingPosition() {
        return pref.getString(SORTING_ID, "");
    }

    public String getFilterMin() {
        return pref.getString(FILTER_MIN, "0");
    }

    public String getFilterMax() {
        return pref.getString(FILTER_MAX, "5000");
    }


    /*  City get All List */

    public String getCityAbuDhabi() {
        return pref.getString(ACTIVITY_ABU_DHABI, "");
    }

    public String getCityDubai() {
        return pref.getString(ACTIVITY_DUBAI, "");
    }

    public String getCityBali() {
        return pref.getString(ACTIVITY_BALI, "");
    }

    public String getCitySingapore() {
        return pref.getString(ACTIVITY_SINGAPORE, "");
    }

    public String getCityBangkok() {
        return pref.getString(ACTIVITY_BANGKOK, "");
    }

    public String getCityHongKong() {
        return pref.getString(ACTIVITY_HONG_KONG, "");
    }

    public String getCityKaulaLumpur() {
        return pref.getString(ACTIVITY_KUALA_LUMPUR, "");
    }

    public String getCityLangkawi() {
        return pref.getString(ACTIVITY_LANGKAWI, "");
    }

    public String getCityPenang() {
        return pref.getString(ACTIVITY_PENANG, "");
    }


    public boolean deleteToken() {
        editor.remove(CITY_ID);
        if (editor.commit()) {
            Log.d(TAG, "Authorized key is deleted");
            return true;
        }
        return false;
    }
}
