package com.notmiyouji.newsapp.java.RSSURL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.global.NewsDetailsChrome;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.RSSFeed.RSSObject;

public class FeedAdapterHorizontal extends RecyclerView.Adapter<FeedAdapterHorizontal.FeedViewHolder> {
    private final LayoutInflater inflater;
    RSSObject rssObject;
    AppCompatActivity activity;

    public FeedAdapterHorizontal(RSSObject rssObject, AppCompatActivity activity) {
        this.rssObject = rssObject;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.news_items_horizontal, parent, false);
        return new FeedViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("RtlHardcoded")
    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {

        holder.txtTitle.setText(rssObject.getItems().get(position).getTitle());
        holder.txtPubDate.setText(rssObject.getItems().get(position).getPubDate());
        holder.txtsource.setText(rssObject.getFeed().getTitle());
        String path = rssObject.getItems().get(position).getThumbnail();
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.getImageFromURL(holder.imageView, holder);
        holder.itemView.setOnClickListener(v -> {
            NewsDetailsChrome chromeClient = new NewsDetailsChrome(
                    rssObject.getItems().get(position).getLink(),
                    rssObject.getItems().get(position).getTitle(),
                    rssObject.getItems().get(position).getThumbnail(),
                    rssObject.getItems().get(position).getLink(),
                    rssObject.getItems().get(position).getPubDate(), activity);
            chromeClient.openNewsDetails();
        });
    }

    @Override
    public int getItemCount() {
        try {
            return rssObject.getItems().size();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtPubDate, txtsource;
        public ImageView imageView;
        public Activity activity;

        public FeedViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPubDate = itemView.findViewById(R.id.txtPubDate);
            txtsource = itemView.findViewById(R.id.txtSource);
            imageView = itemView.findViewById(R.id.imgNews);
            //Set Event
        }
    }
}
