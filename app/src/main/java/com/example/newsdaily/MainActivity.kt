package com.example.newsdaily

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsdaily.R.id.recyclerView


class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter : NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        // val adapter  = NewsListAdapter(items , this)
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url =
            "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=f21228fd259041cfae092202ba4b144a"
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsjsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsjsonObject.getString("title"),
                        newsjsonObject.getString("author"),
                        newsjsonObject.getString("url"),
                        newsjsonObject.getString("urlToImage"),
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)

            },
            Response.ErrorListener { })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

// Add the request to the RequestQueue.

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val url = "https://developers.android.com"
        val builder =  CustomTabsIntent.Builder()
        val  CustomTabsIntent = builder.build()
        CustomTabsIntent.launchUrl(this , Uri.parse(item.url))

    }
}