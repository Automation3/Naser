package com.example.no1.photongallery

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView

class login1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)

        val phone_number = findViewById(R.id.phone_number) as EditText
        val number = phone_number.text

        val next_activity = findViewById(R.id.imageView2) as ImageView
        next_activity.setOnClickListener {
            val intent = Intent(this, login2::class.java)
            intent.putExtra("number", number)
            startActivity(intent)

        }


    }
}


