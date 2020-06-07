package com.example.lasthomework;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    //https://beiyou.bytedance.com/api/invoke/video/invoke/video
    // https://beiyou.bytedance.com/api/invoke
    @GET("video/invoke/video")
    Call<List<videoInfo>> getVideoInfo();
}
