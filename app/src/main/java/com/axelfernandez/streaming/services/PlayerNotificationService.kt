package com.axelfernandez.streaming.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import com.axelfernandez.streaming.R
import com.axelfernandez.streaming.ui.audioStreaming.AudioStreamingFragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util


class PlayerNotificationService : Service() {

    private lateinit var mPlayer: SimpleExoPlayer
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private val binder = LocalBinder()

    private var notificationId = 123;
    private var channelId = "channelId"

    val isPlaying : Boolean
        get() = mPlayer.isPlaying

    fun play(){
        mPlayer.prepare()
        mPlayer.play()
    }
    fun pause(){
        mPlayer.pause()
    }
    override fun onCreate() {
        super.onCreate()
        val context = this

        mPlayer = SimpleExoPlayer.Builder(this).build()
        // Create a data source factory.
        dataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(context, "app-name"))
        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri("http://centova2.veemesoft.com.ar:8347/live")
            .build()
        mPlayer.setMediaItem(mediaItem)
        mPlayer.playWhenReady = true

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            channelId,
            R.string.channel_name,
            R.string.channel_desc,
            notificationId,
            object : PlayerNotificationManager.MediaDescriptionAdapter {



                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    // return pending intent
                    val intent = Intent(context, AudioStreamingFragment::class.java);
                    return PendingIntent.getActivity(
                        context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                //pass description here
                override fun getCurrentContentText(player: Player): String? {
                    return "Radio Paradise"
                }

                //pass title (mostly playing audio name)
                override fun getCurrentContentTitle(player: Player): String {
                    return "Estás Escuchando"
                }

                // pass image as bitmap
                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    return BitmapFactory.decodeResource(context.getResources(),R.drawable.radioparadise)
                }
            },
            object : PlayerNotificationManager.NotificationListener {

                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    onGoing: Boolean) {

                    startForeground(notificationId, notification)

                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    stopSelf()
                }

            }
        )
        //attach player to playerNotificationManager
        playerNotificationManager.setPlayer(mPlayer)
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mPlayer.play()
        return START_STICKY
    }

    // detach player
    override fun onDestroy() {
        playerNotificationManager.setPlayer(null)
        mPlayer.release()
        super.onDestroy()
    }

    //removing service when user swipe out our app
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    inner class LocalBinder : Binder(){
        fun getService(): PlayerNotificationService = this@PlayerNotificationService

    }
}