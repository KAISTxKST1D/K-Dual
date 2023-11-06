package com.example.k_dual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSet = this.findViewById<Button>(R.id.settingButton)
        btnSet.setOnClickListener {
            val intent = Intent(this, PopUpUrl::class.java)
            startActivity(intent)
        }
    }

}