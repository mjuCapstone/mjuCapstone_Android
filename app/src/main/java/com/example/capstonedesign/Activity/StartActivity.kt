package com.example.capstonedesign.Activity

import Response.AutoLoginResponse
import Service.AutoLoginService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonedesign.R
import com.example.capstonedesign.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.start)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var btnLogin : ImageButton = findViewById(R.id.btnMainLogin)
        var btnSignUp : ImageButton = findViewById(R.id.btnMainSignUp)
        var btnFind : Button = findViewById(R.id.btnMainFind)
        btnLogin.setOnClickListener{
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        btnSignUp.setOnClickListener{
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        var rootLayout : LinearLayout = findViewById(R.id.start)
        //자동 로그인 시도
        var preferences = getSharedPreferences("APP", MODE_PRIVATE)
        var token = preferences.getString("token", null)
        var intentToMain = Intent(this, MainActivity::class.java)
        if(token != null){
            val autoLoginService = RetrofitClient.setRetroFitInstanceWithToken(applicationContext).create(AutoLoginService::class.java)
            autoLoginService.autoLogin().enqueue(object : retrofit2.Callback<AutoLoginResponse>{
                override fun onResponse(
                    call: Call<AutoLoginResponse>,
                    response: Response<AutoLoginResponse>
                ) {
                    if(response.isSuccessful) {
                        response.body()?.let {
                            startActivity(intentToMain)
                            finish()
                        }
                    }
                    else{
                        Toast.makeText(applicationContext, "로그인 세션이 만료되었습니다. 다시 로그인 하세요", Toast.LENGTH_SHORT).show()
                        rootLayout.visibility = LinearLayout.VISIBLE
                    }
                }

                override fun onFailure(call: Call<AutoLoginResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
        else{
            rootLayout.visibility = LinearLayout.VISIBLE
        }
    }
}