package com.psj.qiushibaike;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.MyAdapter;
import data.Data;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    public MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset = {"ab", "cd", "ef", "gh", "ij", "kl"};
    private List<Data.ItemsEntity> newsList = new ArrayList<>();
    public String time = "";
    public String path = "";
    public Data data;
    public SwipeRefreshLayout swipeRefreshLayout;

    private Handler requestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
//                    Toast.makeText(MainActivity.this, "request success", Toast.LENGTH_SHORT).show();
                    mAdapter.setData(newsList);
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case 0:
                    Toast.makeText(MainActivity.this, "request failed", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = System.currentTimeMillis() + "";
        path = "http://api.ithome.com/xml/newslist/news.xml?r=" + time;
        Log.e("time", time + "");
        setContentView(R.layout.recyleview_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        swipeRefreshLayout.setOnRefreshListener(this);
        data = new Data();
        getData();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this);


        mRecyclerView.setAdapter(mAdapter);

       /* mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                getData();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });*/
    }

    public void getData() {
        //用okHttp,可以使用
        newsList.clear();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url("http://m2.qiushibaike.com/article/list/suggest?page=1&type=refresh&count=30").build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("reponsekkkkkkkkkkkkk",response.body().string());
                Gson gson = new Gson();
                data = gson.fromJson(response.body().string(), new TypeToken<Data>() {
                }.getType());
//                newsList = data.getItems();
                for (int i = 0; i < data.getItems().size(); i++) {
                    newsList.add(data.getItems().get(i));
                }
//                mAdapter.setData(data.getItems());
//                mAdapter.notifyDataSetChanged();
                Message msg = new Message();
                msg.what = 2;
                Bundle bundle = new Bundle();
                msg.setData(bundle);
                requestHandler.sendMessage(msg);

            }
        });
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
