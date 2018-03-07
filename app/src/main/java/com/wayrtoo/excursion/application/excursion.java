package com.wayrtoo.excursion.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.support.multidex.MultiDexApplication;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.wayrtoo.excursion.util.FontsOverride;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Map;


public class excursion extends MultiDexApplication {
    public static final String TAG = excursion.class
            .getSimpleName();

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "sessionid";

    private Typeface typeFace;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mContext;

    private static excursion mInstance;
    private SharedPreferences _preferences;

    public static excursion get() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // super.attachBaseContext(LocaleHelper.onAttach(newBase,"fr"));
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext=this;
        FontsOverride.setDefaultFont(mContext, "SERIF", "fonts/DroidSerif-Regular.ttf");
        //FontsOverride.setDefaultFont(this, "SERIF", "fonts/Raleway-Medium.ttf");

        mInstance = this;
        _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRequestQueue = Volley.newRequestQueue(this);
        CookieHandler.setDefault(new CookieManager());
    }

    public static synchronized excursion getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public void setFaturaLight(TextView textView) {
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeFaceRegular = Typeface.createFromAsset(getAssets(), "futura_light_font.ttf");
        textView.setTypeface(typeFaceRegular);
    }
    public void setFaturaMedium(TextView textView) {
        AssetManager am = getApplicationContext().getAssets();
        Typeface typeFaceRegular = Typeface.createFromAsset(getAssets(), "futura_medium_bt.ttf");
        textView.setTypeface(typeFaceRegular);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = _preferences.edit();
                prefEditor.putString(SESSION_COOKIE, cookie);
                prefEditor.commit();
            }
        }
    }
    public final void addSessionCookie(Map<String, String> headers) {
        String sessionId = _preferences.getString(SESSION_COOKIE, "");
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }



}
