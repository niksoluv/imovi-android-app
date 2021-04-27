package com.example.imovi3.background

import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.imovi3.R
import com.example.imovi3.activities.DetailActivity
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class NotificationService : Service() {
    var timer: Timer? = null
    var timerTask: TimerTask? = null
    var TAG = "Timers"
    var Your_X_SECS = 5
    lateinit var OLD_FILM: String
    lateinit var NEW_FILM: String
    var id: String = ""

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        NEW_FILM = latestFilm()
        OLD_FILM = NEW_FILM
        super.onStartCommand(intent, flags, startId)
        startTimer()
        return START_STICKY
    }

    override fun onCreate() {
        Log.e(TAG, "onCreate")
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        stopTimerTask()
        super.onDestroy()
    }

    //we are going to use a handler to be able to run in our TimerTask
    val handler: Handler = Handler()
    private fun startTimer() {
        timer = Timer()

        initializeTimerTask()
        timer!!.schedule(timerTask, 5000, (Your_X_SECS * 1000).toLong())
    }

    fun latestFilm(): String {
        var url = resources.getString(com.example.imovi3.R.string.url_latest) +
                resources.getString(com.example.imovi3.R.string.api_key)
        var req_queue: RequestQueue = Volley.newRequestQueue(this)

        val req: JsonObjectRequest  = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    id = response.get("id").toString()
                },
                { print("That didn't work!") })
        req_queue.add(req)
        return id
    }

    fun stopTimerTask() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                NEW_FILM = latestFilm()
                //if(NEW_FILM!=OLD_FILM) {
                    handler.post(Runnable { createNotification(NEW_FILM) })
                    OLD_FILM = NEW_FILM
                //}
            }
        }
    }
    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    private fun createNotification(id: String) {
        var url = resources.getString(R.string.url_latest) +
                resources.getString(R.string.api_key)
        var req_queue: RequestQueue = Volley.newRequestQueue(this)

        val req: JsonObjectRequest  = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val intent: Intent = Intent(this, DetailActivity::class.java).apply {
                        putExtra("film_id", id)
                    }
                    val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                        //addNextIntentWithParentStack(intent)
                        addNextIntent(intent)
                        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                    }
                    val image_url = response.get("poster_path").toString()
                    var bmp: Bitmap?
                    if(image_url!="null")
                        bmp = getBitmapFromURL(image_url)
                    else
                        bmp = BitmapFactory.decodeResource(this.resources,
                                R.drawable.ic_imovi)

                    var builder = NotificationCompat.Builder(this, default_notification_channel_id)
                            .setSmallIcon(R.drawable.ic_small)
                            .setLargeIcon(bmp)
                            .setContentTitle(response.getString("original_title"))
                            .setContentText("New movie is out! " + response.getString("title"))
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true)
                    //.addAction(R.drawable.default_image, "save to favorites", resultPendingIntent)
                    with(NotificationManagerCompat.from(this)) {
                        notify(System.currentTimeMillis().toInt(), builder.build())
                    }
                },
                { print("That didn't work!") })
        req_queue.add(req)
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        const val default_notification_channel_id = "default"
    }
}