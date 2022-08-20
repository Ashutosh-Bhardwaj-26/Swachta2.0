package com.example.myapplication.houseowner

import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class ImageClassification: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    private var img_view : ShapeableImageView?=null

    private var name_view : MaterialTextView?=null
    private var phn_view : MaterialTextView?=null
    private var aadhar_view : MaterialTextView?=null
    private var address_view : MaterialTextView?=null
    private var location_view : MaterialTextView?=null

    private lateinit var lastlocation : Location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageclassification)

        database = FirebaseDatabase.getInstance().getReference("User")
        auth = Firebase.auth

        img_view = findViewById(R.id.userClickedImg)
        name_view = findViewById(R.id.name_data)
        location_view = findViewById(R.id.location_data)


        //taken form previous activity to make changes in this activity
        val takenimage = intent.getParcelableExtra("TakenImage") as Bitmap?
        val lastlocation = intent.getStringExtra("TakenLocation") as String
        img_view?.setImageBitmap(takenimage)
        location_view?.setText(lastlocation)

        val uid = auth.uid!!

        database.child(uid).get().addOnSuccessListener {
            name_view!!.setText(it.child("name").value.toString())
        }

    }
}