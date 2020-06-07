package com.example.lasthomework;

//TODO GSON讲解文章：https://www.cnblogs.com/tianzhijiexian/p/4246497.html

//import com.google.gson.annotations.SerializedName;

import java.util.List;


public class videoInfo {
    public String _id;
    public String feedurl;
    public String nickname;
    public String description;
    public int likecount;
    public String avatar;//展示的图片的url

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id=" + _id +
                ", feedurl='" + feedurl + '\'' +
                ", nickname" + nickname +
                ", description" + description +
                ", likecount" + likecount +
                ", avatar" + avatar +
                '}';
    }
}
