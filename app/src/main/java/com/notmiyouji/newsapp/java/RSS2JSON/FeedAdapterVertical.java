package com.notmiyouji.newsapp.java.RSS2JSON;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.UpdateInformation.FavouriteController;
import com.notmiyouji.newsapp.kotlin.OpenActivity.OpenNewsDetails;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.RSSFeed.Category.ItemsCategory;
import com.notmiyouji.newsapp.kotlin.RegEXImage;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FeedAdapterVertical extends RecyclerView.Adapter<FeedAdapterVertical.FeedViewVerticalHolder> {
    private final LayoutInflater inflater;
    List<ItemsCategory> items;
    AppCompatActivity activity;
    GetUserLogined getUserLogined;
    FavouriteController favouriteController;
    String name;

    public FeedAdapterVertical(List<ItemsCategory> items, AppCompatActivity activity, String name) {
        this.items = items;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        getUserLogined = new GetUserLogined(activity);
        favouriteController = new FavouriteController(activity);
        this.name = name;
    }

    @NonNull
    @Override
    public FeedViewVerticalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.news_items_vertical, parent, false);
        return new FeedViewVerticalHolder(itemView);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(FeedViewVerticalHolder holder, int position) {
        holder.txtTitle.setText(items.get(position).getTitle());
        holder.txtPubDate.setText(items.get(position).getPubDate());
        holder.txtsource.setText(name);
        RegEXImage regEXImage = new RegEXImage(items.get(position).getDescription(), items.get(position).getThumbnail());
        String path = regEXImage.regEXImage();
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.getImageFromURL(holder.imageView, holder);
        switch (getUserLogined.getStatus()) {
            case "login":
                favouriteController.checkFavouriteEmailRecycleView(getUserLogined.getUserID(),
                        items.get(position).getLink(),
                        items.get(position).getTitle(),
                        path, name, holder.favouritebtn, holder.unfavouritebtn);
                break;
            case "google":
                favouriteController.checkFavouriteSSORecycleView(getUserLogined.getUserID(),
                        items.get(position).getLink(),
                        items.get(position).getTitle(),
                        path, name, holder.favouritebtn, holder.unfavouritebtn);
                break;
            default:
                holder.favouritebtn.setVisibility(View.VISIBLE);
                holder.unfavouritebtn.setVisibility(View.GONE);
                break;
        }
        holder.favouritebtn.setOnClickListener(v -> {
            switch (getUserLogined.getStatus()) {
                case "login":
                    favouriteController.addFavouriteEmail(getUserLogined.getUserID(),
                            items.get(position).getLink(),
                            items.get(position).getTitle(),
                            path,items.get(position).getPubDate(), name);
                    holder.favouritebtn.setVisibility(View.GONE);
                    holder.unfavouritebtn.setVisibility(View.VISIBLE);
                    break;
                case "google":
                    favouriteController.addFavouriteSSO(getUserLogined.getUserID(),
                            items.get(position).getLink(),
                            items.get(position).getTitle(),
                            path,items.get(position).getPubDate(), name);
                    holder.favouritebtn.setVisibility(View.GONE);
                    holder.unfavouritebtn.setVisibility(View.VISIBLE);
                    break;
                default:
                    Toast.makeText(activity, "Please login to use this feature", Toast.LENGTH_SHORT).show();
                    break;
            }

        });
        holder.unfavouritebtn.setOnClickListener(v -> {
            switch (getUserLogined.getStatus()) {
                case "login":
                    favouriteController.removeFavouriteEmail(getUserLogined.getUserID(),
                            items.get(position).getLink(),
                            items.get(position).getTitle(),
                            path, name);
                    holder.favouritebtn.setVisibility(View.VISIBLE);
                    holder.unfavouritebtn.setVisibility(View.GONE);
                    break;
                case "google":
                    favouriteController.removeFavouriteSSO(getUserLogined.getUserID(),
                            items.get(position).getLink(),
                            items.get(position).getTitle(),
                            path, name);
                    holder.favouritebtn.setVisibility(View.VISIBLE);
                    holder.unfavouritebtn.setVisibility(View.GONE);
                    break;
                default:
                    Toast.makeText(activity, "Please login to use this feature", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        holder.itemView.setOnClickListener(v -> {
            //Webview is cool but it's not the best way to show the news, so I'm going to use a Chrome Custom Tab
            //If your browser not installed, it will open in the webview
            String getpath;
            if (path != null) {
                getpath = path;
            }
            else {
                getpath = "https://techvccloud.mediacdn.vn/2018/3/29/notavailableen-1522298007107364895792-21-0-470-800-crop-152229801290023105615.png";
            }
            OpenNewsDetails openNewsDetails = new OpenNewsDetails(
                    items.get(position).getLink(),
                    items.get(position).getTitle(),
                    getpath,
                    items.get(position).getLink(),
                    items.get(position).getPubDate(), activity);
            openNewsDetails.openNewsDetails();
        });
    }

    @Override
    public int getItemCount() {
        try {
            return items.size();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<ItemsCategory> filterList) {
        this.items = filterList;
        notifyDataSetChanged();
    }

    public static class FeedViewVerticalHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtPubDate, txtsource;
        public ImageView imageView, favouritebtn, unfavouritebtn;
        public Activity activity;

        public FeedViewVerticalHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPubDate = itemView.findViewById(R.id.txtPubDate);
            txtsource = itemView.findViewById(R.id.txtSource);
            imageView = itemView.findViewById(R.id.imgNews);
            favouritebtn = itemView.findViewById(R.id.favouriteBtn);
            unfavouritebtn = itemView.findViewById(R.id.unfavouriteBtn);
            //Set Event
        }
    }
}
