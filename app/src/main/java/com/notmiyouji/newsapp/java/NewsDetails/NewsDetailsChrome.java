package com.notmiyouji.newsapp.java.NewsDetails;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;

import com.notmiyouji.newsapp.R;

//Introducing the Chrome Custom Tabs. Faster and more secure than webview.
public class NewsDetailsChrome {
    private final String url;
    private final String title;
    private final String img;
    private final String source;
    private final String pubdate;
    private final Activity activity;
    Intent intent;
    public NewsDetailsChrome(String url, String title, String img, String source, String pubdate, Activity activity) {
        this.url = url;
        this.title = title;
        this.img = img;
        this.source = source;
        this.pubdate = pubdate;
        this.activity = activity;
    }

    public void openNewsDetails() {
        //Make sure chrome is installed
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setStartAnimations(activity, android.R.anim.fade_in, android.R.anim.fade_out);
        builder.setExitAnimations(activity, android.R.anim.fade_in, android.R.anim.fade_out);
        intent = new Intent(activity, CustomTabsBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.addMenuItem(activity.getString(R.string.favourite_menu), pendingIntent);
        builder.setUrlBarHidingEnabled(false);
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    //This is just a demo for favourite menu item
    public static class CustomTabsBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String url = intent.getDataString();

            Toast.makeText(context, "Copy link pressed. URL = " + url, Toast.LENGTH_SHORT).show();

            //Here you can copy the URL to the clipboard
        }
    }

}