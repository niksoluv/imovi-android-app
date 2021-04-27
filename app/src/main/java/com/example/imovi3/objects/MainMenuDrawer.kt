package com.example.imovi2.objects

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.imovi3.R
import com.example.imovi3.fragments.*
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import kotlinx.android.synthetic.main.activity_main.*

class MainMenuDrawer(val mainActivity: AppCompatActivity, val toolbar: Toolbar) {

    private lateinit var mDrawer: Drawer

    fun create(){
        mDrawer = DrawerBuilder()
            .withActivity(mainActivity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(1)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(100)
                    .withName("All")
                    .withSelectable(false)
                    .withIcon(R.drawable.worldwide),
                PrimaryDrawerItem().withIdentifier(100)
                    .withName("Popular")
                    .withSelectable(false)
                    .withIcon(R.drawable.worldwide),
                PrimaryDrawerItem().withIdentifier(100)
                    .withName("Trending")
                    .withSelectable(false)
                    .withIcon(R.drawable.worldwide),
                PrimaryDrawerItem().withIdentifier(100)
                    .withName("Top Rated")
                    .withSelectable(false)
                    .withIcon(R.drawable.worldwide),
                PrimaryDrawerItem().withIdentifier(100)
                    .withName("Upcoming")
                    .withSelectable(false)
                    .withIcon(R.drawable.worldwide),
                PrimaryDrawerItem().withIdentifier(100)
                    .withName("Latest")
                    .withSelectable(false)
                    .withIcon(R.drawable.worldwide),
                PrimaryDrawerItem().withIdentifier(100)
                    .withName("Search")
                    .withSelectable(false)
                    .withIcon(R.drawable.worldwide),
                DividerDrawerItem(),
                PrimaryDrawerItem().withIdentifier(100)
                    .withName("Favorites")
                    .withSelectable(false)
                    .withIcon(R.drawable.worldwide)
            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when(position){
                        0->{
                            val fragment = AllFragment()
                            mainActivity.supportFragmentManager.beginTransaction().apply {
                                replace(R.id.Output, fragment)
                                commit()
                            }
                            mainActivity.mainToolbar.title = "All"
                        }
                        1->{
                            val fragment = PopularFragment()
                            mainActivity.supportFragmentManager.beginTransaction().apply {
                                replace(R.id.Output, fragment)
                                commit()
                            }
                            mainActivity.mainToolbar.title = "Popular"
                        }
                        2->{
                            val fragment = TrendingFragment()
                            mainActivity.supportFragmentManager.beginTransaction().apply {
                                replace(R.id.Output, fragment)
                                commit()
                            }
                            mainActivity.mainToolbar.title = "Trending"
                        }
                        3->{
                            val fragment = TopRatedFragment()
                            mainActivity.supportFragmentManager.beginTransaction().apply {
                                replace(R.id.Output, fragment)
                                commit()
                            }
                            mainActivity.mainToolbar.title = "Top Rated"
                        }
                        4->{
                            val fragment = UpcomingFragment()
                            mainActivity.supportFragmentManager.beginTransaction().apply {
                                replace(R.id.Output, fragment)
                                commit()
                            }
                            mainActivity.mainToolbar.title = "Upcoming"
                        }
                        5->{
                            val fragment = LatestFragment()
                            mainActivity.supportFragmentManager.beginTransaction().apply {
                                replace(R.id.Output, fragment)
                                commit()
                            }
                            mainActivity.mainToolbar.title = "Latest"
                        }
                        6->{
                            val fragment = SearchFragment()
                            mainActivity.supportFragmentManager.beginTransaction().apply {
                                replace(R.id.Output, fragment)
                                commit()
                            }
                            mainActivity.mainToolbar.title = "Search"
                        }
                        8->{
                            val fragment = FavoritesFragment()
                            mainActivity.supportFragmentManager.beginTransaction().apply {
                                replace(R.id.Output, fragment)
                                commit()
                            }
                            mainActivity.mainToolbar.title = "Favorites"
                        }
                    }
                    return false
                }
            }).build()
    }
}