package com.example.lasthomework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private VideoAdapter videoAdapter;
    private ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoAdapter = new VideoAdapter();
        viewPager2=findViewById(R.id.viewpager2);
        viewPager2.setAdapter(videoAdapter);
        getVideo();
        if(getActionBar()!=null)
            getActionBar().hide();//隐藏标题栏
    }
    private void getVideo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/api/invoke/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getVideoInfo().enqueue(new Callback<List<videoInfo>>() {
            @Override
            public void onResponse(Call<List<videoInfo>> call, Response<List<videoInfo>> response) {
                if (response.body() != null) {
                    List<videoInfo> videos=response.body();
                    if (videos.size() != 0) {
                        videoAdapter.setVideoInfos(videos);
                        videoAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<videoInfo>> call, Throwable t) {
                Log.d("retrofit", t.getMessage());
            }
        });

    }
}
