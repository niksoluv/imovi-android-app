package com.example.imovi3.activities

import android.R.attr.description
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Images
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.imovi2.objects.*
import com.example.imovi3.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONArray
import java.time.LocalDate


class DetailActivity : AppCompatActivity() {

    private lateinit var mFilmId: String
    var filmInDatabase: Boolean = false
    private lateinit var detailMenuDrawer: DetailMenuDrawer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        init()
        GetData()
    }

    private fun init(){
        detailMenuDrawer = DetailMenuDrawer(this, detailToolbar)
        detailMenuDrawer.create()
        setSupportActionBar(detailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = intent.getStringExtra("film_title")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun GetData(){

        val req_queue: RequestQueue = Volley.newRequestQueue(this)
        val my_intent = intent
        val film_id = my_intent.getStringExtra("film_id")
        mFilmId = film_id.toString()
        var url: String = resources.getString(R.string.url_detail) + film_id +
                resources.getString(R.string.api_key)

        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                var db = DBHandler(this)
                var data = db.readData()
                for (i in 0..(data.size - 1)) {
                    if (data[i].filmId == film_id) {
                        favorites_button.text = "REMOVE FROM FAVORITES"
                        filmInDatabase = true
                    }
                }

                var date_text = response.get("release_date").toString()
                val date: LocalDate
                if (date_text != "")
                    date = LocalDate.parse(response.get("release_date").toString())
                else
                    date = LocalDate.now()
                val info = response.get("poster_path")
                val image_url = "https://image.tmdb.org/t/p/w500/" + info.toString()
                val genres_arr = response.getJSONArray("genres")
                val tagline = response.get("tagline")
                val language = response.get("original_language")
                val duration = response.get("runtime")
                val status = response.get("status")
                val budget = response.get("budget")
                val sv = ScrollView(this)
                val overview_text: TextView = TextView(this)
                val movie_title = response.get("original_title").toString()

                language_text.append(language.toString())
                duration_text.append(duration.toString() + " min")
                status_text.append(status.toString())
                budget_text.append(budget.toString() + "$")
                val poster_path = response.get("poster_path").toString()
                if (poster_path != "null") {
                    Picasso.get().load(image_url).into(film_image)
                } else
                    film_image.setImageBitmap(
                        BitmapFactory.decodeResource(
                            this.resources,
                            R.drawable.poster_not_found
                        )
                    )
                original_title.text = movie_title.toString()
                TMDB_text.append(response.get("vote_average").toString())
                year_text.append(date.year.toString())
                overview_text.setTextSize(22f)
                overview_text.text = response.get("overview").toString()
                sv.addView(overview_text)
                overview_layout.addView(sv)

                tagline_text.append(tagline.toString())
                var trailer_butt: Button = findViewById(R.id.trailer_button)


                for (item in 0 until genres_arr.length()) {
                    val genre = genres_arr.getJSONObject(item)
                    genre_text.append(genre.get("name").toString())
                    if (item < genres_arr.length() - 1) {
                        genre_text.append(", ")
                    }
                }
                val video_available = response.get("video")

                val sub_url =
                    resources.getString(R.string.url_detail) + film_id + resources.getString(
                        R.string.detail_api_key
                    )
                val sub_req = JsonObjectRequest(
                    Request.Method.GET, sub_url, null,
                    { response ->
                        val arr: JSONArray = response.getJSONArray("results")
                        if (arr.length() > 0) {
                            val item = arr.getJSONObject(0)

                            val film_key = item.getString("key")
                            film_image.setOnClickListener {
                                AlertDialog.Builder(this)
                                    .setPositiveButton("share") { dialog, which ->
                                        val youtube_url =
                                            resources.getString(R.string.you_tube_url) + film_key
                                        ShareCompat.IntentBuilder.from(this)
                                            .setType("text/plain")
                                            .setChooserTitle("Share URL")
                                            .setText(youtube_url)
                                            .setSubject(movie_title)
                                            .startChooser()
                                    }
                                    .setNegativeButton("download") { dialog, which ->
                                        film_image.setDrawingCacheEnabled(true)
                                        val b: Bitmap = film_image.getDrawingCache()
                                        Images.Media.insertImage(
                                            getContentResolver(),
                                            b,
                                            "title",
                                            "description"
                                        )
                                        Toast.makeText(this, "Saved to gallery", Toast.LENGTH_SHORT).show()
                                    }
                                    .show()
                            }
                            trailer_butt.setOnClickListener {
                                val intent: Intent =
                                    Intent(this, TrailerActivity::class.java).apply {
                                        putExtra("video_key", film_key)
                                    }
                                startActivity(intent)
                            }
                        } else {
                            film_image.setOnClickListener {
                                Toast.makeText(
                                    this,
                                    "Sorry, link is unsvsilable",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            trailer_butt.setOnClickListener {
                                Toast.makeText(
                                    this,
                                    "Sorry, video is unsvsilable",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    { print("That didn't work!") })
                req_queue.add(sub_req)


                var favorites_butt: Button = findViewById(R.id.favorites_button)
                favorites_butt.setOnClickListener {
                    var film = Films(film_id.toString())
                    var db = DBHandler(this)
                    if (!filmInDatabase) {
                        db.insertData(film)
                        favorites_button.text = "REMOVE FROM FAVORITES"
                    } else {
                        db.deleteData(film)
                        favorites_button.text = "ADD TO FAVORITES"
                    }
                    filmInDatabase = !filmInDatabase
                }
            },
            { print("That didn't work!") })
        req_queue.add(req)
    }
}