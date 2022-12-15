package com.notmiyouji.newsapp.NewsAPI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.notmiyouji.newsapp.NewsAPI.NewsAPIModels.Article;
import com.notmiyouji.newsapp.NewsAPI.NewsAPIModels.Category.ArticleCategory;
import com.notmiyouji.newsapp.NewsAPI.NewsAPIModels.Category.NewsCategory;
import com.notmiyouji.newsapp.NewsAPI.NewsAPIModels.News;
import com.notmiyouji.newsapp.RSSURL.FavouriteNews;
import com.notmiyouji.newsapp.RSSURL.HomePage;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.RSSURL.SourceNewsList;
import com.notmiyouji.newsapp.global.NavigationPane;
import com.notmiyouji.newsapp.global.recycleviewadapter.newsapi.NewsAPITypeAdapter;
import com.notmiyouji.newsapp.global.recycleviewadapter.newsapi.NewsAdapterHorizontal;
import com.notmiyouji.newsapp.global.recycleviewadapter.newsapi.NewsAdapterVertical;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAPI_Page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    //Initialization variable
    public static final String API_KEY = new NewsAPIKey().getNEWSAPIKEY(); //the newsAPI key is here
    DrawerLayout drawerNewsAPI;
    NavigationView navigationView;
    LinearLayoutManager newstypeLayout, newsAPIHorizontalLayout, newsAPIVerticalLayout;
    RecyclerView newstypeView, newsViewHorizontal, newsViewVertical;
    Toolbar toolbar;
    NavigationPane navigationPane;
    NewsAPITypeAdapter newsAPITypeAdapter;
    Intent intent;
    List<Article> articles = new ArrayList<>(); //not inclue category
    List<ArticleCategory> articles2 = new ArrayList<>(); //include category
    NewsAdapterHorizontal newsAdapterHorizontal;
    NewsAdapterVertical newsAdapterVertical;
    String TAG = NewsAPI_Page.class.getSimpleName();
    APIInterface apiInterface = NewsAPIKey.getAPIClient().create(APIInterface.class);
    Call<News> call;
    Call<NewsCategory> callCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_api_page);
        //Hooks
        drawerNewsAPI = findViewById(R.id.newsapi_page);
        navigationView = findViewById(R.id.nav_pane_view);
        toolbar = findViewById(R.id.nav_button);
        newstypeView = findViewById(R.id.news_type);
        newsViewHorizontal = findViewById(R.id.cardnews_view_horizontal);
        newsViewVertical = findViewById(R.id.cardnews_view_vertical);
        //create navigation drawer
        navigationPane = new NavigationPane(drawerNewsAPI, this, toolbar, navigationView, R.id.newsapi_menu);
        navigationPane.CallFromUser();
        //NewsCategory Type List
        newsAPITypeAdapter = new NewsAPITypeAdapter(this);
        newstypeLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        newstypeView.setLayoutManager(newstypeLayout);
        newstypeView.setAdapter(newsAPITypeAdapter);
        //Loading Messeage
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading, please wait...");
        mDialog.show();
        //Load JSONData and apply to RecycleView Horizontal Lastest NewsCategory
        LoadJSONLastestNews(this, mDialog);
        //Load JSONData Business NewsCategory and apply to RecycleView Vertical Lastest NewsCategory
        LoadJSONCategory(this, mDialog, "business");
    }

    private void LoadJSONCategory(AppCompatActivity activity, ProgressDialog mDialog, String categoryname) {
        String category = categoryname;
        Thread loadSourceGeneral = new Thread(new Runnable() {
            @Override
            public void run() {
                callCategory = apiInterface.getNewsCategory("us", "business", API_KEY);
                callCategory.enqueue(new Callback<NewsCategory>() {
                    @Override
                    public void onResponse(@NonNull Call<NewsCategory> callCategory, @NonNull Response<NewsCategory> response2) {
                        if (response2.isSuccessful()) {
                            assert response2.body() != null;
                            if (response2.body().getArticle() != null) {
                                if (!articles2.isEmpty()) {
                                    articles2.clear();
                                }
                                articles2 = response2.body().getArticle();
                                newsAPIVerticalLayout = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                                newsViewVertical.setLayoutManager(newsAPIVerticalLayout);
                                newsAdapterVertical = new NewsAdapterVertical(articles2, activity);
                                newsViewVertical.setAdapter(newsAdapterVertical);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsCategory> call, Throwable t) {
                    }
                    });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsViewVertical.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss);
                    }
                });
            }
        });
        loadSourceGeneral.start();
    }

    public void LoadJSONLastestNews(AppCompatActivity activity, ProgressDialog mDialog){
        Thread loadSourceAPI = new Thread(new Runnable() {
            @Override
            public void run() {
                call = apiInterface.getLatestNews("us", API_KEY);
                call.enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getArticle() != null) {
                                if (!articles.isEmpty()) {
                                    articles.clear();
                                }
                                articles = response.body().getArticle();
                                newsAPIHorizontalLayout = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                                newsViewHorizontal.setLayoutManager(newsAPIHorizontalLayout);
                                newsAdapterHorizontal = new NewsAdapterHorizontal(articles, activity);
                                newsViewHorizontal.setAdapter(newsAdapterHorizontal);

                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<News> call, Throwable t) {
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        newsViewHorizontal.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss);
                    }
                });
            }
        });
        loadSourceAPI.start();
    }


    @Override
    public void onBackPressed() {
        if (drawerNewsAPI.isDrawerOpen(GravityCompat.START)) {
            drawerNewsAPI.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int menuitem = item.getItemId();
        if (menuitem == R.id.home_menu) {
            intent = new Intent(NewsAPI_Page.this, HomePage.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.source_menu) {
            intent = new Intent(NewsAPI_Page.this, SourceNewsList.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.favourite_menu) {
            intent = new Intent(NewsAPI_Page.this, FavouriteNews.class);
            startActivity(intent);
        }

        return true;
    }
}