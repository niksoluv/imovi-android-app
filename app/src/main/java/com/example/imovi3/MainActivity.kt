package com.example.imovi3

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.imovi2.objects.MainMenuDrawer
import com.example.imovi3.background.NotificationService
import com.example.imovi3.fragments.AllFragment
import com.example.imovi3.fragments.PopularFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import org.acra.config.ACRAConfiguration
import org.acra.config.ConfigurationBuilder
import org.acra.sender.ReportSenderException
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var mainMenuDrawer: MainMenuDrawer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = PopularFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.Output, fragment)
            commit()
        }

        init()
    }

    override fun onStop() {
        super.onStop()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){
        mainMenuDrawer = MainMenuDrawer(this, mainToolbar)
        mainMenuDrawer.create()
        mainToolbar.title = "Popular"
        createNotificationChannel()
        startService(Intent(this, NotificationService::class.java))
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NotificationService.default_notification_channel_id, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}