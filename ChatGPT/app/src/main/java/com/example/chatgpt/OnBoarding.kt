package com.example.chatgpt

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class OnBoarding : AppCompatActivity() {

    lateinit var recyclerView:RecyclerView
    lateinit var adapter: OnBoardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        supportActionBar?.hide()


        // onBoarding circle image by default
        val circle1 = findViewById<ImageView>(R.id.circle_1)
        circle1.setColorFilter(resources.getColor(R.color.white))

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = OnBoardingAdapter(this)

        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        recyclerView.setLayoutManager(linearLayoutManager)
        recyclerView.setAdapter(adapter)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val position = linearLayoutManager.findFirstVisibleItemPosition()
                val circle1 = findViewById<ImageView>(R.id.circle_1)
                val circle2 = findViewById<ImageView>(R.id.circle_2)
                val circle3 = findViewById<ImageView>(R.id.circle_3)
                val circle4 = findViewById<ImageView>(R.id.circle_4)
                val circle5 = findViewById<ImageView>(R.id.circle_5)
                val heading = findViewById<TextView>(R.id.heading)
                val desc = findViewById<TextView>(R.id.desc)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // Dragging
                } else  // idle state
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (position == 0) {
                            circle1.setColorFilter(resources.getColor(R.color.white))
                            circle2.setColorFilter(resources.getColor(R.color.black))
                            circle3.setColorFilter(resources.getColor(R.color.black))
                            circle4.setColorFilter(resources.getColor(R.color.black))
                            circle5.setColorFilter(resources.getColor(R.color.black))
                            heading.text = "Get all your Queries resolved"
                            desc.text = "powered by openAI ChatPT"
                        } else if (position == 1) {
                            circle1.setColorFilter(resources.getColor(R.color.black))
                            circle2.setColorFilter(resources.getColor(R.color.white))
                            circle3.setColorFilter(resources.getColor(R.color.black))
                            circle4.setColorFilter(resources.getColor(R.color.black))
                            circle5.setColorFilter(resources.getColor(R.color.black))
                            heading.text = "Writing cover letters \n essays and emails, etc"
                            desc.text = "instantly at anywhere and any time"
                        } else if (position == 2) {
                            circle1.setColorFilter(resources.getColor(R.color.black))
                            circle2.setColorFilter(resources.getColor(R.color.black))
                            circle3.setColorFilter(resources.getColor(R.color.white))
                            circle4.setColorFilter(resources.getColor(R.color.black))
                            circle5.setColorFilter(resources.getColor(R.color.black))
                            heading.text = "Generate images \n of any thing"
                            desc.text = "powered by Dall-E"
                        } else if (position == 3) {
                            circle1.setColorFilter(resources.getColor(R.color.black))
                            circle2.setColorFilter(resources.getColor(R.color.black))
                            circle3.setColorFilter(resources.getColor(R.color.black))
                            circle4.setColorFilter(resources.getColor(R.color.white))
                            circle5.setColorFilter(resources.getColor(R.color.black))
                            heading.text = "Voice/Text assistant"
                            desc.text = "Ask through text \n or by giving voice command \n similarly get results" +
                                    " in text or voice"
                        }
                        else if (position == 4) {
                            circle1.setColorFilter(resources.getColor(R.color.black))
                            circle2.setColorFilter(resources.getColor(R.color.black))
                            circle3.setColorFilter(resources.getColor(R.color.black))
                            circle4.setColorFilter(resources.getColor(R.color.black))
                            circle5.setColorFilter(resources.getColor(R.color.white))
                            heading.text = "Solve any programming Logic"
                            desc.text = "Genearate program of any language \n solve any complex problem"
                        }
                    }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })


    }

    fun btnGetStart(view: View) {
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}