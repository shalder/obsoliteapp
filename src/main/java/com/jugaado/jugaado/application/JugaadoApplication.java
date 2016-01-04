package com.jugaado.jugaado.application;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by ravivooda on 9/24/15.
 */
public class JugaadoApplication extends Application {
    private Tracker analyticsTracker;

    synchronized public Tracker getAnalyticsTracker(){
        if (analyticsTracker == null){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analyticsTracker = analytics.newTracker("");
        }
        return analyticsTracker;
    }
}
