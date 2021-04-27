package com.example.imovi3.objects

import android.app.Application
import android.content.Context
import com.example.imovi3.R
import org.acra.ACRA
import org.acra.ReportField
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import org.acra.config.ACRAConfiguration
import org.acra.config.ConfigurationBuilder
import org.acra.sender.HttpSender

@ReportsCrashes(mailTo = "vetaldaaa@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

class ACRA_Crash_Handler : Application() {
    override fun onCreate() {
        super.onCreate()
        val config: ACRAConfiguration = ConfigurationBuilder(this).build()
        ACRA.init(this, config)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        val config: ACRAConfiguration = ConfigurationBuilder(this).build()
        ACRA.init(this, config)
    }
}