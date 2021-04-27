package com.example.imovi3.fragments

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.imovi3.R
import com.example.imovi3.activities.DetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.film_layout.view.*
import kotlinx.android.synthetic.main.fragment_all.*
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

class TopRatedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_rated, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseJSON()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseJSON() {
        var req_queue: RequestQueue = Volley.newRequestQueue(context)
        req_queue = Volley.newRequestQueue(context)
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val arrayItemsName: String
        var url: String

        url = resources.getString(R.string.url_top_rated) +
                resources.getString(R.string.api_key)
        arrayItemsName = "results"

        val req: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val arr: JSONArray = response.getJSONArray(arrayItemsName)
                for (item in 0 until arr.length()) {
                    val film_box: View =
                        layoutInflater.inflate(R.layout.film_layout, null)

                    var film_data: JSONObject = arr.getJSONObject(item)

                    var id: String = film_data.getString("id")
                    var original_title: String = film_data.getString("original_title")

                    var vote_average: Double = film_data.getDouble("vote_average")
                    var overview: String = film_data.getString("overview")
                    var release_date = LocalDate.parse(film_data.get("release_date").toString())
                    film_box.film_title.append(original_title)
                    film_box.release_date.append(release_date.year.toString())
                    film_box.TMDB_text_main.append(vote_average.toString())
                    film_box.overview_text.text = overview

                    var image_url =
                        "https://image.tmdb.org/t/p/w500/" + film_data.get("poster_path")
                    Picasso.get().load(image_url).into(film_box.image_box)
                    film_box.setOnClickListener {
                        val intent: Intent = Intent(
                            context,
                            DetailActivity::class.java
                        ).apply {
                            putExtra("film_id", id)
                            putExtra("film_title", original_title)
                        }
                        startActivity(intent)
                    }
                    layout.addView(film_box)
                }
            },
            { print("That didn't work!") })
            req_queue.add(req)

        var sv = ScrollView(context)
        sv.addView(layout)

        dataContainer.addView(sv)
    }

}