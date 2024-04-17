package com.example.capstonedesign.Activity

import Data.LoginData
import Response.LoginResponse
import Service.LoginService
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonedesign.R
import com.example.capstonedesign.RetrofitClient
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    fun print(message : String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    private fun getCurrentToken() : String?{
        var preferences = getSharedPreferences("APP",MODE_PRIVATE)
        return preferences.getString("token", null)
    }
    // null 이 아닐 때 let 구분이 실행되도록 설정 요청 후 응답받는 값이 null이 아닐때 설정.
    // 최초 로그인시 저장됩니다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var preferences = getSharedPreferences("APP",Context.MODE_PRIVATE)
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
                        Log.d("test", loginData.toString())
                        /*TODO("메인화면 진입 후 뒤로가기 버튼을 눌렀을 때 " +
                                "어플리케이션을 종료할지 물어볼 것인지 아니면 로그인 화면으로 돌아갈 것인지" +
                                "정하기")*/
                        //토큰 처리 메소드
                        when(response.code()){
                            200 -> {
                                response.body()?.let{
                                    var accessToken = getCurrentToken()
                                    var successData = it.data
                                    if(accessToken != successData.accessToken){
                                        preferences.edit().putString("token", successData.accessToken)
                                    }
                                    print(successData.accessToken)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            else ->{
                                print("로그인에 실패하셨습니다.")
                                print(response.code().toString() + ":" + response.message())
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("errorMsg", t.message.toString())
                        print(t.message.toString())
                    }
                })
            }

        }
    }
}