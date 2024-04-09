package com.example.capstonedesign

import Data.LoginData
import Response.LoginResponse
import Service.LoginService
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Response
import retrofit2.create

class LoginActivity : AppCompatActivity() {
    fun print(message : String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnSignup : Button = findViewById(R.id.btnGoToSignUp)
        btnSignup.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        btnSignup.setOnClickListener{
            var intent = Intent(this, SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val btnLogin : ImageButton = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            var edtId = findViewById<EditText>(R.id.edtId_login)
            var edtPw = findViewById<EditText>(R.id.edtPw_login)
            if(edtId.text.toString().equals("")){
                print("아이디를 입력해주세요")
            }
            else if(edtPw.text.toString().equals("")){
                print("비밀번호를 입력해주세요")
            }
            else{
                var loginData = LoginData(edtId.text.toString(), edtPw.text.toString())
                var loginService = RetrofitClient.retrofitInstance.create(LoginService::class.java)
                var intent = Intent(this, MainActivity::class.java)
                loginService.login(loginData).enqueue(object : retrofit2.Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.body()?.isSuccess == true){
                            print("로그인에 성공하셨습니다")
                            startActivity(intent)
                            TODO("메인화면 진입 후 뒤로가기 버튼을 눌렀을 때 " +
                                    "어플리케이션을 종료할지 물어볼 것인지 아니면 로그인 화면으로 돌아갈 것인지" +
                                    "정하기")
                            finish() 
                        }
                        else{
                            print("로그인에 실패하셨습니다")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        print(t.message.toString())
                    }
                })
            }

        }
    }
}