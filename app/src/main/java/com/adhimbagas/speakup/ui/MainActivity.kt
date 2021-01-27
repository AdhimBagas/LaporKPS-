package com.adhimbagas.speakup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.adhimbagas.speakup.R
import com.adhimbagas.speakup.databinding.ActivityMainBinding
import com.adhimbagas.speakup.ui.auth.LoginSignUpActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, LoginSignUpActivity::class.java)
            startActivity(intent)
        }


    }
}