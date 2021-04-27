package com.example.imovi3.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.imovi3.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_trailer.*

class TrailerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)
        val youTubePlayerView  = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "imovi        Trailer"
        }
        //lifecycle.addObserver(youTubePlayerView)
        val my_intent = getIntent()
        val film_key = my_intent.getStringExtra("video_key").toString()
        youTubePlayerView.enableBackgroundPlayback(true)
        //youTubePlayerView.enterFullScreen()
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(film_key, 0f)
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                Toast.makeText(this@TrailerActivity, "Error(((", Toast.LENGTH_SHORT).show()
            }
        })
    }
}