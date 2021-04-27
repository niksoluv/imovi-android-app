package com.example.imovi2.objects

import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialize.color.Material

class DetailMenuDrawer(val detailActivity: AppCompatActivity, val toolbar: Toolbar) {

    lateinit var mDrawer: Drawer

    fun create(){
        mDrawer = DrawerBuilder()
            .withActivity(detailActivity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(false)
            .withSelectedItem(1).build()
    }
}