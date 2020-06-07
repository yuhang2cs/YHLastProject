package com.example.lasthomework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private List<videoInfo> videoInfos;//将要展示的viedeoInfo数组
    public List<videoInfo> getVideoInfos() {
        return videoInfos;
    }

    public void setVideoInfos(List<videoInfo> videoInfos) {
        this.videoInfos = videoInfos;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(videoInfos!=null) {
            try {
                holder.bindData(videoInfos.get(position));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                holder.bindData(null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        if(videoInfos!=null)
        return videoInfos.size();
        else {
            return 0;
        }
    }

   public static class MyViewHolder extends RecyclerView.ViewHolder {
        private CustomView videoView;
        private View playButton;
        private CircleImageView circleImageView;
        private View holdeView;
        private TextView nickname;
        private TextView description;
        private ImageView firstImage;
        private Like like;
        private View heart;
        private TextView likeCount;
        private boolean isFirst=true;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            holdeView=itemView;
            playButton=itemView.findViewById(R.id.playbtn);
            videoView=itemView.findViewById(R.id.video);
            circleImageView=itemView.findViewById(R.id.circle);
            nickname=itemView.findViewById(R.id.nickname);
            description=itemView.findViewById(R.id.description);
            firstImage=itemView.findViewById(R.id.firstImage);
            like=itemView.findViewById(R.id.like);
            heart=itemView.findViewById(R.id.heart);
            likeCount=itemView.findViewById(R.id.likecount);
        }

        void bindData(videoInfo video) throws FileNotFoundException {
            if(video!=null){
                like.setVideo(video);
                Uri uri=Uri.parse(video.feedurl);
                videoView.setVideoURI(uri);
                Uri avatarUri=Uri.parse(video.avatar);
                Glide.with(holdeView.getContext())
                     .load(avatarUri)
                     .into(circleImageView);
                String strNick="@"+video.nickname;
                nickname.setText(strNick);
                description.setText(video.description);
                likeCount.setText(Integer.toString(video.likecount));
                //TODO 第一帧放视频有延迟
                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("yuhang","btn");
                    }
                });
                like.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                if(like.isDoubleClick()){//出现双击事件
                                    like.setDoubleClickFalse();//修改doubleClick为false
                                    video.likecount++;//点赞数加1
                                    likeCount.setText(Integer.toString(video.likecount));//重新设置点赞数
                                    heart.setBackgroundResource(R.mipmap.ic_heart);//修改爱心图片为红心图片
                                }
                        }
                        return false;//继续传递触摸事件，从而触发onClick
                    }
                });
                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(videoView.isPlaying()){//视频正在播放
                            videoView.pause();//暂停
                            playButton.setVisibility(View.VISIBLE);//显示播放按钮
                        }
                        else {
                            videoView.start();//播放
                            playButton.setVisibility(View.INVISIBLE);//隐藏播放按钮
                        }
                    }
                });

                firstImage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                if(isFirst){
                                    videoView.setVisibility(View.VISIBLE);//显示视频
                                    videoView.start();//视频播放
                                    playButton.setVisibility(View.INVISIBLE);//播放按钮
                                }
                                break;
                        }
                        return false;
                    }
                });
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                                    firstImage.setVisibility(View.INVISIBLE);
                                return true;
                            }
                        });
                    }
                });
                if(isFirst){
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(video.feedurl,new HashMap<>());

                    Bitmap bitmap = mmr.getFrameAtTime();//获取第一帧图片
                    firstImage.setImageBitmap(bitmap);
                    mmr.release();//释放资源
                }
            }

        }

    }
}
