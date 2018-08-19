package com.example.no1.photongallery

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class login2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        val number =  intent.getStringExtra("number")
        val show_number =findViewById(R.id.txt_phone_number) as TextView
       // show_number.setText(number)

        val next_activity2 = findViewById(R.id.imageView3) as ImageView
        next_activity2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }
}
