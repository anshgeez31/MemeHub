package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

var currentImageUrl: String? = null
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shareMeme = findViewById<Button>(R.id.btnShare)
        val nextMeme = findViewById<Button>(R.id.btnNext)

        loadMemes()
        nextMeme.setOnClickListener {
            loadMemes()
        }

        shareMeme.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            // intent is string then we used putExtra
            intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme I got from Reddit $currentImageUrl")
            val chooser = Intent.createChooser(intent, "Share this meme using ...")
            startActivity(chooser)
        }
    }

    private fun loadMemes() {
        val progressBar = findViewById<ProgressBar>(R.id.pbLoad)
        progressBar.visibility = View.VISIBLE    // visibility of progress bar began

        val url = "https://meme-api.com/gimme" // API url which calls different reddit memes
        val imageMeme = findViewById<ImageView>(R.id.ivMeme)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                currentImageUrl = response.getString("url") // this line takes link of meme
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //progress bar visibilty commented
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //progress bar visibilty commented
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(imageMeme)
            },
            { error ->
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        )
        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}

