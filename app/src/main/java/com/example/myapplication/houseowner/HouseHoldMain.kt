package com.example.myapplication.houseowner

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.adapter.ViewPagerAdapter
import com.example.myapplication.auth.LoginActivity
import com.example.myapplication.databinding.ActivityHouseholdmainBinding
import com.example.myapplication.ui.CameraFragment
import com.example.myapplication.ui.MapFragment
import com.example.myapplication.ui.ProfileFragment

class HouseHoldMain: AppCompatActivity() {

    private var binding: ActivityHouseholdmainBinding?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHouseholdmainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var token = getSharedPreferences("userID", MODE_PRIVATE)


        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        toolbar.inflateMenu(R.menu.main_menu)

        val onMenuItemClickListener = Toolbar.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    var editor = token.edit()
                    editor.putString("loginUID"," ")
                    editor.commit()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Logged Out ", Toast.LENGTH_SHORT).show()
                }
            }
            false
        }

        toolbar.setOnMenuItemClickListener(onMenuItemClickListener)



        val fragmentArrayList = ArrayList<Fragment>()

        fragmentArrayList.add(MapFragment())
        fragmentArrayList.add(CameraFragment())
        fragmentArrayList.add(ProfileFragment())

        val adapter= ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)

        binding!!.viewPager.adapter = adapter
        binding!!.tabs.setupWithViewPager(binding!!.viewPager)



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)



        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}