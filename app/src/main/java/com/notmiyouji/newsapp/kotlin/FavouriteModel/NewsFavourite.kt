package com.notmiyouji.newsapp.kotlin.FavouriteModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewsFavourite {
    // SerializedName and Expose: sync_id, user_id, url, title, image_url, source_name, status
    @SerializedName("sync_id")
    @Expose
    var syncId: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null
    @SerializedName("pubdate")
    @Expose
    var pubdate: String? = null
    @SerializedName("source_name")
    @Expose
    var sourceName: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
}