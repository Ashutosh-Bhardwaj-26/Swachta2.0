package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.houseowner.MapActivity
import com.example.myapplication.houseowner.UploadActivity

class MainActivity  : AppCompatActivity() {

    private lateinit var btn_ml : Button
    private lateinit var btn_map : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_map = findViewById(R.id.btn_map)
        btn_ml = findViewById(R.id.btn_ml)

        btn_map.setOnClickListener(){
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        btn_ml.setOnClickListener(){
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }


    }
}