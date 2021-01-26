package com.adhimbagas.speakup.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.adhimbagas.speakup.R
import com.adhimbagas.speakup.databinding.ActivityLoginSignUpBinding

class LoginSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityLoginSignUpBinding>(this,R.layout.activity_login_sign_up)



    }
}