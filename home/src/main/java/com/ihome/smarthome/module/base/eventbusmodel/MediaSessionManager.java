package com.ihome.smarthome.module.base.eventbusmodel;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/2/7 16:18
 */




import android.media.session.MediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.ihome.smarthome.service.ConnectionService;


/**
 * Created by Administrator on 2019/4/3.
 */

public class MediaSessionManager {
    private static final String TAG = "MediaSessionManager";
    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO;

    private ConnectionService playService;
    private MediaSessionCompat mediaSession;

    public static MediaSessionManager get(){
        return SingletonHolder.instance;
    }

    private static class SingletonHolder{
        private static MediaSessionManager instance = new MediaSessionManager();
    }

    private MediaSessionManager(){

    }

    //音乐的控制逻辑都在PlayService服务中，将service实例传递过来，与MediaSessionManager进行交互
    public void init(ConnectionService playService){
        this.playService = playService;
        setupMediaSession();
    }

    private void setupMediaSession(){
        mediaSession = new MediaSessionCompat(playService, TAG);
        //指明支持的按键信息类型
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mediaSession.setCallback(callback);//设置监听回调
        mediaSession.setActive(true);//必须设置为true，这样才能开始接收各种信息


    }

 /*   //点击控制按钮时，更新播放状态
    public void updatePlaybackState(){
        int state = (AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing()) ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
        //第三个参数必须为1，否则锁屏上面显示的时长会有问题
        mediaSession.setPlaybackState(
                //监听的事件（播放，暂停，上一曲，下一曲）
                new PlaybackStateCompat.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(state, AudioPlayer.get().getAudioPosition(), 1)
                        .build());
    }

    //播放歌曲时，需要更新屏幕上的歌曲信息
    public void updateMetaData(Music music){
        if(music == null){
            mediaSession.setMetadata(null);
            return;
        }

        //Log.d(TAG, "parseMp3File名称: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        //Log.d(TAG, "parseMp3File专辑: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        //Log.d(TAG, "parseMp3File歌手: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        //Log.d(TAG, "parseMp3File码率: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
        //Log.d(TAG, "parseMp3File时长: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        //Log.d(TAG, "parseMp3File类型: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE));

        MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, music.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, music.getAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, music.getArtist())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, music.getDuration())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, CoverLoader.get().loadThumb(music));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, AppCache.get().getLocalMusicList().size());
        }

        mediaSession.setMetadata(metaData.build());
    }*/

    //初始化回调，用于监听锁屏界面上的按钮事件
    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
           // AudioPlayer.get().playPause();
        }

        @Override
        public void onPause() {
          //  AudioPlayer.get().playPause();
        }

        @Override
        public void onSkipToNext() {
          //  AudioPlayer.get().next();
        }

        @Override
        public void onSkipToPrevious() {
           // AudioPlayer.get().prev();
        }

        @Override
        public void onStop() {
          //  AudioPlayer.get().stopPlayer();
        }

        @Override
        public void onSeekTo(long pos) {
           // AudioPlayer.get().seekTo((int)pos);
        }
    };

}