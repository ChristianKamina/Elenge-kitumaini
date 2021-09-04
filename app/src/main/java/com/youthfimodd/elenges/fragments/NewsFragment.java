package com.youthfimodd.elenges.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.youthfimodd.elenges.Main2Activity;
import com.youthfimodd.elenges.NewsdetailsActivity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.custom.news_models.ApiClient;
import com.youthfimodd.elenges.custom.news_models.ApiInterface;
import com.youthfimodd.elenges.custom.news_models.Articles;
import com.youthfimodd.elenges.custom.news_models.News;
import com.youthfimodd.elenges.custom.news_models.RviewAdapter_News;
import com.youthfimodd.elenges.custom.news_models.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements onRefresh, SwipeRefreshLayout.OnRefreshListener{
    //
    public static final String API_KEY = "d4160fa3cbef4f9a84536d207d4157ea";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Articles> articles = new ArrayList<>();
    private RviewAdapter_News adapter;
    private String TAG = NewsFragment.class.getSimpleName();
    private TextView tophandline;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Nom sur ActionBar
        ((Main2Activity) Objects.requireNonNull(getActivity())).SetActionBarTitle("Je m'informe");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        //actualiser
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        //swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        tophandline = view.findViewById(R.id.topheardline);

        recyclerView = view.findViewById(R.id.recyclerView_News);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        onloadingSwipeRefresh("");
        //
        errorLayout = view.findViewById(R.id.errorLayout);
        errorImage = view.findViewById(R.id.errorImage);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);
        //
        return view;
    }
    //
    public void loadJson(final String keyword){

        errorLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String Country = Utils.getCountry();
        Call<News> call;
        call = apiInterface.getNews(Country,API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null){

                    if (!articles.isEmpty()){
                        articles.clear();
                    }

                    articles = response.body().getArticle();
                    adapter = new RviewAdapter_News(articles, getContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    initlistener();

                    tophandline.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                }else {

                    tophandline.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();

                    String errorCode;

                    switch (response.code()){
                        case 404:
                            errorCode = "404 not found";
                            break;

                        case 500:
                            errorCode = "500 server broken";
                            break;

                        default:
                            errorCode = "unknown error";
                            break;
                    }
                    showErrorMessage(
                            R.mipmap.no_result,
                            "Pas de Resultat",
                            " Please, Try again! \n"+
                                    errorCode);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                tophandline.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(
                        R.mipmap.no_internet,
                        "Ooops",
                        " Pas de connexion internet. \n Vérifier votre connexion puis réessayer \n"/*+
                                //t.toString()*/);
            }
        });
    }
    //
    private void initlistener(){

        adapter.setOnItemClickListener(new RviewAdapter_News.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ImageView imageView = view.findViewById(R.id.img);
                Intent intent = new Intent(getContext(), NewsdetailsActivity.class);

                Articles article = articles.get(position);
                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img", article.getUrlToImage());
                intent.putExtra("date", article.getPublishedAt());
                intent.putExtra("source", article.getSource().getName());
                intent.putExtra("author", article.getAuthor());

                Pair<View, String> pair = Pair.create((View)imageView, ViewCompat.getTransitionName(imageView));

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    startActivity(intent, optionsCompat.toBundle());
                }else {
                    startActivity(intent);
                }

            }
        });
    }
    //actualiser
    @Override
    public void onRefresh() {
        //
        loadJson("");
    }

    private void onloadingSwipeRefresh(final String keyword){

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        loadJson(keyword);
                    }
                }
        );
    }
    //
    private void showErrorMessage(int imageView, String title, String message){

        if (errorLayout.getVisibility() == View.GONE) {

            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onloadingSwipeRefresh("");
            }
        });
    }

}
